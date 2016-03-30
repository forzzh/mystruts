package cn.struts.invocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.struts.config.domain.ActionConfig;
import cn.struts.context.ActionContext;
import cn.struts.interceptor.Interceptor;

public class ActionInvocation {
	//过滤器链
	private Iterator<Interceptor> interceptors;
	//即将调用的action实例
	private Object action;
	//action配置信息
	private ActionConfig config;
	private ActionContext ac;
	
	public ActionInvocation(List<String> interceptorsClassName, ActionConfig config, HttpServletRequest request, HttpServletResponse response) {
		if (interceptorsClassName != null && interceptorsClassName.size() > 0) {
			List<Interceptor> interceptorList = new ArrayList<Interceptor>();
			for (String className : interceptorsClassName) {
				Interceptor interceptor;
				try {
					interceptor = (Interceptor) Class.forName(className).newInstance();
					interceptor.init();
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("创建interceptor失败" + className);
				}
				interceptorList.add(interceptor);
			}
			this.interceptors = interceptorList.iterator();
		}
		
		
		this.config = config;
		
		try {
			action = Class.forName(config.getClassName()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("创建Action失败" + config.getClassName());
		} 
		
		//准备数据中心
		ac = new ActionContext(request, response, action);
	}
	
	public ActionContext getActionContext() {
		return ac;
	}
	
	String result = null;
	public String invoke(ActionInvocation invocation) {
		//为了递归调用，判断是否有下一个所有用Iterator
		if (interceptors != null && interceptors.hasNext() && result == null) {
			//有就调用下一个
			Interceptor interceptor = interceptors.next();
			result = interceptor.interceptor(invocation);
		} else {
			//没有
			String methodName = config.getMethod();
			
			try {
				Method method = action.getClass().getMethod(methodName);
				result = (String) method.invoke(action);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("方法调用失败" + methodName);
			} 
		}
		return result;
	}
}
