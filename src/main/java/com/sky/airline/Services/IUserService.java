package com.sky.airline.Services;

import com.sky.airline.Entity.AuthRequest;
import com.sky.airline.Entity.User;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IUserService {
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    Boolean registerVeriflyEmail(User user) throws MessagingException;

    Boolean registerVeriflyCode(String email, String code);

    String login(AuthRequest authRequest);
}
