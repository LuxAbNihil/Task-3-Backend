package org.demartino.videosharingsite.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.demartino.videosharingsite.dao.UserDao;
import org.demartino.videosharingsite.entity.AppUser;
import org.demartino.videosharingsite.entity.PasswordResetToken;
import org.demartino.videosharingsite.password.Password;
import org.demartino.videosharingsite.view.ResetPassword;
import org.demartino.videosharingsite.view.Upload;
import org.demartino.videosharingsite.view.User;
import org.demartino.videosharingsite.view.UserAndVideoListContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UploadService uploadService;
	
	@Value("${sender_email}")
	private String sendingEmail;
	
	@Value("${password_reset_link}")
	private String passwordResetPage;
	
	@Value("${email_username}")
	private String username;
	
	@Value("${email_password}")
	private String password;
	
	private final static Logger logger = LogManager.getLogger(UserServiceImpl.class);
	
	/**
	 * Passes the presented user object to the DAO for persisting the user if it doesn't already exist
	 * @param User : The user object to be persisted to the DB
	 * @return Returns the persisted User object or  null if the username already exists
	 */
	public User createUser(User user) {
		if(user == null || userDao.findUserByUsername(user.getUsername()) != null)
		{
			return null;
		}
		String hashedPassword = Password.hash(user.getPassword());
		user.setPassword(hashedPassword);
		AppUser appUser = new AppUser(user);
		AppUser returnedAppUser = userDao.createUser(appUser);
		User returnedUser = new User(returnedAppUser);
		return returnedUser;
	}

	public boolean deleteUserByUsername(String username) {
		if(username == null) {
			return false;
		}
		boolean successful = userDao.deleteUserByUsername(username);
		return successful;
	}

	public User findUserByUsername(String username) {
		if(username == null) {
			return null;
		}
		AppUser appUser = userDao.findUserByUsername(username);
		if(appUser == null)
		{
			return null;
		}
		User returnedUser = new User(appUser);
		return returnedUser;
	}

	public User updateUser(User user) {
		if(user == null) {
			return null;
		}
		AppUser appUser = userDao.findUserByUsername(user.getUsername());
		if(appUser == null)
		{
			return null;
		}
	    appUser = appUser.convert(user);
		appUser = userDao.updateUser(appUser);
		User returnedUser = new User(appUser);
		return returnedUser;
	}
	
	public List<User> getAllUsers() {
		List<AppUser> userEntities = userDao.getAllUsers();
		List<User> users = new ArrayList<User>();
		for(int i = 0; i<userEntities.size();i++)
		{
			AppUser userEntity = userEntities.get(i);
			User user = new User(userEntity);
			users.add(user);
		}
		return users;
	}
	
	public UserAndVideoListContainer getUserAndVideoListContainer(String username) {
		UserAndVideoListContainer userAndVideoListContainer = new UserAndVideoListContainer();
		List<User> users = new ArrayList<User>();
		List<Upload> videos = new ArrayList<Upload>();
		AppUser appUser = userDao.findUserByUsername(username);
		if(username == null || appUser == null) {
			userAndVideoListContainer.setUsers(users);
			userAndVideoListContainer.setVideos(videos);
			return userAndVideoListContainer;
		}
		userAndVideoListContainer.setUsers(getAllUsers());
		userAndVideoListContainer.setVideos(uploadService.getAllVideosForUser(username));
		return userAndVideoListContainer;
	}
	
	public User getUserByEmail(String email) {
		AppUser appUser = userDao.getUserByEmail(email);
		User user = new User(appUser);
		return user;
	}
	
	@Override
	public PasswordResetToken createPasswordResetToken(User user, String token, long expirationTime) {
		PasswordResetToken myToken = new PasswordResetToken(user, token, expirationTime);
		myToken = userDao.createPasswordResetToken(myToken);
		return myToken;
	}
	
	public long setExpirationEpochForPasswordResetToken() {
		LocalDateTime now = LocalDateTime.now();
		ZoneId zoneId = ZoneId.systemDefault();
		long epoch = now.atZone(zoneId).toEpochSecond();
		long expirationTime = epoch + (long)(3600 * 24);
		return expirationTime;
	}

	public void sendPasswordResetEmail(PasswordResetToken passwordResetToken, String email) {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "localhost");
		properties.put("mail.smtp.port", "25");
	    properties.put("mail.smtp.auth", "true");
		Session session = Session.getInstance(properties,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		String token = passwordResetToken.getToken();
		
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(sendingEmail));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));
			message.setSubject("Password Reset for Videoshare");
			message.setContent(
					"<h1>Please Click the Link to Reset Your Password</h1>"
					+ "<p>Please copy and paste this token into the appropriate field"
					+ "on the reset password page<p>" 
					+ "<a href=" + passwordResetPage + ">" + token + "</a>", "text/html"
					);
			Transport.send(message);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean updatePassword(ResetPassword resetPassword) {
		String hashedPassword = Password.hash(resetPassword.getPassword());
		String token = resetPassword.getToken();
		PasswordResetToken passwordResetToken = userDao.getPasswordResetTokenByToken(token);
		long expiration = passwordResetToken.getExpirationTime();
		Instant instant = Instant.now();
		long currentTimeEpoch = instant.getEpochSecond();
		if(currentTimeEpoch > expiration) 
		{
			return false;
		}
		AppUser appUser = userDao.getUserByUserId(passwordResetToken.getAppUser().getId());
		appUser.setPassword(hashedPassword);
		appUser = userDao.updateUser(appUser);
		return true;
	}

	
}
