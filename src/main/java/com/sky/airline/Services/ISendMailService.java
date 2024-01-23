package com.sky.airline.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sky.airline.Entity.User;
import jakarta.mail.MessagingException;

public interface ISendMailService {

    public String randomCode(String mail, User user) throws JsonProcessingException;
    public void sendEmail(User user) throws MessagingException;
}
