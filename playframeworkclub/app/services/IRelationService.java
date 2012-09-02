package services;

import models.Relation;

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/25/12
 * Time: 9:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IRelationService {

    public void add(Relation relation);

    public void delete(Relation relation);

    public boolean isFollow(long userId,long followId);
}
