package com.innovaturelabs.training.employee.management.util;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.innovaturelabs.training.employee.management.exception.BadRequestException;
import com.innovaturelabs.training.employee.management.util.ForgotPasswordTokenGenerator.PasswordToken;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;

    public void sendForgotPasswordRequest(PasswordToken token, String email) {

        String url = "http://localhost:4200/forgot-password?token=" + token.data + "&expiry=" + token.expiry;

        System.err.println("Token Url : " + url);

        String subject = "Employee Management : Reset Password";
        String body = "<h3>Reset password </h3>"
                + "<a href=" + url + ">Click here to reset password</a>";
        try {
            sendEmail(email, subject, body);
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
            throw new BadRequestException("Email Service Temporarily Unavailable");
        }

    }

    public void sendRegisterSuccess(String email, String userName, String password) {

        String subject = "Employee Management : Registration Succesful";
        String body = "<h2>Login Details </h2>"
                + "<h4>Username :" + userName + " </h4>"
                + "<h4>Password :" + password + " </h4>"
                + "<h5>Reset Password at first login </h5>";
        try {
            sendEmail(email, subject, body);
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
            throw new BadRequestException("Email Service Temporarily Unavailable");
        }

    }

    public void sendEmail(String toEmail, String subject, String body)
            throws UnsupportedEncodingException, MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("empmanagemenet@gmail.com", "Job Management");
        // helper.setBcc("aaaaa@gmail.com");
        message.setContent(body,"text/html");
        helper.setTo(toEmail);
        helper.setSubject(subject);

        mailSender.send(message);

    }
}
