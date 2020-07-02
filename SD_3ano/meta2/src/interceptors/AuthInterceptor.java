package interceptors;


import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.Interceptor;
import model.User;

import java.util.Map;

public class AuthInterceptor implements Interceptor {


    @Override
    public void destroy() {

    }

    @Override
    public void init() {

    }

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        Map<String, Object> session = actionInvocation.getInvocationContext().getSession();
        User user= (User) session.get("user");
        if(user==null){
            return ActionSupport.LOGIN;
        }
        return actionInvocation.invoke();
    }
}
