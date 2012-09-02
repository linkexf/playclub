package controllers;

import models.*;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.modules.spring.Spring;
import play.mvc.Controller;
import play.mvc.With;
import services.*;
import utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/27/12
 * Time: 9:16 PM
 * To change this template use File | Settings | File Templates.
 */
@With(Authenticate.class)
public class CTopic extends Controller {

    public static void create()
    {
        ITagService tagService=(ITagService) Spring.getBean("tagService");
        List<Tag> tags=tagService.list();
        User user =(User) Cache.get(session.get(Constant.KEY_SESSION_USER_LOGON));
        render(tags,user);
    }

    public static void save(@Valid Topic topic,String topic_tags)
    {
        if(Validation.hasErrors())
        {
            params.flash(); // add http parameters to the flash scope
            validation.keep(); // keep the errors for the next request
            create();
        }

        ITopicService topicService= (ITopicService) Spring.getBean("topicService");
        topic=topicService.insert(topic);

        if(topic_tags!=null&&!"".equals(topic_tags)&&topic_tags.length()>0)
        {
            for(String tagId:topic_tags.split(","))
            {
                TopicTag topicTag=new TopicTag();
                topicTag.setTagId(Long.parseLong(tagId));
                topicTag.setTopicId(topic.getId());
                topicService.addTopicTag(topicTag);
            }
        }



        IUserService userService=(IUserService) Spring.getBean("userService");
        userService.updateTopicCount(topic.getUserId());

        redirect("/");
    }

    public static void index(long topicId)
    {
        ITopicService topicService= (ITopicService) Spring.getBean("topicService");
        Topic topic=topicService.get(topicId);
        User user=topic.getUser();

        topicService.addVisitCount(topic);

        TopicCollect topicCollect=new TopicCollect();
        topicCollect.setUserId(user.getId());
        topicCollect.setTopicId(topic.getId());
        boolean isCollect=topicService.isCollect(topicCollect);

        //User currentUser =(User) Cache.get(session.get(Constant.KEY_SESSION_USER_LOGON));

        IReplyService replyService=(IReplyService) Spring.getBean("replyService");
        List<Reply> replyList=replyService.list(topicId);

        List<Reply> replies=new ArrayList<Reply>();
        for (Reply reply:replyList)
        {
            List<Reply> replyList1=replyService.listByReplyId(reply.getId());
            reply.setChildReplies(replyList1);
            replies.add(reply);
        }

        boolean isFollow=false;
        User currentUser =(User) Cache.get(session.get(Constant.KEY_SESSION_USER_LOGON));
        if(currentUser!=null)
        {
            IRelationService relationService=(IRelationService)Spring.getBean("relationService");
            isFollow=relationService.isFollow(currentUser.getId(),user.getId());
        }

        ITagService tagService=(ITagService) Spring.getBean("tagService");
        List<Tag> tagList=tagService.listByTopic(topic.getId());
        topic.setTagList(tagList);


        render(topic,user,isCollect,isFollow,replies);
    }

    public static void collect(long topicId)
    {
        System.out.println(topicId);
        User currentUser =(User) Cache.get(session.get(Constant.KEY_SESSION_USER_LOGON));
        TopicCollect topicCollect=new TopicCollect();
        topicCollect.setTopicId(topicId);
        topicCollect.setUserId(currentUser.getId());

        ITopicService topicService= (ITopicService) Spring.getBean("topicService");
        topicService.insertTopicCollect(topicCollect);

        IUserService userService=(IUserService) Spring.getBean("userService");
        if(userService.addTopicCount(currentUser.getId()))
        {
            renderJSON("{\"status\": \"success\"}");
        }else{
            renderJSON("{\"status\": \"fail\"}");
        }

    }

    public static void cancelCollect(long topicId)
    {
        User currentUser =(User) Cache.get(session.get(Constant.KEY_SESSION_USER_LOGON));
        TopicCollect topicCollect=new TopicCollect();
        topicCollect.setTopicId(topicId);
        topicCollect.setUserId(currentUser.getId());

        ITopicService topicService= (ITopicService) Spring.getBean("topicService");
        topicService.deleteTopicCollect(topicCollect);

        IUserService userService=(IUserService) Spring.getBean("userService");
        if(userService.subtractTopicCount(currentUser.getId()))
        {
            renderJSON("{\"status\": \"success\"}");
        }else{
            renderJSON("{\"status\": \"fail\"}");
        }
    }

}
