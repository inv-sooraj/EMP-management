package com.project.employee.features;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderService {
	@Autowired
	private JavaMailSender mailSender;

	public void sendEmail(String toEmail, String subject, String content)throws UnsupportedEncodingException, MessagingException {
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom("empmanagemenet@gmail.com","JobFinder");
		helper.setBcc("stejojosemon4@gmail.com");
		helper.setTo(toEmail);
		helper.setSubject(subject);
		helper.setText(content,true);
		
		mailSender.send(message);
	}
}
