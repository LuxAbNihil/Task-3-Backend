package org.demartino.videosharingsite.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.demartino.videosharingsite.view.User;
import org.springframework.stereotype.Repository;

@Repository
@Entity
@Table(name="APPUSER")
public class AppUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id", nullable=false)
	private Long id;
	@Column(name="username", nullable=false)
	private String username;
	@Column(name="password", nullable=false)
	private String password;  
	@Column(name="email")
	private String email;
	@Column(name="address")
	private String address;
	@Column(name="phoneNumber") //underscore between words because SQL is case insensitive
	private String phoneNumber;
	@Column(name="age")
	private Short age;
	
	@OneToMany(mappedBy="userEntity", fetch=FetchType.LAZY)
	private Set<UploadedVideo> videos; //TODO use two calls instead of join
	
	public AppUser() {};
	
	public AppUser(User user)
	{
		super();
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.address = user.getAddress();
		this.phoneNumber = user.getPhoneNumber();
		this.age = user.getAge();
		this.password = user.getPassword();
	}
	
	
	public AppUser convert(User user)
	{	
		//should I instantiate a new AppUser appUser and assign the 
		//values to the appUser variable and then return that variable 
		//or is this fine?
		if(user.getId() != null)
		{
			this.id = user.getId();
		}
		if(user.getEmail() != null)
		{
			this.email = user.getEmail();
		}
		if(user.getAddress() != null)
		{
			this.address = user.getAddress();
		}
		if(user.getAge() != null)
		{
			this.age = user.getAge();
		}
		if(user.getPhoneNumber() != null)
		{
			this.phoneNumber = user.getPhoneNumber();
		}
		if(user.getPassword() != null)
		{
			this.password = user.getPassword();
		}
		return(this);
	}
	
	@Override
	public String toString() {
		return "User [username=" + username + ", id=" + id + ", email=" + email + ", address=" + address
				+ ", phoneNumber=" + phoneNumber + ", age=" + age + ", password=" + password + "]";
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Short getAge() {
		return age;
	}
	public void setAge(Short age) {
		this.age = age;
	} 
	
	
}