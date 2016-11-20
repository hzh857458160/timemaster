package com.dac.timemaster.aboutApp;

import android.os.AsyncTask;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 邮件管理类
 *
 */
public class EmailSend {

    private static final String SENDER_NAME = "hzh00112@163.com";
    private static final String SENDER_PASS = "SMTP163";
    private static final String VALUE_MAIL_HOST = "smtp.163.com";
    private static final String KEY_MAIL_HOST = "mail.smtp.host";
    private static final String KEY_MAIL_AUTH = "mail.smtp.auth";
    private static final String VALUE_MAIL_AUTH = "true";

    public static EmailSend getInstance() {
        return InstanceHolder.instance;
    }

    private EmailSend() {
    }

    private static class InstanceHolder {
        private static EmailSend instance = new EmailSend();
    }

    class MailTask extends AsyncTask<Void, Void, Boolean> {

        private MimeMessage mimeMessage;

        public MailTask(MimeMessage mimeMessage) {
            this.mimeMessage = mimeMessage;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Transport.send(mimeMessage);
                return Boolean.TRUE;
            } catch (MessagingException e) {
                e.printStackTrace();
                return Boolean.FALSE;
            }
        }
    }

    public void sendMail(final String title, final String content) {
        MimeMessage mimeMessage = createMessage(title, content);
        MailTask mailTask = new MailTask(mimeMessage);
        mailTask.execute();

    }



    private Authenticator getAuthenticator() {
        return new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_NAME, SENDER_PASS);
            }
        };
    }

    private MimeMessage createMessage(String title, String content) {
        Properties properties = System.getProperties();
        properties.put(KEY_MAIL_HOST, VALUE_MAIL_HOST);
        properties.put(KEY_MAIL_AUTH, VALUE_MAIL_AUTH);
        Session session = Session.getInstance(properties, getAuthenticator());
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress(SENDER_NAME));
            InternetAddress[] addresses = new InternetAddress[]{new InternetAddress(SENDER_NAME)};
            mimeMessage.setRecipients(Message.RecipientType.TO, addresses);
            mimeMessage.setSubject(title);
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(content, "text/html");
            textPart.setText(content,"UTF-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            mimeMessage.setContent(multipart);
            mimeMessage.setSentDate(new Date());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return mimeMessage;
    }


}