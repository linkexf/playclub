package models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/28/12
 * Time: 10:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopicTag implements Serializable {

    private long topicId;

    private long tagId;

    private Date createTime;

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
