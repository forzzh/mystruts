package cn.struts.interceptor;

import cn.struts.invocation.ActionInvocation;

public interface Interceptor {

	void init();
	
	String interceptor(ActionInvocation invocation);
	
	void destroy();
}
