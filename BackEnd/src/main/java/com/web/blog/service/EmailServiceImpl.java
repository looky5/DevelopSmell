package com.web.blog.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.web.blog.model.email.Mail;

@Component
public class EmailServiceImpl{

	@Autowired
	private JavaMailSender mailSender;
	
    private static final String FROM_ADDRESS = "sdfgcsdfg@gmail.com";

    private int generateCode() {
        Random rand = new Random();
        int code = rand.nextInt(90000) + 10000;
        return code;
    }
    
    public String mailSend(Mail mail) {
    	int code = generateCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getAddress());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(mail.getTitle());
        message.setText("인증코드 = " + code);
        mailSender.send(message);
        return Integer.toString(code);
    }
}
