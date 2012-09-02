package services;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import play.db.DB;

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/18/12
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseServiceSupport extends NamedParameterJdbcDaoSupport {

    public  BaseServiceSupport()
    {
        this.setDataSource(DB.datasource);
    }
}
