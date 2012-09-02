package models;

import play.data.validation.Required;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/27/12
 * Time: 9:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Topic implements Serializable {

    private long id;

    @Required
    private String title;

    @Required
    private String content;

    @Required
    private long userId;

    private boolean top;

    private int replyCount;

    private int visitCount;

    private int collectCount;

    private Date createTime;

    private Date updateTime;

    private long lastReply;

    private Date lastReplyTime;

    private boolean html;

    private List<Tag> tagList;

    private List<TopicTag> topicTagList;

    private User user;

    private User reply;

    public List<TopicTag> getTopicTagList() {
        return topicTagList;
    }

    public void setTopicTagList(List<TopicTag> topicTagList) {
        this.topicTagList = topicTagList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public User getReply() {
        return reply;
    }

    public void setReply(User reply) {
        this.reply = reply;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public long getLastReply() {
        return lastReply;
    }

    public void setLastReply(long lastReply) {
        this.lastReply = lastReply;
    }

    public Date getLastReplyTime() {
        return lastReplyTime;
    }

    public void setLastReplyTime(Date lastReplyTime) {
        this.lastReplyTime = lastReplyTime;
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }
}
