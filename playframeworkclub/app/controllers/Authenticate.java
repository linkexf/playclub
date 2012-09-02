package controllers;

import models.User;
import play.Play;
import play.cache.Cache;
import play.modules.spring.Spring;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Router;
import play.mvc.With;
import services.IUserService;
import utils.Constant;

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/21/12
 * Time: 8:43 PM
 * To change this template use File | Settings | File Templates.
 */
@With(Application.class)
public class Authenticate extends Controller {

    @Before(unless = {"CUser.index", "CTag.collect", "CTag.cancelCollect","CTopic.index"})
    static void auth()
    {
        if(!session.contains(Constant.KEY_SESSION_USER_LOGON)) {
            flash.put("url", "GET".equals(request.method) ? request.url : Play.ctxPath + "/"); // seems a good default
            redirect("/login");
        }
    }


    static boolean check()
    {
        IUserService userService=(IUserService) Spring.getBean("userService");
        if(session.get(Constant.KEY_SESSION_USER_LOGON)!=null)
        {
            User user = userService.getUserByLogin(session.get(Constant.KEY_SESSION_USER_LOGON));

            return user.isAdmin();
        }
        return false;
    }



}
