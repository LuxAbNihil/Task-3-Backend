package org.demartino.videosharingsite.service;

import java.util.ArrayList;
import java.util.List;

import org.demartino.videosharingsite.dao.UserDao;
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
	
	public UserAndVideoListContainer login(Login login) 
	{
		UserAndVideoListContainer userAndVideoListContainer = new UserAndVideoListContainer();
		List<User> users = new ArrayList<User>();
		List<Upload> videos = new ArrayList<Upload>();
		boolean isLoggedIn = userDao.isValidLogin(login);
		if(isLoggedIn == false) 
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