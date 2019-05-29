package org.demartino.videosharingsite.service;

import java.util.ArrayList;
import java.util.List;

import org.demartino.videosharingsite.dao.UserDao;
import org.demartino.videosharingsite.entity.AppUser;
import org.demartino.videosharingsite.view.Login;
import org.demartino.videosharingsite.view.Upload;
import org.demartino.videosharingsite.view.UserAndVideoListContainer;
import org.demartino.videosharingsite.view.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
	
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UploadService uploadService;
	
	
	/**
	 * Determines if the login attempt is valid
	 * @param Login : the login object containing the user's username and password
	 * @return Returns a UserAndVideoListContainer with the list of the user's videos and all users. 
	 * 	If the login is not valid it returns an UserAndVideoListContainer object containing empty lists.
	 */
	public UserAndVideoListContainer login(Login login) 
	{
		if(login == null)
		{
			return null;
		}
		UserAndVideoListContainer userAndVideoListContainer = new UserAndVideoListContainer();
		List<User> users = new ArrayList<User>();
		List<Upload> videos = new ArrayList<Upload>();
		AppUser appUser = userDao.isValidLogin(login);
		if(appUser == null) 
		{
			userAndVideoListContainer.setUsers(users);
			userAndVideoListContainer.setVideos(videos);
			return userAndVideoListContainer;
		}
		users = userService.getAllUsers();
		videos = uploadService.getAllVideosForUser(login.getUsername());
		userAndVideoListContainer.setUsers(users);
		userAndVideoListContainer.setVideos(videos);
		return userAndVideoListContainer;
	}	
}