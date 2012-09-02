package services;

import models.Tag;
import models.TagCollect;
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
 * Date: 8/26/12
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("tagService")
public class TagService extends BaseServiceSupport implements ITagService{

    private static final String INSERT_SQL="insert into Tag (name,description,background,topic_count,collect_count,create_at,tag_order) values(?,?,?,?,?,now(),?)";
    private static final String UPDATE_SQL="update Tag set name=?,description=?,background=?,tag_order=? where id=?";
    private static final String SELECT_SQL="select * from Tag order by tag_order";
    private static final String DELETE_SQL="delete from Tag where name=?";
    private static final String SELECT_BY_NAME_SQL="select * from Tag where name=?";
    private static final String SELECT_BY_TOPIC_ID_SQL="select Tag.* from Tag,TopicTag where Tag.id=TopicTag.tag_id and TopicTag.topic_id=?";

    private static final String ADD_TAG_COLLECT="insert into TagCollect(user_id,tag_id,create_at) values(?,?,now())";
    private static final String DELETE_TAG_COLLECT="delete from TagCollect where user_id=? and tag_id=?";
    private static final String IS_COLLECT_SQL="select count(*) from TagCollect where user_id=? and tag_id=?";

    public Tag add(final Tag tag)
    {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.getJdbcTemplate().update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                        ps.setString(1, tag.getName());
                        ps.setString(2, tag.getDescription());
                        ps.setString(3, tag.getBackground());
                        ps.setInt(4, tag.getTopicCount());
                        ps.setInt(5, tag.getCollectCount());
                        ps.setInt(6, tag.getOrder());
                        return ps;
                    }
                },
                keyHolder);
        keyHolder.getKey();
        tag.setId(keyHolder.getKey().intValue());
        return tag;
    }

    public void update(final Tag tag)
    {
        getJdbcTemplate().update(UPDATE_SQL, new PreparedStatementSetter(){
            public void setValues(PreparedStatement ps) throws SQLException
            {
                ps.setString(1, tag.getName());
                ps.setString(2, tag.getDescription());
                ps.setString(3,tag.getBackground());
                ps.setInt(4,tag.getOrder());
                ps.setLong(5,tag.getId());
            }
        });
    }

    public List<Tag> listByTopic(long topicId)
    {
        List<Tag> tags = this.getJdbcTemplate().query(SELECT_BY_TOPIC_ID_SQL,
                new RowMapper<Tag>() {
                    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Tag tag = new Tag();
                        tag.setId(rs.getLong("id"));
                        tag.setName(rs.getString("name"));
                        tag.setOrder(rs.getInt("tag_order"));
                        return tag;
                    }
                },topicId);
        return tags;
    }

    public List<Tag> list()
    {
        List<Tag> tags = this.getJdbcTemplate().query(SELECT_SQL,
                new RowMapper<Tag>() {
                    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Tag tag = new Tag();
                        tag.setId(rs.getLong("id"));
                        tag.setName(rs.getString("name"));
                        tag.setOrder(rs.getInt("tag_order"));
                        return tag;
                    }
                });
        return tags;

    }

    public void delete(String name)
    {
        this.getJdbcTemplate().update(DELETE_SQL,name);
    }

    public Tag get(String name)
    {
        RowMapper<Tag> mapper = new RowMapper<Tag>() {
            public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
                Tag tag=new Tag();
                tag.setId(rs.getLong("id"));
                tag.setName(rs.getString("name"));
                tag.setDescription(rs.getString("description"));
                tag.setBackground(rs.getString("background"));
                tag.setOrder(rs.getInt("tag_order"));
                return tag;
            }
        };
        try{
            return (Tag) this.getJdbcTemplate().queryForObject(SELECT_BY_NAME_SQL, new Object[] {name}, mapper);
        }catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    public void addTagCollect(final TagCollect tagCollect)
    {
        getJdbcTemplate().update(ADD_TAG_COLLECT, new PreparedStatementSetter(){
            public void setValues(PreparedStatement ps) throws SQLException
            {
                ps.setLong(1, tagCollect.getUserId());
                ps.setLong(2, tagCollect.getTagId());
            }
        });
    }

    public void deleteTagCollect(final TagCollect tagCollect){
        this.getJdbcTemplate().update(DELETE_TAG_COLLECT,tagCollect.getUserId(),tagCollect.getTagId());
    }

    public boolean isCollect(TagCollect tagCollect)
    {
        return this.getJdbcTemplate().queryForInt(IS_COLLECT_SQL,tagCollect.getUserId(),tagCollect.getTagId())>0?true:false;
    }
}
