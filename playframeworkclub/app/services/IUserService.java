package services;

import models.User;

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/18/12
 * Time: 9:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IUserService {

    public User getUserByLogin(String loginName);

    public User getUserByEmail(String email);

    public User getUserById(long id);

    public User save(User user);

    public boolean updatePassword(User user);

    public boolean updateActive(User user);

    public boolean update(User user);

    public boolean updateStar(long id,boolean isStar);

    public boolean updateFollowCount(long userId,long followId);

    public boolean subtractFollowCount(long userId,long followId);

    public boolean addTagCollectCount(long userId);

    public boolean subtractTagCollectCount(long userId);

    public boolean updateTopicCount(long userId);

    public boolean addReplyCount(long userId);

    public boolean addTopicCount(long userId);

    public boolean subtractTopicCount(long userId);
}
