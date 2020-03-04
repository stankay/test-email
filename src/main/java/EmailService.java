import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

/**
 * Simple Spring service to send e-mails. The service expects input parameters to be full-fledged
 * e-mail sources including headers, attachments etc.
 */
public class EmailService {

    /**
     * SMTP host taken from Spring Boot config
     */
    private String smtpHost;

    private boolean smtpActuallySend = true;

    public void setSmtpHost(String host) {
        smtpHost = host;
    }

    public void setSmtpActuallySend(boolean smtpActuallySend) {
        this.smtpActuallySend = smtpActuallySend;
    }

    /**
     * Send passed MimeMessage via e-mail
     *
     * @param mimeMessage
     * @throws MessagingException
     * @throws IOException
     */
    public void sendEmail(MimeMessage mimeMessage) throws MessagingException {

        Properties props = new Properties();
        props.setProperty("mail.smtp.host", smtpHost);
        //properties.put("mail.smtp.port", sender.getSmtp().getPort());

        Session session = Session.getInstance(props);

        Transport tr = session.getTransport("smtp");
        tr.connect();
        tr.sendMessage(mimeMessage, mimeMessage.getFrom());
    }

    /**
     * Convert mimeMessageSource to MimeMessage and send via e-mail
     *
     * @param mimeMessageSource
     * @throws MessagingException
     */
    public void sendEmail(String mimeMessageSource) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(
                Session.getInstance(new Properties()),
                new ByteArrayInputStream(mimeMessageSource.getBytes()));

        sendEmail(mimeMessage);
    }

    public static void main(String[] args) throws Exception {
        EmailService es = new EmailService();
        es.setSmtpHost("smtp.cpas.cz");
        es.setSmtpActuallySend(true);
        String mimeMessageSource =
                  "From: soudnispory@generaliceska.cz\n"
                + "To: michal.stankay1@generaliceska.cz\n"
                //+ "To: michal@stankay.net\n"
                + "Subject: Hi from test @ " + new Date() + "\n\n"
                + "Hello!";

        System.out.println("Sending\n----\n" + mimeMessageSource + "\n----");

        es.sendEmail(mimeMessageSource);
    }
}
