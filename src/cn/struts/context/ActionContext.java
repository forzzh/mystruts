package cn.struts.context;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.struts.stack.ValueStack;

public class ActionContext {

	private Map<String, Object> context;
	
	public static ThreadLocal<ActionContext> tl = new ThreadLocal<ActionContext>();
	
	public ActionContext(HttpServletRequest request, HttpServletResponse response, Object action) {
		context = new HashMap<String, Object>();
		context.put(Constant.REQUEST, request);
		context.put(Constant.RESPONSE, response);
		context.put(Constant.PARAM, request.getParameterMap());
		context.put(Constant.SESSION, request.getSession());
		context.put(Constant.APPLICATION, request.getSession().getServletContext());
		
		//valuestack
		ValueStack vs = new ValueStack();
		//需要把action放入栈顶
		vs.push(action);
		
		request.setAttribute(Constant.VALUE_STATIC, vs);
		
		context.put(Constant.VALUE_STATIC, vs);
		
		tl.set(this);
	}
	
	public HttpServletRequest getRequest() {
		return (HttpServletRequest) context.get(Constant.REQUEST);
	}
	
	public HttpServletResponse getResponse() {
		return (HttpServletResponse) context.get(Constant.RESPONSE);
	}
	
	public HttpSession getSession() {
		return (HttpSession) context.get(Constant.SESSION);
	}
	
	public ServletContext getApplication() {
		return (ServletContext) context.get(Constant.APPLICATION);
	}
	
	public Map<String, String[]> getParam() {
		return (Map<String, String[]>) context.get(Constant.PARAM);
	}
	
	public ValueStack getStack() {
		return (ValueStack) context.get(Constant.VALUE_STATIC);
	}
	
	public static ActionContext getActionContext() {
		return ActionContext.tl.get();
	}
}
