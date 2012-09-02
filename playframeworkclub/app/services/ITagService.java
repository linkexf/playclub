package services;

import models.Tag;
import models.TagCollect;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/26/12
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ITagService {

    public Tag add(Tag tag);

    public void update(Tag tag);

    public List<Tag> list();

    public List<Tag> listByTopic(long topicId);

    public void delete(String name);

    public Tag get(String name);

    public void addTagCollect(TagCollect tagCollect);

    public void deleteTagCollect(TagCollect tagCollect);

    public boolean isCollect(TagCollect tagCollect);

}
