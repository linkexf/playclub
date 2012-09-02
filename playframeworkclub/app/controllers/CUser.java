package controllers;

import models.Relation;
import models.User;
import org.apache.commons.lang.StringUtils;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.libs.Crypto;
import play.modules.spring.Spring;
import play.mvc.Controller;
import play.mvc.With;
import services.IRelationService;
import services.IUserService;
import utils.Constant;

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/21/12
 * Time: 8:30 PM
 * To change this template use File | Settings | File Templates.
 */
@With(Authenticate.class)
public class CUser extends Controller {


    public static void index(String loginName)
    {
        IUserService userService=(IUserService) Spring.getBean("userService");
        User user = userService.getUserByLogin(loginName);

        boolean isFollow=false;
        User currentUser =(User) Cache.get(session.get(Constant.KEY_SESSION_USER_LOGON));
        if(currentUser!=null && !loginName.equals(session.get(Constant.KEY_SESSION_USER_LOGON)))
        {

            IRelationService relationService=(IRelationService)Spring.getBean("relationService");
            isFollow=relationService.isFollow(currentUser.getId(),user.getId());
        }

        render(user,currentUser,isFollow);
    }


    public static void setStar(long id)
    {
        IUserService userService=(IUserService) Spring.getBean("userService");
        if(userService.updateStar(id,true))
        {
            renderJSON("{\"status\": \"success\"}");
        }else{
            renderJSON("{\"status\": \"fail\"}");
        }
    }

    public static void cancelStar(long id)
    {
        IUserService userService=(IUserService) Spring.getBean("userService");
        if(userService.updateStar(id,false))
        {
            renderJSON("{\"status\": \"success\"}");
        }else{
            renderJSON("{\"status\": \"fail\"}");
        }
    }

    public static void follow(long id)
    {
        IUserService userService=(IUserService) Spring.getBean("userService");
        User user = userService.getUserByLogin(session.get(Constant.KEY_SESSION_USER_LOGON));
        Relation relation=new Relation();
        relation.setUserId(user.getId());
        relation.setFollowId(id);
        IRelationService relationService=(IRelationService)Spring.getBean("relationService");
        relationService.add(relation);
        if(userService.updateFollowCount(user.getId(),id))
        {
            renderJSON("{\"status\": \"success\"}");
        }else{
            renderJSON("{\"status\": \"fail\"}");
        }

    }

    public static void cancelFollow(long id)
    {
        IUserService userService=(IUserService) Spring.getBean("userService");
        User user = userService.getUserByLogin(session.get(Constant.KEY_SESSION_USER_LOGON));
        Relation relation=new Relation();
        relation.setUserId(user.getId());
        relation.setFollowId(id);
        IRelationService relationService=(IRelationService)Spring.getBean("relationService");
        relationService.delete(relation);
        if(userService.subtractFollowCount(user.getId(),id))
        {
            renderJSON("{\"status\": \"success\"}");
        }else{
            renderJSON("{\"status\": \"fail\"}");
        }
    }

    public static void profile()
    {
        IUserService userService=(IUserService) Spring.getBean("userService");
        User user = userService.getUserByLogin(session.get(Constant.KEY_SESSION_USER_LOGON));
        render(user);
    }

    public static void restPass(@Required String oldPassword,@Required String newPassword)
    {


        if(Validation.hasErrors())
        {
            params.flash(); // add http parameters to the flash scope
            validation.keep(); // keep the errors for the next request
            profile();
        }
        if(oldPassword.equals(newPassword))
        {
            flash.error("新密码和原密码不能重复！");
            params.flash(); // add http parameters to the flash scope
            profile();
        }

        IUserService userService=(IUserService) Spring.getBean("userService");
        User u = userService.getUserByLogin(session.get(Constant.KEY_SESSION_USER_LOGON));
        if(u!=null&&StringUtils.equals(u.getPassword(), Crypto.passwordHash(oldPassword)))
        {
            u.setPassword(Crypto.passwordHash(newPassword));
            userService.updatePassword(u);
            flash.success("密码修改成功！");
            profile();
        }
        flash.error("原密码错误，请重新输入！");
        params.flash(); // add http parameters to the flash scope
        profile();

    }

    public static void update(User user,String receiveMail)
    {
        System.out.println(user.getName());
        user.setReceiveMail(receiveMail==null?false:true);
        IUserService userService=(IUserService) Spring.getBean("userService");
        if(userService.update(user))
        {
            flash.success("修改成功！");
            profile();
        }
        flash.error("修改失败！");
        profile();
    }

}
