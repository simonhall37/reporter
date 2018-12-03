package com.simon.wa.auth;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "user_profile")
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
	
	private String username;
	private String password;
	private String apikey;
	private boolean enabled;
	
	public User(){}
	
	public User(String username, String password, String apikey) {
		this.username = username;
		this.apikey = apikey;
		this.password = new BCryptPasswordEncoder(11).encode(password);
		this.enabled = true;
	}
	
	public User wipePass() {
		this.password  = null;
		return this;
	}
	
	public User plainToHashedPass() {
		this.password = new BCryptPasswordEncoder(11).encode(password);
		return this;
	}
	
	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getApikey() {
		return apikey;
	}
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return this.enabled;
	}
}