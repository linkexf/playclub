package notifiers;

import models.User;
import org.apache.commons.mail.EmailAttachment;
import play.Play;
import play.libs.Crypto;
import play.mvc.Mailer;
import play.mvc.Scope;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: linke
 * Date: 8/20/12
 * Time: 8:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Mails extends Mailer {

    public static void welcome(User user) {
        user.setSecret(Crypto.encryptAES(user.getEmail()));
        setSubject("欢迎加入%s ,请激活您的帐号!", Play.configuration.getProperty("application.name"));
        addRecipient(user.getEmail());
        setFrom("Play!Club <" + Play.configuration.getProperty("mail.smtp.user") + ">");
        send(user);
    }

    public static void lostPassword(User user) {
        user.setSecret(Crypto.encryptAES(user.getEmail()+"&"+new Date()));
        setFrom("Play!Club <" + Play.configuration.getProperty("mail.smtp.user") + ">");
        setSubject("修改密码确认函");
        addRecipient(user.getEmail());
        send(user);
    }
}
