package services;

import models.Topic;
import models.TopicCollect;
import models.TopicTag;
import models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/27/12
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("topicService")
public class TopicService extends BaseServiceSupport implements ITopicService{

    private static final String INSERT_SQL="insert into Topic(title,content,user_id,create_at,update_at,content_is_html) values(?,?,?,now(),now(),?)";
    private static final String INSERT_TOPIC_TAG_SQL="insert into TopicTag(topic_id,tag_id,create_at) values(?,?,now())";
    private static final String SELECT_SQL="select Topic.*,User.name,User.loginName,User.imageUrl from Topic,User where Topic.user_id=User.id order by Topic.id desc limit ?,?";
    private static final String SELECT_BY_ID_SQL="select Topic.*,User.name,User.loginName,User.imageUrl,User.collect_topic_count,User.score,User.collect_tag_count,User.following_count,User.follower_count from Topic,User where Topic.user_id=User.id and Topic.id=?";
    private static final String COUNT_SQL="select count(*) from Topic,User where Topic.user_id=User.id";
    private static final String IS_COLLECT_SQL="select count(*) from TopicCollect where user_id=? and topic_id=?";
    private static final String INSERT_TOPIC_COLLECT_SQL="insert into TopicCollect(user_id,topic_id,create_at) values(?,?,now())";
    private static final String DELETE_TOPIC_COLLECT_SQL="delete from TopicCollect where user_id=? and topic_id=?";
    private static final String UPDATE_TOPIC_LAST_REPLY_SQL="update Topic set last_reply=? , reply_count=reply_count+1 , last_reply_at=now() where id=?";
    private static final String UPDATE_VISIT_COUNT_SQL="update Topic set visit_count=visit_count+1 where id=?";
    private static final String SELECT_BY_TAG_ID_SQL="select Topic.*,User.name,User.loginName,User.imageUrl from Topic,User,TopicTag where Topic.user_id=User.id and Topic.id=TopicTag.topic_id and TopicTag.tag_id=? order by Topic.id desc limit ?,?";
    private static final String COUNT_BY_TAG_SQL="select count(*) from Topic,User,TopicTag where Topic.user_id=User.id and Topic.id=TopicTag.topic_id and TopicTag.tag_id=?";

    public Topic insert(final Topic topic)
    {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.getJdbcTemplate().update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                        ps.setString(1, topic.getTitle());
                        ps.setString(2, topic.getContent());
                        ps.setLong(3, topic.getUserId());
                        ps.setBoolean(4, topic.isHtml());
                        return ps;
                    }
                },
                keyHolder);
        topic.setId(keyHolder.getKey().intValue());
        return topic;
    }

    public void addTopicTag(final TopicTag topicTag)
    {
        getJdbcTemplate().update(INSERT_TOPIC_TAG_SQL, new PreparedStatementSetter(){
            public void setValues(PreparedStatement ps) throws SQLException
            {
                ps.setLong(1, topicTag.getTopicId());
                ps.setLong(2, topicTag.getTagId());
            }
        });
    }

    public List<Topic> list(int offset,int rows)
    {
        List<Topic> topics = this.getJdbcTemplate().query(SELECT_SQL,
                new RowMapper<Topic>() {
                    public Topic mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Topic topic = new Topic();
                        topic.setId(rs.getLong("id"));
                        topic.setUserId(rs.getLong("user_id"));
                        topic.setTitle(rs.getString("title"));
                        topic.setCollectCount(rs.getInt("collect_count"));
                        topic.setTop(rs.getBoolean("top"));
                        topic.setCreateTime(rs.getTimestamp("create_at"));
                        if(rs.getLong("last_reply")>0)
                        {
                            topic.setLastReply(rs.getLong("last_reply"));
                            topic.setLastReplyTime(rs.getTimestamp("last_reply_at"));
                        }
                        topic.setReplyCount(rs.getInt("reply_count"));
                        topic.setVisitCount(rs.getInt("visit_count"));
                        User user=new User();
                        user.setId(topic.getUserId());
                        user.setName(rs.getString("name"));
                        user.setLoginName(rs.getString("loginName"));
                        user.setImageUrl(rs.getString("imageUrl"));
                        topic.setUser(user);
                        return topic;
                    }
                },offset,rows);
        return topics;
    }

    public List<Topic> listByTag(long tagId,int offset,int rows)
    {
        List<Topic> topics = this.getJdbcTemplate().query(SELECT_BY_TAG_ID_SQL,
                new RowMapper<Topic>() {
                    public Topic mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Topic topic = new Topic();
                        topic.setId(rs.getLong("id"));
                        topic.setUserId(rs.getLong("user_id"));
                        topic.setTitle(rs.getString("title"));
                        topic.setCollectCount(rs.getInt("collect_count"));
                        topic.setTop(rs.getBoolean("top"));
                        topic.setCreateTime(rs.getTimestamp("create_at"));
                        if(rs.getLong("last_reply")>0)
                        {
                            topic.setLastReply(rs.getLong("last_reply"));
                            topic.setLastReplyTime(rs.getTimestamp("last_reply_at"));
                        }
                        topic.setReplyCount(rs.getInt("reply_count"));
                        topic.setVisitCount(rs.getInt("visit_count"));
                        User user=new User();
                        user.setId(topic.getUserId());
                        user.setName(rs.getString("name"));
                        user.setLoginName(rs.getString("loginName"));
                        user.setImageUrl(rs.getString("imageUrl"));
                        topic.setUser(user);
                        return topic;
                    }
                },tagId,offset,rows);
        return topics;
    }

    public int count()
    {
        return this.getJdbcTemplate().queryForInt(COUNT_SQL);
    }

    public int countByTag(long tagId)
    {
        return this.getJdbcTemplate().queryForInt(COUNT_BY_TAG_SQL,tagId);
    }


    public Topic get(long id)
    {
        RowMapper<Topic> mapper =  new RowMapper<Topic>() {
            public Topic mapRow(ResultSet rs, int rowNum) throws SQLException {
                Topic topic = new Topic();
                topic.setId(rs.getLong("id"));
                topic.setUserId(rs.getLong("user_id"));
                topic.setTitle(rs.getString("title"));
                topic.setContent(rs.getString("content"));
                topic.setCollectCount(rs.getInt("collect_count"));
                topic.setTop(rs.getBoolean("top"));
                topic.setCreateTime(rs.getTimestamp("create_at"));
                topic.setUpdateTime(rs.getTimestamp("update_at"));
                if(rs.getLong("last_reply")>0)
                {
                    topic.setLastReply(rs.getLong("last_reply"));
                    topic.setLastReplyTime(rs.getTimestamp("last_reply_at"));
                }
                topic.setReplyCount(rs.getInt("reply_count"));
                topic.setVisitCount(rs.getInt("visit_count"));
                User user=new User();
                user.setId(topic.getUserId());
                user.setName(rs.getString("name"));
                user.setLoginName(rs.getString("loginName"));
                user.setImageUrl(rs.getString("imageUrl"));
                user.setCollectTagCount(rs.getInt("collect_tag_count"));
                user.setCollectTopicCount(rs.getInt("collect_topic_count"));
                user.setFollowerCount(rs.getInt("follower_count"));
                user.setFollowingCount(rs.getInt("following_count"));
                user.setScore(rs.getInt("score"));
                topic.setUser(user);
                return topic;
            }
        };

        try{
            return this.getJdbcTemplate().queryForObject(SELECT_BY_ID_SQL, new Object[] {id}, mapper);
        }catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    public boolean isCollect(TopicCollect topicCollect)
    {
        return this.getJdbcTemplate().queryForInt(IS_COLLECT_SQL,topicCollect.getUserId(),topicCollect.getTopicId())>0?true:false;
    }

    public void insertTopicCollect(final TopicCollect topicCollect)
    {
        getJdbcTemplate().update(INSERT_TOPIC_COLLECT_SQL, new PreparedStatementSetter(){
            public void setValues(PreparedStatement ps) throws SQLException
            {
                ps.setLong(1, topicCollect.getUserId());
                ps.setLong(2, topicCollect.getTopicId());
            }
        });
    }

    public void updateTopicLastReply(final Topic topic)
    {
        getJdbcTemplate().update(UPDATE_TOPIC_LAST_REPLY_SQL, new PreparedStatementSetter(){
            public void setValues(PreparedStatement ps) throws SQLException
            {
                ps.setLong(1, topic.getLastReply());
                ps.setLong(2, topic.getId());
            }
        });
    }

    public void deleteTopicCollect(TopicCollect topicCollect)
    {
        this.getJdbcTemplate().update(DELETE_TOPIC_COLLECT_SQL,topicCollect.getUserId(),topicCollect.getTopicId());
    }

    public void addVisitCount(Topic topic)
    {
        this.getJdbcTemplate().update(UPDATE_VISIT_COUNT_SQL,topic.getId());
    }


}
