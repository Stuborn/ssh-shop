package cn.itcast.shop.interceptor;

import org.apache.struts2.ServletActionContext;

import cn.itcast.shop.adminuser.AdminUser;
import cn.itcast.shop.adminuser.AdminUserAction;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class LoginInterceptor extends MethodFilterInterceptor{

	@Override
	protected String doIntercept(ActionInvocation actionInvocation) throws Exception {
		
		AdminUser existAdminUser = (AdminUser) ServletActionContext
				.getRequest().getSession().getAttribute("existAdminUser");
		if(existAdminUser != null){
			return actionInvocation.invoke();
		}else{
			ActionSupport action = (ActionSupport) actionInvocation.getAction();
			action.addActionError("您还没有登录!");
			return action.LOGIN;
		}
		
	}

}
