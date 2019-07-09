package org.demartino.videosharingsite.service;

import java.util.List;

import org.demartino.videosharingsite.view.User;
import org.demartino.videosharingsite.view.UserAndVideoListContainer;

public interface UserService {
	User createUser(User user);
	boolean deleteUserByUsername(String username);
	User findUserByUsername(String username);
	User updateUser(User user);
	List<User> getAllUsers();
	UserAndVideoListContainer getUserAndVideoListContainer(String username);

}
