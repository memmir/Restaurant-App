package com.inn.restaurant.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMessage(String to, String subject, String text, List<String> list){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("burayamailadresi");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        if(list != null && list.size() > 0){
            message.setCc(getCcArray(list));
        }

        mailSender.send(message);
    }


    private String[] getCcArray(List<String> ccList){
        String[] ccArray = new String[ccList.size()];
        for(int i = 0; i < ccList.size(); i++){
            ccArray[i] = ccList.get(i);
        }
        return ccArray;
    }
}
