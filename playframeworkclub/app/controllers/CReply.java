package controllers;

import models.Reply;
import models.Topic;
import models.User;
import play.cache.Cache;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.modules.spring.Spring;
import play.mvc.Controller;
import services.IReplyService;
import services.ITopicService;
import services.IUserService;
import utils.Constant;

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/30/12
 * Time: 9:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class CReply extends Controller {


    public static void save(@Valid Reply reply)
    {
        if(Validation.hasErrors())
        {
            params.flash(); // add http parameters to the flash scope
            validation.keep(); // keep the errors for the next request
            CTopic.index(reply.getTopicId());
        }

        IReplyService replyService=(IReplyService) Spring.getBean("replyService");
        Reply r=replyService.insert(reply);

        Topic topic=new Topic();
        topic.setId(reply.getTopicId());
        topic.setLastReply(r.getUserId());
        ITopicService topicService= (ITopicService) Spring.getBean("topicService");
        topicService.updateTopicLastReply(topic);

        User currentUser =(User) Cache.get(session.get(Constant.KEY_SESSION_USER_LOGON));

        IUserService userService=(IUserService) Spring.getBean("userService");
        userService.addReplyCount(currentUser.getId());

        CTopic.index(reply.getTopicId());

    }

    public static void saveReply(@Valid Reply rReply)
    {
        if(Validation.hasErrors())
        {
            params.flash(); // add http parameters to the flash scope
            validation.keep(); // keep the errors for the next request
            CTopic.index(rReply.getTopicId());
        }

        IReplyService replyService=(IReplyService) Spring.getBean("replyService");
        Reply r=replyService.insert(rReply);

        Topic topic=new Topic();
        topic.setId(rReply.getTopicId());
        topic.setLastReply(r.getUserId());
        ITopicService topicService= (ITopicService) Spring.getBean("topicService");
        topicService.updateTopicLastReply(topic);

        User currentUser =(User) Cache.get(session.get(Constant.KEY_SESSION_USER_LOGON));

        IUserService userService=(IUserService) Spring.getBean("userService");
        userService.addReplyCount(currentUser.getId());

        CTopic.index(rReply.getTopicId());

    }
}
