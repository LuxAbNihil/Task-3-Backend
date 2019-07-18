package org.demartino.videosharingsite.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.demartino.videosharingsite.view.User;

@Entity
@Table(name="PASSWORDRESETTOKEN")
public class PasswordResetToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="token_id")
	private Long tokenId;
	@Column(name="token")
	private String token; 
	@Column(name="expiration_time")
	private Long expirationTime;
	@OneToOne(targetEntity = AppUser.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private AppUser appUser;
	
	public PasswordResetToken() {}
	
	public PasswordResetToken(User user, String token, long expirationTime) {
		AppUser appUser = new AppUser(user);
		this.appUser = appUser;
		this.token = token;
		this.expirationTime = expirationTime;
	}
	public Long getTokenId() {
		return tokenId;
	}

	public void setTokenId(Long tokenId) {
		this.tokenId = tokenId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Long expirationTime) {
		this.expirationTime = expirationTime;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	
}
