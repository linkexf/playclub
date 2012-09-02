package controllers;

import org.apache.commons.lang.StringUtils;
import play.Play;
import play.cache.Cache;
import play.data.validation.Email;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.libs.Crypto;
import play.modules.spring.Spring;
import play.mvc.*;

import models.*;
import services.ITagService;
import services.ITopicService;
import services.IUserService;
import utils.Constant;
import notifiers.Mails;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.codec.digest.DigestUtils.md5;

public class Application extends Controller {

    @Before
    static void getCurrentUser(){
        if(session.contains(Constant.KEY_SESSION_USER_LOGON)){
            renderArgs.put(Constant.CURRENT_USER, session.get(Constant.KEY_SESSION_USER_LOGON));
            IUserService userService=(IUserService) Spring.getBean("userService");
            User currentUser = userService.getUserByLogin(session.get(Constant.KEY_SESSION_USER_LOGON));
            Cache.set(session.get(Constant.KEY_SESSION_USER_LOGON),currentUser);
        }
    }


    public static void index() {
        int current_page=params.get("page")==null?1:Integer.parseInt(params.get("page"));
        User currentUser =(User) Cache.get(session.get(Constant.KEY_SESSION_USER_LOGON));

        ITagService tagService=(ITagService) Spring.getBean("tagService");
        List<Tag> tags=tagService.list();

        ITopicService topicService= (ITopicService) Spring.getBean("topicService");
        int rows=Integer.parseInt(Play.configuration.get("application.page").toString());
        int offset=(current_page-1)*rows;
        List<Topic> topicList=topicService.list(offset,rows);
        int total=topicService.count();

        List<Topic> topics=new ArrayList<Topic>();
        for (Topic topic:topicList)
        {
            if(topic.getLastReply()>0)
            {
                IUserService userService=(IUserService) Spring.getBean("userService");
                User reply=userService.getUserById(topic.getLastReply());
                topic.setReply(reply);
            }

            List<Tag> tagList=tagService.listByTopic(topic.getId());
            topic.setTagList(tagList);
            topics.add(topic);
        }

        render(currentUser,tags,topics,current_page,total);
    }

    public static void tag(String name)
    {
        int current_page=params.get("page")==null?1:Integer.parseInt(params.get("page"));
        ITagService tagService=(ITagService) Spring.getBean("tagService");
        Tag tag=tagService.get(name);

        ITopicService topicService= (ITopicService) Spring.getBean("topicService");
        int rows=Integer.parseInt(Play.configuration.get("application.page").toString());
        int offset=(current_page-1)*rows;
        List<Topic> topicList=topicService.listByTag(tag.getId(),offset,rows);
        int total=topicService.countByTag(tag.getId());

        List<Topic> topics=new ArrayList<Topic>();
        for (Topic topic:topicList)
        {
            if(topic.getLastReply()>0)
            {
                IUserService userService=(IUserService) Spring.getBean("userService");
                User reply=userService.getUserById(topic.getLastReply());
                topic.setReply(reply);
            }

            List<Tag> tagList=tagService.listByTopic(topic.getId());
            topic.setTagList(tagList);
            topics.add(topic);
        }
        if(session.get(Constant.KEY_SESSION_USER_LOGON)!=null)
        {
            User currentUser =(User) Cache.get(session.get(Constant.KEY_SESSION_USER_LOGON));
            TagCollect tagCollect=new TagCollect();
            tagCollect.setUserId(currentUser.getId());
            tagCollect.setTagId(tag.getId());
            boolean isCollect=tagService.isCollect(tagCollect);
            render(tag,currentUser,isCollect,topics,current_page,total);
        }else{
            render(tag,topics,current_page,total);
        }
    }

    public static void register()
    {
        if(session.contains(Constant.KEY_SESSION_USER_LOGON)){
            index();
        }
        render();
    }

    public static void login()
    {
        if(session.contains(Constant.KEY_SESSION_USER_LOGON)){
            index();
        }
        render();
    }

    public static void forgetPass()
    {
        render();
    }

    public static void restPass(@Required String a)
    {
        String email= Crypto.decryptAES(a).split("&")[0];
        render(email, a);
    }

    public static void notice()
    {
        render();
    }

    public static void about()
    {
        User currentUser =(User) Cache.get(session.get(Constant.KEY_SESSION_USER_LOGON));
        render(currentUser);
    }

    public static void join(@Valid User user)
    {
        validation.equals("user.confirmPassword",user.getConfirmPassword(),"user.password",user.getPassword()).message("两次密码不一致！");
        if(Validation.hasErrors())
        {
            params.flash(); // add http parameters to the flash scope
            validation.keep(); // keep the errors for the next request
            register();
        }
        String imageUrl="http://www.gravatar.com/avatar/" + md5(user.getEmail()) + "?size=48";
        user.setImageUrl(imageUrl);
        user.setPassword(Crypto.passwordHash(user.getPassword()));
        IUserService userService=(IUserService)Spring.getBean("userService");
        userService.save(user);
        Mails.welcome(user);
        index();
    }

    public static void auth(@Required String loginName,@Required String password) {
        if(Validation.hasErrors())
        {
            params.flash(); // add http parameters to the flash scope
            validation.keep(); // keep the errors for the next request
            login();
        }

        IUserService userService=(IUserService)Spring.getBean("userService");
        User u = userService.getUserByLogin(loginName);
        if(u != null && StringUtils.equals(u.getPassword(), Crypto.passwordHash(password))) {
            if(u.isActive())
            {
                session.put(Constant.KEY_SESSION_USER_LOGON,u.getLoginName());
                Cache.add(u.getLoginName(),u);
                Application.index();
            }else{
                flash.error("该用户未激活！");
                params.flash(); // add http parameters to the flash scope
                login();
            }
        }
        flash.error("用户名或密码错误");
        params.flash(); // add http parameters to the flash scope
        login();
    }

    public static void logout()
    {
        Cache.delete(session.get(Constant.KEY_SESSION_USER_LOGON));
        session.remove(Constant.KEY_SESSION_USER_LOGON);
        session.clear();
        index();
    }

    public static void searchPass(@Required @Email String email)
    {
        if(Validation.hasErrors())
        {
            params.flash(); // add http parameters to the flash scope
            validation.keep(); // keep the errors for the next request
            forgetPass();
        }
        IUserService userService=(IUserService)Spring.getBean("userService");
        User u = userService.getUserByEmail(email);
        if(u!=null)
        {
            Mails.lostPassword(u);
            flash.success("密码获取成功，请进入你的电子邮箱获取密码！");
        }else{
            flash.error("该用户不存在！");
        }
        params.flash(); // add http parameters to the flash scope
        forgetPass();
    }

    public static void active(@Required String a)
    {
        String email= Crypto.decryptAES(a);
        IUserService userService=(IUserService)Spring.getBean("userService");
        User user=userService.getUserByEmail(email);
        if(user.isActive())
        {
            flash.error("该用户已处于激活状态，请登录！");
        }else{
            user.setActive(true);
            if(userService.updateActive(user))
            {
                flash.success("用户激活成功，请登录！");
            }else{
                flash.error("系统错误，请稍候再试！");
            }
        }
        params.flash(); // add http parameters to the flash scope
        notice();
    }

    public static void updatePass(@Required String a,@Required String email,@Required String password,@Required String confirmPassword)
    {
        validation.equals(password,confirmPassword);
        if(Validation.hasErrors())
        {
            System.out.println(a);
            params.flash(); // add http parameters to the flash scope
            validation.keep(); // keep the errors for the next request
            restPass(a);
        }
        IUserService userService=(IUserService)Spring.getBean("userService");
        User user=userService.getUserByEmail(email);
        user.setPassword(Crypto.passwordHash(password));
        if(userService.updatePassword(user))
        {
            flash.success("密码重置成功，请重新登录！");
        }else{
            flash.error("系统错误，请稍候再试！");
        }
        notice();
    }

}