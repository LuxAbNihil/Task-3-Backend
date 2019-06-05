package org.demartino.videosharingsite.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.demartino.videosharingsite.service.LoginService;
import org.demartino.videosharingsite.service.UploadService;
import org.demartino.videosharingsite.service.UserService;
import org.demartino.videosharingsite.view.Login;
import org.demartino.videosharingsite.view.Upload;
import org.demartino.videosharingsite.view.User;
import org.demartino.videosharingsite.view.UserAndVideoListContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/user/") //might not need first slash
public class Controller {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private UploadService uploadService;
	
	private final static Logger logger = Logger.getLogger(Controller.class);
	
	/**
	 * Grabs a list of all users
	 * @return A ResponseEntity<List<User>> where the list lists all users.
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUsers() {
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
	@RequestMapping(value="/", method=RequestMethod.POST) 
	public ResponseEntity<User> createUser(@RequestBody User user) {
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
	@RequestMapping(value="/{username}", method=RequestMethod.PUT) 
	public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("username") String username) {
		User returnedUser = userService.updateUser(user); 
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
	@RequestMapping(value="/{username}", method=RequestMethod.DELETE) //make urls rest compliant (/user/{username} for example)
	public ResponseEntity<Boolean> deleteUser(@PathVariable("username") String username) {
		boolean isUserDeleted = userService.deleteUserByUsername(username);
		return new ResponseEntity<Boolean>(isUserDeleted, HttpStatus.OK);
	}
	
	/**
	 * Checks if login credentials are valid
	 * @param login : A login object containing the username and password of the user logging on.
	 * @return A ResponsEntity<Boolean> where the boolean indicates whether the login was successful or not. 
	 */
	@RequestMapping(value="login/", method=RequestMethod.POST)
	public ResponseEntity<UserAndVideoListContainer> login(@RequestBody Login login) {
		UserAndVideoListContainer isValidLogon = loginService.login(login);
		return new ResponseEntity<UserAndVideoListContainer>(isValidLogon, HttpStatus.OK);
	}
	
	/**
	 * Retrieves user by username from via the Service Layer
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
}	
