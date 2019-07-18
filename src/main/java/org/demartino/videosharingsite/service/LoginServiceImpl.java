package org.demartino.videosharingsite.service;

import java.util.ArrayList;
import java.util.List;

import org.demartino.videosharingsite.dao.UserDao;
import org.demartino.videosharingsite.entity.AppUser;
import org.demartino.videosharingsite.password.Password;
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
	
	/**
	 * Determines if the login attempt is valid
	 * @param Login : the login object containing the user's username and password
	 * @return Returns a boolean, true if valid username and password, false otherwise. 
	 */
	public boolean login(Login login) 
	{
		if(login == null)
		{
			return false;
		}
		boolean isPasswordValid = Password.validate(login.getPassword(), userDao.getPasswordByUsername(login.getUsername()));
		AppUser appUser = userDao.findUserByUsername(login.getUsername());
		if(null == appUser || !isPasswordValid) 
		{
			return false;
		}
		return true; 
	}	
}