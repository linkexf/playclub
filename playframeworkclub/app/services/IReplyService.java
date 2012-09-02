package services;

import models.Reply;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/30/12
 * Time: 9:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IReplyService {

    public Reply insert(Reply reply);

    public List<Reply> list(long topicId);

    public List<Reply> listByReplyId(long replyId);
}
