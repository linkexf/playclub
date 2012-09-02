package controllers;

import com.google.gson.Gson;
import models.Tag;
import models.TagCollect;
import models.User;
import play.cache.Cache;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.modules.spring.Spring;
import play.mvc.Controller;
import play.mvc.With;
import services.ITagService;
import services.IUserService;
import utils.Constant;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/26/12
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
@With(Authenticate.class)
public class CTag extends Controller {

    public static void create()
    {
        ITagService tagService=(ITagService) Spring.getBean("tagService");
        List<Tag> tags=tagService.list();
        render(tags);
    }

    public static void edit(String tagName)
    {
        ITagService tagService=(ITagService) Spring.getBean("tagService");
        Tag tag=tagService.get(tagName);
        renderJSON(tag);
    }



    public static void save(@Valid Tag tag)
    {
        if(Validation.hasErrors())
        {
            params.flash(); // add http parameters to the flash scope
            validation.keep(); // keep the errors for the next request
        }else{

            ITagService tagService=(ITagService) Spring.getBean("tagService");
            if(tag.getId()>0)
            {
                tagService.update(tag);
            }else{
                tagService.add(tag);
            }
            flash.success("保存成功！");
        }
        create();
    }

    public static void delete(String name)
    {
        ITagService tagService=(ITagService) Spring.getBean("tagService");
        tagService.delete(name);
        create();
    }

    public static void collect(long tagId)
    {
        User currentUser =(User) Cache.get(session.get(Constant.KEY_SESSION_USER_LOGON));
        ITagService tagService=(ITagService) Spring.getBean("tagService");
        TagCollect tagCollect=new TagCollect();
        tagCollect.setUserId(currentUser.getId());
        tagCollect.setTagId(tagId);
        tagService.addTagCollect(tagCollect);

        IUserService userService=(IUserService) Spring.getBean("userService");
        if(userService.addTagCollectCount(currentUser.getId()))
        {
            renderJSON("{\"status\": \"success\"}");
        }else{
            renderJSON("{\"status\": \"fail\"}");
        }
    }

    public static void cancelCollect(long tagId)
    {
        User currentUser =(User) Cache.get(session.get(Constant.KEY_SESSION_USER_LOGON));
        System.out.println(currentUser.getId()+currentUser.getName());
        ITagService tagService=(ITagService) Spring.getBean("tagService");
        TagCollect tagCollect=new TagCollect();
        tagCollect.setUserId(currentUser.getId());
        tagCollect.setTagId(tagId);
        tagService.deleteTagCollect(tagCollect);
        IUserService userService=(IUserService) Spring.getBean("userService");
        if(userService.subtractTagCollectCount(currentUser.getId()))
        {
            renderJSON("{\"status\": \"success\"}");
        }else{
            renderJSON("{\"status\": \"fail\"}");
        }
    }
}
