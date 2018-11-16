package com.simon.wa.controllers;

import java.security.Principal;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simon.wa.auth.User;
import com.simon.wa.auth.UserRepository;

@RestController
@CrossOrigin
public class UserController {
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepo;
    
	@RequestMapping("/login")
	public boolean login(HttpServletRequest request, @RequestBody User user) {
		log.info(user.getUsername() + " was authenticated");
	    return true;
	}
     
    @RequestMapping("/user")
    public User user(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization")
          .substring("Basic".length()).trim();
        String username = new String(Base64.getDecoder()
          .decode(authToken)).split(":")[0];
        return this.userRepo.findByUsername(username).wipePass();
    }
}