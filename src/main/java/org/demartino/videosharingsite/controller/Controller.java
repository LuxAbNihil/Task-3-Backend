package org.demartino.videosharingsite.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.demartino.videosharingsite.entity.PasswordResetToken;
import org.demartino.videosharingsite.jwt.JwtOperations;
import org.demartino.videosharingsite.service.LoginService;
import org.demartino.videosharingsite.service.UploadService;
import org.demartino.videosharingsite.service.UserService;
import org.demartino.videosharingsite.view.Email;
import org.demartino.videosharingsite.view.Login;
import org.demartino.videosharingsite.view.ResetPassword;
import org.demartino.videosharingsite.view.Upload;
import org.demartino.videosharingsite.view.User;
import org.demartino.videosharingsite.view.UserAndVideoListContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(origins= {"http://localhost:4200"}, methods={RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS})
@RequestMapping(value="/user/") 
public class Controller {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private JwtOperations jwtOperations;
	
	private final static Logger logger = LogManager.getLogger(Controller.class);
	
	/**
	 * Grabs a list of all users
	 * @return A ResponseEntity<List<User>> where the list lists all users.
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUsers() {
		logger.debug("In getAllUsers function");
		List<User> users = userService.getAllUsers();
		if(users.isEmpty()) {
			return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	/**
	 * Adds a user to the database via the UserServiceImpl class
	 * @param user : A user object that contains the data for the creation of the entity
	 * @return A responseEntity<User> where User is the a view model representation of the persisted entity.  
	 */
	@RequestMapping(value="/", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> createUser(@RequestBody User user) {
		logger.debug("User is: ", user);
		User returnedUser = userService.createUser(user);
		if(returnedUser == null)
		{
			return new ResponseEntity<User>(returnedUser, HttpStatus.INTERNAL_SERVER_ERROR); //Accepted thrown because processing has not been completed since an entity already exists
		}
		return new ResponseEntity<User>(returnedUser, HttpStatus.CREATED);
	}
	
	/**
	 * Updates a user currently in the database via the UserServiceImpl class
	 * @param user : The user object containing the data to be updated in the database
	 * @param username : The username of the account being updated
	 * @return A ResponseEntity<User> where the User is the updated users. 
	 */
	@RequestMapping(value="/{username}", method=RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("username") String username) {
		logger.debug("User is: ", user);
		User returnedUser = userService.updateUser(user);
		logger.debug("Returned User is: ", returnedUser);
		if(returnedUser == null)
		{
			return new ResponseEntity<User>(returnedUser, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<User>(returnedUser, HttpStatus.OK); 
	}
	
	/**
	 * Deletes a user from the database via the UserServiceImpl class
	 * @param username : The username of the account to be deleted
	 * @return A ResponseEntity<Boolean> where the boolean indicates whether or not the resource was successfully delted. 
	 */
	@RequestMapping(value="/{username}", method=RequestMethod.DELETE) 
	public ResponseEntity<Boolean> deleteUser(@PathVariable("username") String username) {
		boolean isUserDeleted = userService.deleteUserByUsername(username);
		return new ResponseEntity<Boolean>(isUserDeleted, HttpStatus.OK);
	}
	
	/**
	 * Checks if login credentials are valid
	 * @param login : A login object containing the username and password of the user logging on.
	 * @return ResponsEntity<String>: Contains the token generated during a valid login or JSON string EMPTY if 
	 * 	invalid login.
	 * @throws JsonProcessingException when the generated token cannot be converted to JSON
	 */
	@RequestMapping(value="login/", method=RequestMethod.POST)
	public ResponseEntity<String> login(@RequestBody Login login) throws JsonProcessingException {
		boolean isValidLogon = loginService.login(login);
		String token;
		ObjectMapper objectMapper = new ObjectMapper();
		if(isValidLogon) {
			token = jwtOperations.createJws(login.getUsername());
			token = objectMapper.writeValueAsString(token);
		}
		else {
			token = "EMPTY";
		}
		return new ResponseEntity<String>(token, HttpStatus.OK);
	}
	
	/**
	 * Retrieves user by username from db via the Service Layer
	 * @param username : The username of the user to be retrieved
	 * @return A ResponseEntity<User> where the User is the user retrieved using the given username
	 */
	@RequestMapping(value="/{username}", method=RequestMethod.GET)
	public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
		User userToBeReturned = userService.findUserByUsername(username);
		if(userToBeReturned == null) 
		{
			return new ResponseEntity<User>(userToBeReturned, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<User>(userToBeReturned, HttpStatus.OK); 
	}
	/**
	 * Retrieves the UserAndVideoListContainer for a user.
	 * @param username: the username for which the video list is populated
	 * @return: A ResponseEntity<UserAndVideoListContainer> where the UserAndVideoListContainer
	 * is the list of all users in the system and the list of all videos uploaded for a particular user. 
	 */
	@RequestMapping(value="/getUserAndVideoListContainer/{username}", method=RequestMethod.POST)
	public ResponseEntity<UserAndVideoListContainer> getUserAndVideoListConatiner(@RequestBody String token, @PathVariable("username") String username) { 
		UserAndVideoListContainer userAndVideoListContainer = new UserAndVideoListContainer();
		boolean isAuthenticated = jwtOperations.verifyJws(token);
		System.out.println("In getUserAndVideoListContainer controller method");
		
		if(isAuthenticated) {
			userAndVideoListContainer = userService.getUserAndVideoListContainer(username);
			if(userAndVideoListContainer.getUsers().size() == 0) {
				return new ResponseEntity<UserAndVideoListContainer>(userAndVideoListContainer, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<UserAndVideoListContainer>(userAndVideoListContainer, HttpStatus.OK);
			}	
		}
		List<User> users = new ArrayList<User>();
		List<Upload> uploads = new ArrayList<Upload>();
		userAndVideoListContainer.setUsers(users);
		userAndVideoListContainer.setVideos(uploads);
		return new ResponseEntity<UserAndVideoListContainer>(HttpStatus.OK);	
	}
	
	/**
	 * Sends an email with a token for resetting a forgotten password
	 * @param Email: The email of the account to reset the password for
	 * @return Returns a ResponseEntity<Boolean> true if if the user object is not null false otherwise
	 */
	@RequestMapping(value="/forgotPassword/", method=RequestMethod.POST)
	public ResponseEntity<Boolean> forgotPassword(HttpServletRequest request, @RequestBody Email email) {
		String emailString = email.getEmail();
		User user = userService.getUserByEmail(emailString);
		if(user == null) {
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		}
		String token = UUID.randomUUID().toString();
		long expirationTime = userService.setExpirationEpochForPasswordResetToken();
		PasswordResetToken passwordResetToken = userService.createPasswordResetToken(user, token, expirationTime);
		userService.sendPasswordResetEmail(passwordResetToken, emailString);
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	@RequestMapping(value="/resetPassword/", method=RequestMethod.POST)
	public ResponseEntity<String> resetPassword(@RequestBody ResetPassword resetPassword) {
		System.out.println("IN RESET PASSWORD");
		boolean passwordUpdated = userService.updatePassword(resetPassword);
		if(!passwordUpdated) {
			return new ResponseEntity<String>("Token has expired, please"
					+ "go back to the password forgot page and request a"
					+ "new one", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Password has been reset.", HttpStatus.OK);
	}
}	
