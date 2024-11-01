package com.ytl.crm.consumer.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ytl.crm.common.exception.UgcCrmServiceException;
import com.ytl.crm.consumer.req.wechat.ThirdCustomerIdConvertReq;
import com.ytl.crm.consumer.req.wechat.ThirdEmpIdConvertReq;
import com.ytl.crm.consumer.req.wechat.WechatCreateQrCodeReq;
import com.ytl.crm.consumer.req.wechat.WechatDeleteQrCodeReq;
import com.ytl.crm.consumer.resp.wechat.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class WxOfficialConsumerHelper {

    private final WxOfficialConsumer wxOfficialConsumer;
    private final WxOfficialTokenHelper wxOfficialTokenHelper;


    private static final String GET_TMP_MATERIAL_MEDIA_URL = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";


    public String uploadFile(MultipartFile file, String fileType) throws Exception {


        String accessToken = wxOfficialTokenHelper.acquireAccessToken();

        String result = null;
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=" + accessToken + "&type=" + fileType;
        /**
         * 第一部分
         */
        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存
        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        // 设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        // 请求正文信息
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + file.getOriginalFilename() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");
        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        // 输出表头
        out.write(head);
        // 文件正文部分
        // 把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(file.getInputStream());
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        out.write(foot);
        out.flush();
        out.close();

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        try {
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            log.info("发送POST请求出现异常！ {}", e);
            e.printStackTrace();
            throw new IOException("数据读取异常");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        JSONObject jsonObj = JSON.parseObject(result);
        log.info("getMediaId jsonObj: {}", jsonObj);

        if (jsonObj.containsKey("errcode")) {
            return jsonObj.toJSONString();
        }

        // {"errcode":40001,"errmsg":"invalid credential, access_token is invalid or not latest, could get access_token by getStableAccessToken, more details at https://mmbizurl.cn/s/JtxxFh33r rid: 672068a8-73e07d73-19c9f940"}
        return jsonObj.getString("media_id");
    }


    public String uploadImg(MultipartFile file) throws Exception {


        String accessToken = wxOfficialTokenHelper.acquireAccessToken();

        String result = null;
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=" + accessToken;
        /**
         * 第一部分
         */
        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存
        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        // 设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        // 请求正文信息
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + file.getOriginalFilename() + "\"\r\n");
        sb.append("Content-Type:image/png\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");
        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        // 输出表头
        out.write(head);
        // 文件正文部分
        // 把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(file.getInputStream());
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        out.write(foot);
        out.flush();
        out.close();

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        try {
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            log.info("发送POST请求出现异常！ {}", e);
            e.printStackTrace();
            throw new IOException("数据读取异常");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        JSONObject jsonObj = JSON.parseObject(result);
        log.info("getMediaId jsonObj: {}", jsonObj);

        if (jsonObj.containsKey("errcode")) {
            return jsonObj.toJSONString();
        }

        // {"errcode":40001,"errmsg":"invalid credential, access_token is invalid or not latest, could get access_token by getStableAccessToken, more details at https://mmbizurl.cn/s/JtxxFh33r rid: 672068a8-73e07d73-19c9f940"}
        return jsonObj.getString("url");
    }


    public String downloadFile(FileInputStream fileInputStream, String fileName, String fileType) throws Exception {


        String accessToken = wxOfficialTokenHelper.acquireAccessToken();

        String result = null;
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=" + accessToken + "&type=" + fileType;
        /**
         * 第一部分
         */
        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存
        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        // 设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        // 请求正文信息
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + fileName + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");
        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        // 输出表头
        out.write(head);
        // 文件正文部分
        // 把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(fileInputStream);
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        out.write(foot);
        out.flush();
        out.close();

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        try {
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            log.info("发送POST请求出现异常！ {}", e);
            e.printStackTrace();
            throw new IOException("数据读取异常");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        JSONObject jsonObj = JSON.parseObject(result);
        log.info("getMediaId jsonObj: {}", jsonObj);

        if (jsonObj.containsKey("errcode")) {
            return jsonObj.toJSONString();
        }

        // {"errcode":40001,"errmsg":"invalid credential, access_token is invalid or not latest, could get access_token by getStableAccessToken, more details at https://mmbizurl.cn/s/JtxxFh33r rid: 672068a8-73e07d73-19c9f940"}
        return jsonObj.getString("media_id");
    }


    public File downloadFile(String mediaId) {

        try {
            String token = wxOfficialTokenHelper.acquireAccessToken();
            String replacedUrl = GET_TMP_MATERIAL_MEDIA_URL.replace("ACCESS_TOKEN", token).replace("MEDIA_ID", mediaId);

            URL u = new URL(replacedUrl);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.connect();

            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            String content_disposition = conn.getHeaderField("content-disposition");

            log.info("生成不同文件名称:{}", conn.getHeaderField("content-disposition"));

            if (conn.getHeaderField("error-code") != null) {
                log.info("无法获取临时素材:media_id:{},error-code:{},error-msg:{}", mediaId, conn.getHeaderField("error-code"), conn.getHeaderField("error-msg"));
                throw new UgcCrmServiceException(Integer.parseInt(conn.getHeaderField("error-code")), conn.getHeaderField("error-msg"));
            }

            //微信服务器生成的文件名称
            String file_name = "";
            String[] content_arr = content_disposition.split(";");
            if (content_arr.length == 2) {
                String tmp = content_arr[1];
                int index = tmp.indexOf("\"");
                file_name = tmp.substring(index + 1, tmp.length() - 1);
            }


            //生成不同文件名称
            File file = new File(file_name);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buf = new byte[2048];
            int length = bis.read(buf);
            while (length != -1) {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }

            bos.close();
            bis.close();
            return file;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建企微二维码
     *
     * @param userId    用户uid
     * @param stateCode state
     * @return 二维码信息
     */
    public WeChatQrCodeDTO createQrCode(String userId, String stateCode) {
        String accessToken = wxOfficialTokenHelper.acquireAccessToken();
        WechatCreateQrCodeReq createReq = new WechatCreateQrCodeReq();
        createReq.setType(1);
        createReq.setUser(Collections.singletonList(userId));
        createReq.setState(stateCode);
        WeChatQrCodeDTO resp = wxOfficialConsumer.createEmpQrCode(accessToken, createReq);
        WxOfficialRespCheckUtil.checkResp(resp);
        return resp;
    }

    /**
     * 删除二维码
     *
     * @param configId 配置id
     * @return 返回失败成功
     */
    public boolean deleteQrCode(String configId) {
        String accessToken = wxOfficialTokenHelper.acquireAccessToken();
        WechatDeleteQrCodeReq deleteReq = new WechatDeleteQrCodeReq();
        deleteReq.setConfig_id(configId);
        WeChatBaseResp deleteResp = wxOfficialConsumer.deleteEmpQrCode(accessToken, deleteReq);
        WxOfficialRespCheckUtil.checkResp(deleteResp);
        return deleteResp.isSuccess();
    }

    public ExternalContactQueryResp queryContactDetail(String customerWxId) {
        String accessToken = wxOfficialTokenHelper.acquireAccessToken();
        ExternalContactQueryResp queryResp = wxOfficialConsumer.getExternalContact(accessToken, customerWxId);
        WxOfficialRespCheckUtil.checkResp(queryResp);
        return queryResp;
    }

    public Map<String, String> batchQueryEmpOfficialId(List<String> thirdIdList, String agentId) {
        Map<String, String> retMap = Collections.emptyMap();
        if (CollectionUtils.isEmpty(thirdIdList)) {
            return retMap;
        }
        String accessToken = wxOfficialTokenHelper.acquireAccessToken();
        ThirdEmpIdConvertReq convertReq = new ThirdEmpIdConvertReq();
        convertReq.setSourceAgentId(agentId);
        convertReq.setOpenUserIdList(thirdIdList);
        ThirdEmpIdConvertResp convertResp = wxOfficialConsumer.thirdEmpIdConvert(accessToken, convertReq);
        WxOfficialRespCheckUtil.checkResp(convertResp);
        if (!CollectionUtils.isEmpty(convertResp.getUseridList())) {
            List<ThirdEmpIdConvertResp.UserIdInfo> userIdInfoList = convertResp.getUseridList();
            retMap = userIdInfoList.stream().collect(Collectors.toMap(ThirdEmpIdConvertResp.UserIdInfo::getOpenUserId, ThirdEmpIdConvertResp.UserIdInfo::getUserId, (k1, k2) -> k1));
        }
        return retMap;
    }

    public String queryEmpOfficialId(String empThirdWxId, String agentId) {
        if (StringUtils.isBlank(empThirdWxId)) {
            return StringUtils.EMPTY;
        }
        Map<String, String> idMap = batchQueryEmpOfficialId(Collections.singletonList(empThirdWxId), agentId);
        return idMap.get(empThirdWxId);
    }

    public String queryCustomerOfficialId(String customerThirdWxId, String agentId) {
        if (StringUtils.isBlank(customerThirdWxId)) {
            return StringUtils.EMPTY;
        }
        String accessToken = wxOfficialTokenHelper.acquireAccessToken();
        ThirdCustomerIdConvertReq convertReq = new ThirdCustomerIdConvertReq();
        convertReq.setExternalUserId(customerThirdWxId);
        convertReq.setSourceAgentId(agentId);
        ThirdCustomerIdConvertResp convertResp = wxOfficialConsumer.thirdCustomerConvert(accessToken, convertReq);
        WxOfficialRespCheckUtil.checkResp(convertResp);
        return convertResp.getExternalUserid();
    }

}
