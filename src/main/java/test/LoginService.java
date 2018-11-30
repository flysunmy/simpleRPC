package test;

import annotation.RPCService;

/**
 * Created by wuzichao on 2018/11/30.
 */
@RPCService(LoginService.class)
public class LoginService {

    public String login (){
        return "logged in";
    }
}
