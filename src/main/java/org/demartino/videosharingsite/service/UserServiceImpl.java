package org.demartino.videosharingsite.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.demartino.videosharingsite.dao.UserDao;
import org.demartino.videosharingsite.entity.AppUser;
import org.demartino.videosharingsite.view.Upload;
import org.demartino.videosharingsite.view.User;
import org.demartino.videosharingsite.view.UserAndVideoListContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UploadService uploadService;
	
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
}
