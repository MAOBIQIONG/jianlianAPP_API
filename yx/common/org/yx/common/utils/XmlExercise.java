package org.yx.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XmlExercise {

	//存储xml元素信息的容器
	public static Map<String, Object> map = new HashMap<String, Object>();
	public static List<String> list=new ArrayList<String>();
    public static void main(String args[]) throws DocumentException {
    	XmlExercise test = new XmlExercise();
    	String json="{\"gj\":\"china\",\"area\":[{\"name\":\"aaaa\",\"ea\":{\"fff\":\"123\"}},{\"name\":\"aaaa\"}]}";
    	String xml=jsontoXml(json);
        Element root = test.getRootElement(xml);
        List<String> elemList= test.getElementList(root);
        System.out.println("-----------原xml内容------------");
        System.out.println(xml);
        System.out.println("-----------解析结果------------");
        
        List<String> keyList=new ArrayList<String>();
        for (String string : list) {
			keyList.add(string.substring(0, string.lastIndexOf("=")));
		}
        List a=new ArrayList();
        for (String string : list) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put(string.substring(0, string.lastIndexOf("=")), string.substring(string.lastIndexOf("=")+1));
			a.add(map);
		}
        System.err.println(a);
        
        
    }
    /**
     * 获取根元素
     *
     * @return
     * @throws DocumentException
     */
    public static Element getRootElement(String xml) throws DocumentException {
        Document srcdoc = DocumentHelper.parseText(xml);
        Element elem = srcdoc.getRootElement();
        return elem;
    }
    /**
     * 递归遍历方法
     *
     * @param element
     */
    public static List<String> getElementList(Element element) {
        List elements = element.elements();
        if (elements.size() == 0) {
            //没有子元素
            String xpath = element.getPath();
            String value = element.getTextTrim();
           // map.put(xpath, value);
            String b=xpath+"="+value;
            list.add(b);
        } else {
            //有子元素
            for (Iterator it = elements.iterator(); it.hasNext();) {
                Element elem = (Element) it.next();
                //递归遍历
                getElementList(elem);
            }
        }
        return list;
    }
//    public String getListString(List<String> elemList) {
//        StringBuffer sb = new StringBuffer();
//        for (Iterator<String> it = elemList.iterator(); it.hasNext();) {
//            String str = it.next();
//            sb.append(str+"\n");
//        }
//        return sb.toString();
//    }
    
    
    /**
     * 
     * <b>方法名：</b>：jsontoXml<br>
     * <b>功能说明：</b>：json转xml<br>
     * @author <font color='blue'>陈鹏</font> 
     * @date  2017年2月16日 下午4:25:22
     * @param json
     * @return
     */
    public static String jsontoXml(String json) {  
        try {  
            XMLSerializer serializer = new XMLSerializer();  
            JSON jsonObject = JSONSerializer.toJSON(json);  
            return serializer.write(jsonObject);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
    
    
}
