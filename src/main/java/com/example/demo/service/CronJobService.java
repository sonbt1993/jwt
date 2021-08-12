package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class CronJobService {

    @Autowired
    private JavaMailSender emailSender;

    //    @Scheduled(fixedDelay = 1 * 1000 * 60)
//    @Scheduled(cron = "*/1 * * * * ?")
    public void CronJobService() throws MessagingException {
        System.out.println("Execute method asynchronously");
        MimeMessage message = emailSender.createMimeMessage();

        boolean multipart = true;

        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");

        String htmlMsg = "<h3>Im testing send a HTML email</h3>"
                + "<img src='http://www.apache.org/images/asf_logo_wide.gif'>";

        message.setContent(htmlMsg, "text/html");

        helper.setTo(MyConstants.FRIEND_EMAIL);

        helper.setSubject("Test send HTML email");

        this.emailSender.send(message);
        System.out.println("End");
    }


}