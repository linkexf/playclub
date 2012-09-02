package services;

import models.Relation;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/25/12
 * Time: 9:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("relationService")
public class RelationService extends BaseServiceSupport implements IRelationService{

    private static final String INSERT_SQL="insert into Relation(user_id,follow_id,create_at) values(?,?,now())";

    private static final String DELETE_SQL="delete from Relation where user_id=? and follow_id=?";

    private static final String IS_FOLLOW_SQL="select count(*) from Relation where user_id=? and follow_id=?";

    public void add(final Relation relation)
    {
        getJdbcTemplate().update(INSERT_SQL, new PreparedStatementSetter(){
            public void setValues(PreparedStatement ps) throws SQLException
            {
                ps.setLong(1, relation.getUserId());
                ps.setLong(2, relation.getFollowId());
            }
        });
    }

    public boolean isFollow(long userId,long followId)
    {
        return this.getJdbcTemplate().queryForInt(IS_FOLLOW_SQL,userId,followId)>0?true:false;
    }

    public void delete(Relation relation)
    {
        this.getJdbcTemplate().update(DELETE_SQL,relation.getUserId(),relation.getFollowId());
    }
}
