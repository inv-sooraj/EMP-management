package com.innovaturelabs.training.employee.management.util;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.innovaturelabs.training.employee.management.exception.BadRequestException;
import com.innovaturelabs.training.employee.management.security.util.SecurityUtil;
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
        sendEmail(email, subject, body);

    }

    public void sendRegisterSuccess(String email, String userName, String password) {

        String subject = "Employee Management : Registration Succesful";
        String body = "<h2>Login Details </h2>"
                + "<h4>Username :" + userName + " </h4>"
                + "<h4>Password :" + password + " </h4>"
                + "<h5>Reset Password at first login </h5>";
        sendEmail(email, subject, body);

    }

    public void sendJobStatus(String email, Integer jobId, String jobTitle, Boolean approved) {

        String status = (approved.booleanValue() ? "Approved" : "Rejected");

        String subject = "Job status " + status;
                
        String body = "<h2>Job Status </h2>" +
                "Job Id : " + jobId + "<br>" +
                "Job Title : " + jobTitle + "<br>" +
                "Status : " + status;
        sendEmail(email, subject, body);

    }

    public void sendJobRequestStatus(String email,String name, Integer jobRequestId, String jobTitle, String message,
            Boolean approved) {

        String status = (approved.booleanValue() ? "Approved" : "Rejected");

        String subject = " Job Request " + status;
                // + " For Job Request Id : " + jobRequestId;
        String body = "<h4>Dear " + name + "</h4> <br>" +
                "We are "+status+ "your request for the profile "+jobTitle+" having <b>job request id "+ jobRequestId +"</b>.<br>" +
                "Best regards<br><b>botjobs</b>"+
                "Remark : " + message + "<br>" ;
                
        sendEmail(email, subject, body);

    }

    // Email service for deleted user

    public void sendUserDeleteAlertMail(Integer userId, String email, String name) {
        String body;
        String subject = "User Account Deactivated";
        if (userId.equals(SecurityUtil.getCurrentUserId())) {

            body = "<h4>Dear " + name + "</h4> <br>" +
                    "<p>Your request for deactivating account in botjobs.com with <b> user id " + userId
                    + " </b>is successfully registred.  </p>  <br>Best regards <br><b>botjobs</b><br>Remark : <b> If not done by you please feel free to contact us.</b>" ;
        } else {
            body = "<h4>Dear " + name + "</h4> <br>" +
                    "<p>Your account in botjobs.com with <b>user id " + userId
                    + "</b> has been deactivated by higher authorities. </p> <br> <br>Best regards <br><b>botjobs</b><br>Remark : <b> If you have any quires please feel free to contact us. </b> ";

        }

        sendEmail(email, subject, body);

    }

    public void sendEmail(String toEmail, String subject, String body) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom("empmanagemenet@gmail.com", "Job Management");
            // helper.setBcc("aaaaa@gmail.com");
            message.setContent(body, "text/html");
            helper.setTo(toEmail);
            helper.setSubject(subject);

            mailSender.send(message);
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
            throw new BadRequestException("Email Service Temporarily Unavailable");
        }

    }

}
