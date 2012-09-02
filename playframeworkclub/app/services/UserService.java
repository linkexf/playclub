package services;

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

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/18/12
 * Time: 9:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("userService")
public class UserService extends BaseServiceSupport implements IUserService{

    private static final String SELECT_SQL="select * from User where loginname = ?";
    private static final String SELECT_SQL_BY_EMAIL="select * from User where email = ?";
    private static final String SELECT_SQL_BY_ID="select * from User where id = ?";
    private static final String INSERT_SQL="insert into User(name,loginname,email,password,imageUrl,create_at,update_at,active,isAdmin) values(?,?,?,?,?,now(),now(),?,?)";
    private static final String UPDATE_PASSWORD_SQL="update User set password=? where id=?";
    private static final String UPDATE_ACTIVE_SQL="update User set active=? where id=?";
    private static final String UPDATE_STAR_SQL="update User set is_star=? where id=?";
    private static final String UPDATE_FOLLOWING_SQL="update User set following_count=following_count+1 where id=?";
    private static final String UPDATE_FOLLOWER_SQL="update User set follower_count=follower_count+1 where id=?";
    private static final String SUB_FOLLOWING_SQL="update User set following_count=following_count-1 where id=?";
    private static final String SUB_FOLLOWER_SQL="update User set follower_count=follower_count-1 where id=?";
    private static final String UPDATE_SQL="update User set name=?,url=?,location=?,weibo=?,signature=?,profile=?,receive_mail=?,update_at=now() where id=?";

    private static final String ADD_TAGCOLLECTCOUNT_SQL="update User set collect_tag_count=collect_tag_count+1 where id=?";
    private static final String SUB_TAGCOLLECTCOUNT_SQL="update User set collect_tag_count=collect_tag_count-1 where id=?";
    private static final String ADD_TOPIC_COUNT_SQL="update User set topic_count=topic_count+1 where id=?";
    private static final String ADD_REPLY_COUNT_SQL="update User set reply_count=reply_count+1 where id=?";

    private static final String ADD_TOPIC_COLLECT_SQL="update User set collect_topic_count=collect_topic_count+1 where id=?";
    private static final String SUB_TOPIC_COLLECT_SQL="update User set collect_topic_count=collect_topic_count-1 where id=?";

    public User getUserByLogin(String loginName){
        RowMapper<User> mapper = new RowMapper<User>() {
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setLoginName(rs.getString("loginname"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setUrl(rs.getString("url"));
                user.setLocation(rs.getString("location"));
                user.setWeibo(rs.getString("weibo"));
                user.setSignature(rs.getString("signature"));
                user.setProfile(rs.getString("profile"));
                user.setReceiveMail(rs.getBoolean("receive_mail"));
                user.setActive(rs.getBoolean("active"));
                user.setImageUrl(rs.getString("imageUrl"));
                user.setCreateTime(rs.getTimestamp("create_at"));
                user.setStar(rs.getBoolean("is_star"));
                user.setAdmin(rs.getBoolean("isAdmin"));
                user.setFollowerCount(rs.getInt("follower_count"));
                user.setFollowingCount(rs.getInt("following_count"));
                user.setCollectTagCount(rs.getInt("collect_tag_count"));
                user.setTopicCount(rs.getInt("topic_count"));
                user.setCollectTopicCount(rs.getInt("collect_topic_count"));
                return user;
            }

        };
        try{
            return (User) this.getJdbcTemplate().queryForObject(SELECT_SQL, new Object[] {loginName}, mapper);
        }catch (EmptyResultDataAccessException e)
        {
            return null;
        }

    }

    public User getUserByEmail(String email){
        RowMapper<User> mapper = new RowMapper<User>() {
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setLoginName(rs.getString("loginname"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setActive(rs.getBoolean("active"));
                return user;
            }

        };
        try{
            return (User) this.getJdbcTemplate().queryForObject(SELECT_SQL_BY_EMAIL, new Object[] {email}, mapper);
        }catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    public User getUserById(long id)
    {
        RowMapper<User> mapper = new RowMapper<User>() {
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setLoginName(rs.getString("loginname"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setUrl(rs.getString("url"));
                user.setLocation(rs.getString("location"));
                user.setWeibo(rs.getString("weibo"));
                user.setSignature(rs.getString("signature"));
                user.setProfile(rs.getString("profile"));
                user.setReceiveMail(rs.getBoolean("receive_mail"));
                user.setActive(rs.getBoolean("active"));
                user.setImageUrl(rs.getString("imageUrl"));
                user.setCreateTime(rs.getTimestamp("create_at"));
                user.setStar(rs.getBoolean("is_star"));
                user.setAdmin(rs.getBoolean("isAdmin"));
                user.setFollowerCount(rs.getInt("follower_count"));
                user.setFollowingCount(rs.getInt("following_count"));
                user.setCollectTagCount(rs.getInt("collect_tag_count"));
                user.setTopicCount(rs.getInt("topic_count"));
                user.setCollectTopicCount(rs.getInt("collect_topic_count"));
                return user;
            }

        };
        try{
            return (User) this.getJdbcTemplate().queryForObject(SELECT_SQL_BY_ID, new Object[] {id}, mapper);
        }catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    public User save(final User user){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.getJdbcTemplate().update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                        ps.setString(1, user.getName());
                        ps.setString(2, user.getLoginName());
                        ps.setString(3, user.getEmail());
                        ps.setString(4, user.getPassword());
                        ps.setString(5, user.getImageUrl());
                        ps.setBoolean(6, user.isAdmin());
                        ps.setBoolean(7, user.isActive());
                        return ps;
                    }
                },
                keyHolder);
        keyHolder.getKey();
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    public boolean update(final User user)
    {
        return getJdbcTemplate().update(UPDATE_SQL, new PreparedStatementSetter(){
            public void setValues(PreparedStatement ps) throws SQLException
            {
                ps.setString(1, user.getName());
                ps.setString(2, user.getUrl());
                ps.setString(3, user.getLocation());
                ps.setString(4, user.getWeibo());
                ps.setString(5, user.getSignature());
                ps.setString(6, user.getProfile());
                ps.setBoolean(7,user.isReceiveMail());
                ps.setLong(8,user.getId());
            }
        })>0?true:false;
    }

    public boolean updatePassword(User user)
    {
        return this.getJdbcTemplate().update(UPDATE_PASSWORD_SQL,user.getPassword(),user.getId())>0?true:false;
    }

    public boolean updateActive(User user){
        return this.getJdbcTemplate().update(UPDATE_ACTIVE_SQL,user.isActive(),user.getId())>0?true:false;
    }

    public boolean updateStar(long id,boolean isStar){
        return this.getJdbcTemplate().update(UPDATE_STAR_SQL,isStar,id)>0?true:false;
    }

    public boolean updateFollowCount(long userId,long followId)
    {
        int i=this.getJdbcTemplate().update(UPDATE_FOLLOWING_SQL,userId);
        int j=this.getJdbcTemplate().update(UPDATE_FOLLOWER_SQL,followId);
        return i+j>1?true:false;
    }

    public boolean subtractFollowCount(long userId,long followId)
    {
        int i=this.getJdbcTemplate().update(SUB_FOLLOWING_SQL,userId);
        int j=this.getJdbcTemplate().update(SUB_FOLLOWER_SQL,followId);
        return i+j>1?true:false;
    }

    public boolean addTagCollectCount(long userId)
    {
        return this.getJdbcTemplate().update(ADD_TAGCOLLECTCOUNT_SQL,userId)>0?true:false;
    }

    public boolean subtractTagCollectCount(long userId){
        return this.getJdbcTemplate().update(SUB_TAGCOLLECTCOUNT_SQL,userId)>0?true:false;
    }

    public boolean updateTopicCount(long userId){
        return this.getJdbcTemplate().update(ADD_TOPIC_COUNT_SQL,userId)>0?true:false;
    }

    public boolean addReplyCount(long userId)
    {
        return this.getJdbcTemplate().update(ADD_REPLY_COUNT_SQL,userId)>0?true:false;
    }

    public boolean addTopicCount(long userId)
    {
        return this.getJdbcTemplate().update(ADD_TOPIC_COLLECT_SQL,userId)>0?true:false;
    }

    public boolean subtractTopicCount(long userId)
    {
        return this.getJdbcTemplate().update(SUB_TOPIC_COLLECT_SQL,userId)>0?true:false;
    }

}
