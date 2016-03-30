package cn.struts.interceptor;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import cn.struts.context.ActionContext;
import cn.struts.invocation.ActionInvocation;
import cn.struts.stack.ValueStack;

public class ParamInterceptor implements Interceptor {

	@Override
	public void init() {
		
	}

	@Override
	public String interceptor(ActionInvocation invocation) {
//		ActionContext.getActionContext().getStack();
		//方法二
		ActionContext ac = invocation.getActionContext();
		ValueStack stack = ac.getStack();
		Object action = stack.seek();
		
		try {
			BeanUtils.populate(action, ac.getRequest().getParameterMap());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		//放行
		return invocation.invoke(invocation);
	}

	@Override
	public void destroy() {
	}

}
