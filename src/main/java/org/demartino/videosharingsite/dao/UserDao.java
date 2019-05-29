package org.demartino.videosharingsite.dao;

import java.util.List;

import org.demartino.videosharingsite.entity.AppUser;
import org.demartino.videosharingsite.view.Login;

public interface UserDao {
	AppUser createUser(AppUser userEntity);
	boolean deleteUserByUsername(String username);
	AppUser findUserByUsername(String username);
	AppUser updateUser(AppUser user);
	List<AppUser> getAllUsers();
	AppUser isValidLogin(Login login);
}
