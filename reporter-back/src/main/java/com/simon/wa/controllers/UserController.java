package com.simon.wa.controllers;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simon.wa.auth.User;
import com.simon.wa.auth.UserRepository;
import com.simon.wa.exceptions.RestObjectNotFoundException;

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
     
    @GetMapping("api/user")
    public User user(HttpServletRequest request, Principal principal) {
        User toReturn = this.userRepo.findByUsername(principal.getName());
        log.info("GET for " + toReturn.getUsername() + " --- " + toReturn.getPassword());
        return toReturn.wipePass();
    }
    
    @PutMapping("api/user")
	public ResponseEntity<User> updateUser( Principal principal, @Valid @RequestBody User user) {
		if (principal.getName().equals(user.getUsername())) {
			User oldUser = this.userRepo.findByUsername(user.getUsername());
			if (oldUser==null)
				throw new RestObjectNotFoundException(log, "user", "name", user.getUsername());
			else {
				if (user.getPassword()!=null) {
					log.info("Resetting password for " + user.getUsername() +  " to " + user.getPassword());
					user.plainToHashedPass();
					oldUser.setPassword(user.getPassword());
				}
				oldUser.setApikey(user.getApikey());
				oldUser.setEnabled(user.isEnabled());
				this.userRepo.save(oldUser);
				return new ResponseEntity<>(oldUser.wipePass(), HttpStatus.OK);
			}
		}
		else {
			throw new RestObjectNotFoundException(log, "user", "name", user.getUsername());
		}
    	
	}
}