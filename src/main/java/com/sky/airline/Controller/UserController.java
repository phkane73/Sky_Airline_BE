package com.sky.airline.Controller;

import com.sky.airline.Configure.JwtTokenProvider;
import com.sky.airline.Entity.AuthRequest;
import com.sky.airline.Entity.User;
import com.sky.airline.Services.ISendMailService;
import com.sky.airline.Services.IUserService;
import com.sky.airline.Services.KafkaService.ProducerService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/public/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService iUserService;

    private final RedisTemplate<String,Object> redisTemplate;

    private final ProducerService producerService;

    @PostMapping("/register")
    public ResponseEntity<?> userRegister(@RequestBody User user) throws MessagingException {
        producerService.sendMessage(user);
        return new ResponseEntity<>("send mail success!", HttpStatus.OK);
    }

    @GetMapping("/veriflycode")
    public ResponseEntity<?> veriflycode(@RequestParam("email") String email, @RequestParam("codeverifly") String code) {
        return new ResponseEntity<>(iUserService.registerVeriflyCode(email, code), HttpStatus.OK);
    }
    @GetMapping("/veriflycode2")
    public ResponseEntity<?> veriflycode2(@RequestParam("email") String email) {
        return new ResponseEntity<>(redisTemplate.opsForValue().get(email), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(iUserService.login(authRequest), HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUser(@RequestParam("token") String token) {
        return new ResponseEntity<>(new JwtTokenProvider().getUserIdFromJWT(token), HttpStatus.OK);
    }
}
