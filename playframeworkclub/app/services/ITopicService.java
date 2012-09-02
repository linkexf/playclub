package services;

import models.Topic;
import models.TopicCollect;
import models.TopicTag;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/27/12
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ITopicService {

    public Topic insert(Topic topic);

    public void addTopicTag(TopicTag topicTag);

    public List<Topic> list(int offset,int rows);

    public List<Topic> listByTag(long tagId,int offset,int rows);

    public int count();

    public int countByTag(long tagId);

    public Topic get(long id);

    public boolean isCollect(TopicCollect topicCollect);

    public void insertTopicCollect(TopicCollect topicCollect);

    public void deleteTopicCollect(TopicCollect topicCollect);

    public void updateTopicLastReply(Topic topic);

    public void addVisitCount(Topic topic);
}
