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
import com.innovaturelabs.training.employee.management.security.util.TokenGenerator.Token;
import com.innovaturelabs.training.employee.management.util.ForgotPasswordTokenGenerator.PasswordToken;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;

    // Email service for forgot passord
    public void sendForgotPasswordRequest(PasswordToken token, String email) {

        String url = "http://localhost:4200/forgot-password?token=" + token.data + "&expiry=" + token.expiry;

        System.err.println("Token Url : " + url);

        String subject = "Reset password";
        String body = "<h4>Hi, </h4>"
                + "<p>A request has been received to change the password for your botjobs account. </p><br>"
                + "<a href=" + url + ">Click here to reset password</a><br>"
                + "Best regards,<br><b>BOTJOBS</b><br>"
                + "<b>Remarks:If you did not initiate this request,please contact us immediately at empmanagemenet@gmail.com</b>";
        sendEmail(email, subject, body);

    }

    // Email service for change password
    public void changePasswordMail(String email, String name) {

        String subject = "Change Password";
        String body = "<h4>Dear " + name + ", </h4>"
                + "<p>A request has been received to change the password for your botjobs account. </p><br>"
                + "Best regards,<br><b>BOTJOBS</b><br>"
                + "<b>Remarks:If you did not initiate this request,please contact us immediately at empmanagemenet@gmail.com</b>";
        sendEmail(email, subject, body);

    }

    // Email service when user registration
    public void SendUserRegistrationConfirmService(Token token, String email, String name) {

        String url = "http://localhost:4200/user-verify?token=" + token.value;
        String subject = "Verify Email";
        String body = "<h4>Dear " + name + ",</h4> <br>"
                + "<h2>Welcome to Botjobs </h2><br>"
                + "Thank you for signing up with BOTJObs."
                + "To get you started, please click on the button below to verify your account for the first time.<br>+"
                + "<a href=" + url + ">Click here to verify</a><br>"
                + "If you didn't request this email,there's nothing to worry about-  you can safely ignore it.<br>"
                + "Best regards,<br>"
                + "<b>BOTJOBS</b>";
        sendEmail(email, subject, body);

    }

    // Email service for add new user by admin
    public void sendRegisterSuccess(String email, String userName, String password) {

        String subject = " Successfully registered to BOTJOBS";
        String body = "<h4>Hi,</h4>" +
                "We have received a request to create an account in botjobs.com. <p>Please use below credentials to login in.</p>"
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

        String body = "<h4>Dear " + name + ",</h4> <br>"
                + "We have " + status + " your request for adding new job title "
                + jobTitle + " with job id " + jobId
                + ".<br><br> Best regards,<br><b>BOTJOBS</b><br><br><b>Remarks:If you have any queries please fell free to contact us at empmanagemenet@gmail.com. </b>";

        sendEmail(email, subject, body);

    }

    // Email service for job request
    public void sendJobRequestStatus(String email, String name, Integer jobRequestId, String jobTitle, String message,
            Boolean approved) {

        String status = (approved.booleanValue() ? "Approved" : "Rejected");

        String subject = " Job Request " + status;
        String body = "<h4>Dear " + name + ",</h4> <br>"
                + "We have " + status + " your request for the profile "
                + jobTitle + " having <b>job request id "
                + jobRequestId + "</b>.<br><br>"
                + "Best regards,<br><b>BOTJOBS</b><br><br>"
                + "Remark : " + message + "<br>";

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

            helper.setFrom("empmanagemenet@gmail.com", "Bot Jobs");
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
