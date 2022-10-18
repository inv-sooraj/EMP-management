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

    // Email service for forgot passord

    public void sendForgotPasswordRequest(PasswordToken token, String email) {

        String url = "http://localhost:4200/forgot-password?token=" + token.data + "&expiry=" + token.expiry;

        System.err.println("Token Url : " + url);

        String subject = "<h4>Reset password</h4>";
        String body = "<h3>Hi, </h3>"
                + "<p>A request has been received to change the password for your botjobs account. </p><br>"
                + "<a href=" + url + ">Click here to reset password</a><br>"
                + "Best regards,<br><b>BOTJOBS</b><br>"
                + "<b>Remarks:If you did not initiate this request,please contact us immediately at empmanagemenet@gmail.com</b>";
        sendEmail(email, subject, body);

    }

    // Email service for add new user

    public void sendRegisterSuccess(String email, String userName, String password) {

        String subject = " Successfully registered to BOTJOBS";
        String body = "<h4>Hi,</h4>" +
                "We have received a request to create an account in botjobs.com. <p>Please use below credentials to login in</p>"
                + "<h4>Username :<b>" + userName + "</b> </h4>"
                + "<h4>Password :<b>" + password + "</b> </h4><br>"
                + "Best regards,<br><b>BOTJOBS</b><br>"
                + "<b>Remarks:Kindly request you to change your password after login!!</b>";
        sendEmail(email, subject, body);

    }

    // Email service for approving new jobs
    public void sendJobStatus(String email, String name, Integer jobId, String jobTitle, Boolean approved) {

        String status = (approved.booleanValue() ? "Approved" : "Rejected");

        String subject = "Job request " + status;

        String body = "<h4>Dear " + name + ",</h4> <br>" +
                "We are " + status + " your request for adding new job title " + jobTitle + " with job id " + jobId
                + ".<br> Best regards,<br><b>BOTJOBS</b><br><b>Remarks:If you have any queries please fell free to contact us </b>";

        sendEmail(email, subject, body);

    }

    // Email service for job request
    public void sendJobRequestStatus(String email, String name, Integer jobRequestId, String jobTitle, String message,
            Boolean approved) {

        String status = (approved.booleanValue() ? "Approved" : "Rejected");

        String subject = " Job Request " + status;
        // + " For Job Request Id : " + jobRequestId;
        String body = "<h4>Dear " + name + ",</h4> <br>" +
                "We are " + status + "your request for the profile " + jobTitle + " having <b>job request id "
                + jobRequestId + "</b>.<br>" +
                "Best regards,<br><b>BOTJOBS</b><br>" +
                "Remark : " + message + "<br>";

        sendEmail(email, subject, body);

    }

    // Email service for deleted user

    public void sendUserDeleteAlertMail(Integer userId, String email, String name) {
        String body;
        String subject = "User Account Deactivated";
        if (userId.equals(SecurityUtil.getCurrentUserId())) {

            body = "<h4>Dear " + name + ",</h4> <br>" +
                    "<p>Your request for deactivating account in botjobs.com has successfully registred.  </p>  <br>Best regards, <br><b>BOTJOBS</b><br><b>Remark :  If you did not initiate this request,please contact us immediately at empmanagemenet@gmail.com</b>";
        } else {
            body = "<h4>Dear " + name + ",</h4> <br>" +
                    "<p>Your account in botjobs.com has been deactivated by higher authorities. </p> <br> <br>Best regards, <br><b>BOTJOBS</b><br><b>Remark :  If you have any queries please feel free to contact us at empmanagemenet@gmail.com. </b> ";

        }

        sendEmail(email, subject, body);

    }

    // Email service for activate user After deteting
    public void sendUserActiveAlertMAil(Integer userId, String email, String name) {
        String body;
        String subject = "User Account Activated";
        if (userId.equals(SecurityUtil.getCurrentUserId())) {

            body = "<h4>Dear " + name + ",</h4> <br>" +
                    "<p>Your request for activating account in botjobs.com has successfully completed.  </p>  <br>Best regards, <br><b>BOTJOBS</b><br><b>Remark :  If you did not initiate this request,please contact us immediately at empmanagemenet@gmail.com</b>";
        } else {
            body = "<h4>Dear " + name + ",</h4> <br>" +
                    "<p>Your account in botjobs.com has been activated by higher authorities. </p> <br> <br>Best regards, <br><b>BOTJOBS</b><br><b>Remark :  If you have any queries please feel free to contact us at empmanagemenet@gmail.com. </b> ";

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
