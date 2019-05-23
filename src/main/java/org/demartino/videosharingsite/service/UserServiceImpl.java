package org.demartino.videosharingsite.service;

import java.util.ArrayList;
import java.util.List;

import org.demartino.videosharingsite.dao.UserDao;
import org.demartino.videosharingsite.entity.AppUser;
import org.demartino.videosharingsite.view.User;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	public User createUser(User user) {
		AppUser appUser = new AppUser(user);
		if(userDao.findUserByUsername(user.getUsername()) != null)
		{
			return null;
		}
		AppUser returnedAppUser = userDao.createUser(appUser);
		User returnedUser = new User(returnedAppUser);
		return returnedUser;
	}
	
	public boolean deleteUserByUsername(String username) {
		boolean successful = userDao.deleteUserByUsername(username);
		return successful;
	}

	public User findUserByUsername(String username) {
		AppUser userEntity = userDao.findUserByUsername(username);
		User returnedUser = new User(userEntity);
		return returnedUser;
	}

	public User updateUser(User user, String username) {
		System.out.println("In updateUser service method");
		AppUser returnedUserEntity = new AppUser();
		AppUser appUser = new AppUser(user);
		try {
			returnedUserEntity = userDao.updateUser(appUser);
		} catch (HibernateException he){
			return null; 
		}
		
		User returnedUser = new User(returnedUserEntity);
		return returnedUser;
	}
	
	public List<User> getAllUsers()
	{
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
	

	

}
