package services;

import models.Reply;
import models.User;
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
 * Date: 8/30/12
 * Time: 9:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("replyService")
public class ReplyService extends BaseServiceSupport implements IReplyService{

    private static final String INSERT_SQL="insert into Reply(user_id,topic_id,content,reply_id,create_at,update_at,content_is_html) values(?,?,?,?,now(),now(),?)";
    private static final String SELECT_SQL="select Reply.*,User.name,User.loginName,User.imageUrl,User.collect_topic_count,User.score,User.collect_tag_count,User.following_count,User.follower_count from Reply,User where Reply.user_id=User.id and reply_id=0 and topic_id=?";
    private static final String SELECT_BY_REPLY_ID_SQL="select Reply.*,User.name,User.loginName,User.imageUrl,User.collect_topic_count,User.score,User.collect_tag_count,User.following_count,User.follower_count from Reply,User where Reply.user_id=User.id and reply_id=?";

    public Reply insert(final Reply reply)
    {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.getJdbcTemplate().update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                        ps.setLong(1, reply.getUserId());
                        ps.setLong(2, reply.getTopicId());
                        ps.setString(3,reply.getContent());
                        ps.setLong(4,reply.getReplyId());
                        ps.setBoolean(5,reply.isHtml());
                        return ps;
                    }
                },
                keyHolder);
        reply.setId(keyHolder.getKey().intValue());
        return reply;
    }

    public List<Reply> list(long topicId)
    {
        List<Reply> replies = this.getJdbcTemplate().query(SELECT_SQL,
                new RowMapper<Reply>() {
                    public Reply mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Reply reply=new Reply();
                        reply.setId(rs.getLong("id"));
                        reply.setContent(rs.getString("content"));
                        reply.setUserId(rs.getLong("user_id"));
                        reply.setTopicId(rs.getLong("topic_id"));
                        reply.setReplyId(rs.getLong("reply_id"));
                        reply.setCreateTime(rs.getTimestamp("create_at"));
                        reply.setUpdateTime(rs.getTimestamp("update_at"));
                        User user=new User();
                        user.setId(reply.getUserId());
                        user.setName(rs.getString("name"));
                        user.setLoginName(rs.getString("loginName"));
                        user.setImageUrl(rs.getString("imageUrl"));
                        user.setCollectTagCount(rs.getInt("collect_tag_count"));
                        user.setCollectTopicCount(rs.getInt("collect_topic_count"));
                        user.setFollowerCount(rs.getInt("follower_count"));
                        user.setFollowingCount(rs.getInt("following_count"));
                        user.setScore(rs.getInt("score"));
                        reply.setUser(user);
                        return reply;
                    }
                },topicId);
        return replies;
    }

    public List<Reply> listByReplyId(long replyId)
    {
        List<Reply> replies = this.getJdbcTemplate().query(SELECT_BY_REPLY_ID_SQL,
                new RowMapper<Reply>() {
                    public Reply mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Reply reply=new Reply();
                        reply.setId(rs.getLong("id"));
                        reply.setContent(rs.getString("content"));
                        reply.setUserId(rs.getLong("user_id"));
                        reply.setTopicId(rs.getLong("topic_id"));
                        reply.setReplyId(rs.getLong("reply_id"));
                        reply.setCreateTime(rs.getTimestamp("create_at"));
                        reply.setUpdateTime(rs.getTimestamp("update_at"));
                        User user=new User();
                        user.setId(reply.getUserId());
                        user.setName(rs.getString("name"));
                        user.setLoginName(rs.getString("loginName"));
                        user.setImageUrl(rs.getString("imageUrl"));
                        user.setCollectTagCount(rs.getInt("collect_tag_count"));
                        user.setCollectTopicCount(rs.getInt("collect_topic_count"));
                        user.setFollowerCount(rs.getInt("follower_count"));
                        user.setFollowingCount(rs.getInt("following_count"));
                        user.setScore(rs.getInt("score"));
                        reply.setUser(user);
                        return reply;
                    }
                },replyId);
        return replies;
    }
}
