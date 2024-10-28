package com.ytl.crm.api.wechat;

import com.alibaba.fastjson.JSONObject;
import com.ytl.crm.consumer.wechat.CommonUtil;
import com.ytl.crm.consumer.wechat.WxOfficialTokenHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import static com.ytl.crm.consumer.wechat.CommonUtil.getMediaIdFromUrl;


@RestController("fileController")
@RequestMapping("/enterprise")
@Slf4j
public class FileController {


    private static final String UPLOAD_FOREVER_MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

    private static final String GET_TMP_MATERIAL_MEDIA_URL = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";

    private static final long MAX_SIZE = 1024 * 1024 * 2;

    private static final String SUPPORTED_FORMAT = "bmp,png,jpeg,jpg,gif";

    @Autowired
    private WxOfficialTokenHelper wxOfficialTokenHelper;


    /**
     * 上传素材到微信素材库
     *
     * @param file
     */
    @RequestMapping(value = "uploadMediaToWechat", method = RequestMethod.POST)
    @ResponseBody
    public String uploadMediaToWechat(@RequestParam("media") MultipartFile file) throws Exception {

        getMediaIdFromUrl(file, "image");
        return "素材图片上传失败";
    }


    /**
     * 图片是否符合微信规范
     *
     * @param size
     * @param suffixName
     * @return
     */
    public Boolean isValidPic(long size, String suffixName) {

        if (size > MAX_SIZE) {
            log.info("文件太大，文件的大小最大为2M，请重新上传!");
            return false;
        }

        if (!Arrays.asList(SUPPORTED_FORMAT.split(",")).contains(suffixName)) {
            log.info("图片格式不支持，请选择bmp/png/jpeg/jpg/gif的任意一种!");
            return false;
        }
        return true;

    }

//    /**
//     * 网络图片上传到微信服务器
//     *
//     * @param urlPath 图片路径
//     * @return JSONObject
//     * @throws Exception
//     */
//    public String getMediaIdFromUrl(String urlPath, String fileType) throws Exception {
//        String result = null;
//        String url = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token= Access_token &type=" + fileType + "";
//        String fileName = urlPath.substring(urlPath.lastIndexOf("/") + 1);
//        // 获取网络图片
//        URL mediaUrl = new URL(urlPath);
//        HttpURLConnection meidaConn = (HttpURLConnection) mediaUrl.openConnection();
//        meidaConn.setDoOutput(true);
//        meidaConn.setRequestMethod("GET");
//
//        /**
//         * 第一部分
//         */
//        URL urlObj = new URL(url);
//        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
//        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
//        con.setDoInput(true);
//        con.setDoOutput(true);
//        con.setUseCaches(false); // post方式不能使用缓存
//        // 设置请求头信息
//        con.setRequestProperty("Connection", "Keep-Alive");
//        con.setRequestProperty("Charset", "UTF-8");
//        // 设置边界
//        String BOUNDARY = "----------" + System.currentTimeMillis();
//        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
//        // 请求正文信息
//        // 第一部分：
//        StringBuilder sb = new StringBuilder();
//        sb.append("--"); // 必须多两道线
//        sb.append(BOUNDARY);
//        sb.append("\r\n");
//        sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + fileName + "\"\r\n");
//        sb.append("Content-Type:application/octet-stream\r\n\r\n");
//        byte[] head = sb.toString().getBytes("utf-8");
//        // 获得输出流
//        OutputStream out = new DataOutputStream(con.getOutputStream());
//        // 输出表头
//        out.write(head);
//        // 文件正文部分
//        // 把文件已流文件的方式 推入到url中
//        DataInputStream in = new DataInputStream(meidaConn.getInputStream());
//        int bytes = 0;
//        byte[] bufferOut = new byte[1024];
//        while ((bytes = in.read(bufferOut)) != -1) {
//            out.write(bufferOut, 0, bytes);
//        }
//        in.close();
//        // 结尾部分
//        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
//        out.write(foot);
//        out.flush();
//        out.close();
//        meidaConn.disconnect();
//        StringBuffer buffer = new StringBuffer();
//        BufferedReader reader = null;
//        try {
//            // 定义BufferedReader输入流来读取URL的响应
//            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                buffer.append(line);
//            }
//            if (result == null) {
//                result = buffer.toString();
//            }
//        } catch (IOException e) {
//            log.info("发送POST请求出现异常！ {}", e);
//            e.printStackTrace();
//            throw new IOException("数据读取异常");
//        } finally {
//            if (reader != null) {
//                reader.close();
//            }
//        }
//        JSONObject jsonObj = JSON.parseObject(result);
//        log.info("getMediaId jsonObj: {}", jsonObj);
//        return jsonObj.getString("media_id");
//    }

    @RequestMapping(value = "getMediaToWechat", method = RequestMethod.GET)
    @ResponseBody
    public File imageFetch(@RequestParam("mediaId") String mediaId) {
        String token = wxOfficialTokenHelper.acquireAccessToken();
        try {
            String replacedUrl = GET_TMP_MATERIAL_MEDIA_URL.replace("ACCESS_TOKEN", token).replace("MEDIA_ID", mediaId);
            URL u = new URL(replacedUrl);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            String content_disposition = conn.getHeaderField("content-disposition");
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


}
