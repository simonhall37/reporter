package com.simon.wa.controllers;

import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	@PostMapping("/login")
	public boolean login(HttpServletRequest request, @RequestBody User user) {
		try {
			request.login(user.getUsername(), user.getPassword());
		} catch (ServletException e) {
			log.warn("Unsuccessful login attempt from " + user.getUsername() + " --- " + e.getMessage());
			return false;
		}
		log.info(user.getUsername() + " was authenticated");
		return true;
	}

	@GetMapping("/login")
	public boolean redirect() {
		log.info("redirect");
		return true;
	}

	@PostMapping("/customLogout")
	public boolean logout(HttpServletRequest request, Principal principal) {
		HttpSession session = request.getSession(false);
		SecurityContextHolder.clearContext();
		session = request.getSession(false);
		if (session != null) {
			session.invalidate();
			log.info(principal.getName() + " logged out");
			return true;
		}
		else {
			log.warn(principal.getName() +  " - error logging out");
			return false;
		}
		
	}

	@GetMapping("api/user")
	public User user(HttpServletRequest request, Principal principal) {
		User toReturn = this.userRepo.findByUsername(principal.getName());
		log.info("GET for " + toReturn.getUsername() + " --- " + toReturn.getPassword());
		return toReturn.wipePass();
	}

	@PutMapping("api/user")
	public ResponseEntity<User> updateUser(Principal principal, @Valid @RequestBody User user) {
		log.info("Updating:" + user.getUsername());
		if (principal.getName().equals(user.getUsername())) {
			User oldUser = this.userRepo.findByUsername(user.getUsername());
			if (oldUser == null)
				throw new RestObjectNotFoundException(log, "user", "name", user.getUsername());
			else {
				if (user.getPassword() != null) {
					log.info("Resetting password for " + user.getUsername() + " to " + user.getPassword());
					user.plainToHashedPass();
					oldUser.setPassword(user.getPassword());
				}
				oldUser.setApikey(user.getApikey());
				oldUser.setEnabled(user.isEnabled());
				this.userRepo.save(oldUser);
				return new ResponseEntity<>(oldUser.wipePass(), HttpStatus.OK);
			}
		} else {
			throw new RestObjectNotFoundException(log, "user", "name", user.getUsername());
		}

	}
}