package controllers;

import models.User;
import org.apache.commons.lang.StringUtils;
import play.libs.Crypto;
import play.modules.spring.Spring;
import services.IUserService;
import utils.Constant;

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/20/12
 * Time: 7:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class Security extends Secure.Security{

    static boolean authenticate(String username, String password) {
        IUserService userService=(IUserService) Spring.getBean("userService");
        User u = userService.getUserByLogin(username);
        if(u != null && StringUtils.equals(u.getPassword(), Crypto.passwordHash(password))) {
            return true;
        }else{
            return false;
        }
    }

    /**
     * This method returns the current connected username
     * @return
     */
    static String connected() {
        return session.get(Constant.KEY_SESSION_USER_LOGON);
    }

    /**
     * Indicate if a user is currently connected
     * @return  true if the user is connected
     */
    static boolean isConnected() {
        return session.contains(Constant.KEY_SESSION_USER_LOGON);
    }

    /**
     * This method checks that a profile is allowed to view this page/method. This method is called prior
     * to the method's controller annotated with the @Check method.
     *
     * @param profile
     * @return true if you are allowed to execute this controller method.
     */
    static boolean check(String profile) {
        IUserService userService=(IUserService) Spring.getBean("userService");
        User u = userService.getUserByLogin(connected());
        if ("administrator".equals(profile)) {
            return u.isAdmin();
        }else{
            return false;
        }
    }

}
