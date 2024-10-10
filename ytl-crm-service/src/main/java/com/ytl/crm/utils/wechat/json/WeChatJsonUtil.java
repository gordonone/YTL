package com.ytl.crm.utils.wechat.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.experimental.UtilityClass;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.springframework.util.StringUtils;

import java.util.List;

@UtilityClass
public class WeChatJsonUtil {

    /**
     * xml 转换成 JSONObject
     *
     * @param node 节点
     * @return JSONObject
     */
    public static JSONObject elementToJSONObject(Element node) {
        JSONObject result = new JSONObject();
        // 当前节点的名称、文本内容和属性
        List<Attribute> listAttr = node.attributes();
        // 遍历当前节点的所有属性
        for (Attribute attr : listAttr) {
            result.put(tranFistCharToLow(attr.getName()), attr.getValue());
        }
        // 递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();
        if (!listElement.isEmpty()) {
            for (Element e : listElement) {
                if (e.attributes().isEmpty() && e.elements().isEmpty()) {
                    // 判断一级节点是否有属性和子节点,沒有则将当前节点作为上级节点的属性对待
                    result.put(tranFistCharToLow(e.getName()), e.getTextTrim());
                } else {
                    if (!result.containsKey(tranFistCharToLow(e.getName()))) {
                        // 判断父节点是否存在该一级节点名称的属性,没有则创建
                        result.put(tranFistCharToLow(e.getName()), new JSONArray());
                    }
                    // 将该一级节点放入该节点名称的属性对应的值中
                    ((JSONArray) result.get(tranFistCharToLow(e.getName()))).add(elementToJSONObject(e));
                }
            }
        }
        return result;
    }

    /**
     * 首字母转化成小写
     *
     * @param str 字符串
     * @return 转换之后的字符
     */
    private static String tranFistCharToLow(String str) {
        if (StringUtils.isEmpty(str) || str.length() == 1) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}       
