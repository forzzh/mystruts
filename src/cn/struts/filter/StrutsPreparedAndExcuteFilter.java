package cn.struts.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.struts.config.ConfigurationManager;
import cn.struts.config.domain.ActionConfig;
import cn.struts.context.ActionContext;
import cn.struts.invocation.ActionInvocation;

/*
 * 需要初始化配置文件
 * 分析访问的action，加载action配置
 * 根据分析结果调用ActionInvocation类
 * 
 */
public class StrutsPreparedAndExcuteFilter implements Filter {

	private List<String> interceptorList;
	private String extension;
	private Map<String, ActionConfig> actionConfigs;

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		//获得请求路径
		String path = req.getServletPath(); //    /xxx.action
		//判断是否需要访问action
		if (!path.endsWith(extension)) {
			chain.doFilter(request, response);
			return;
		} 
		
		path = path.substring(1);	//	去掉斜杠
		path = path.replace("." + extension, "");	//	去掉.action
		
		ActionConfig config = actionConfigs.get(path);
		if (config == null) {
			throw new RuntimeException("访问的action不存在");
		}
		
		//创建ActionInvocation完成拦截器链以及action方法
		ActionInvocation invocation = new ActionInvocation(interceptorList, config, req, resp);	
		String result = invocation.invoke(invocation);
		
		//转发到目的位子
		String dispatcherPath = config.getResult().get(result);
		if (dispatcherPath == null || "".equals(dispatcherPath)) {
			throw new RuntimeException("你要访问的结果找不到配置");
		}
		req.getRequestDispatcher(dispatcherPath).forward(req, resp);
		//释放资源
		ActionContext.tl.remove();
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		//读取配置文件信息
		interceptorList = ConfigurationManager.getInterceptors();
		extension = ConfigurationManager.getConstant("struts.action.extension");
		actionConfigs = ConfigurationManager.getActions();
	}

}
