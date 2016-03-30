package cn.struts.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import cn.struts.config.domain.ActionConfig;

/**
 * 读取配置文件
 * @author zhaozhihang
 *
 */
public class ConfigurationManager {
	
	/**
	 * 读取interceptor
	 * @return
	 */
	public static List<String> getInterceptors() {
		//1创建解析器
		SAXReader reader = new SAXReader();
		
		//2加载配置文件
		InputStream is = ConfigurationManager.class.getResourceAsStream("/struts.xml");
		Document doc = null;
		try {
			doc = reader.read(is);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("配置文件加载失败");
		}
		
		List<String> intorceptors = null;
		
		//3定义xpath查找
		String xPath = "//interceptor";
		List<Element> list = doc.selectNodes(xPath);
		if (list != null && list.size() > 0) {
			intorceptors = new ArrayList<String>();
			for (Element e : list) {
				String className = e.attributeValue("class");
				intorceptors.add(className);
			}
		}

		return intorceptors;
	}
	
	public static String getConstant(String key) {
		
//		String xPath = "//constant";
//		List<Element> list = getDocument().selectNodes(xPath);
//		if (list != null && list.size() > 0) {
//			for (Element e : list) {
//				String name = e.attributeValue("name");
//				if (key == name) {
//					String value = e.attributeValue("value");
//					return value;
//				}
//			}
//		}
		
		//改进
		String xPath = "//constant[@name='"+key+"']";
		Element constant = (Element) getDocument().selectSingleNode(xPath);
		if (constant != null) {
			return constant.attributeValue("value");
		}
		return null;
	}
	
	public static Map<String, ActionConfig> getActions() {
		String xPath = "//action";
		List<Element> list = getDocument().selectNodes(xPath);
		
		Map<String, ActionConfig> actionMap = null;
		if (list != null && list.size() > 0) {
			actionMap = new HashMap<String, ActionConfig>();
			for (Element e : list) {
				ActionConfig config = new ActionConfig();
				String name = e.attributeValue("name");
				config.setName(name);
				config.setClassName(e.attributeValue("class"));
				config.setMethod(e.attributeValue("method"));
				
				List<Element> results = e.elements("result");
				for (Element result : results) {
					config.getResult().put(result.attributeValue("name"), result.getText());
				}
				actionMap.put(name, config);
			}
		}
		return actionMap;
	}
	
	/**
	 * 获取doc对象
	 * @return
	 */
	private static Document getDocument() {
		//1创建解析器
		SAXReader reader = new SAXReader();
		
		//2加载配置文件
		InputStream is = ConfigurationManager.class.getResourceAsStream("/struts.xml");
		Document doc = null;
		try {
			doc = reader.read(is);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("配置文件加载失败");
		}
		return doc;
	}
}
