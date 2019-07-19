package org.demartino.videosharingsite.dao;

import java.util.List;

import org.demartino.videosharingsite.entity.AppUser;
import org.demartino.videosharingsite.entity.PasswordResetToken;

public interface UserDao {
	AppUser createUser(AppUser userEntity);
	boolean deleteUserByUsername(String username);
	AppUser findUserByUsername(String username);
	AppUser updateUser(AppUser user);
	List<AppUser> getAllUsers();
	String getPasswordByUsername(String username);
	AppUser getUserByEmail(String email);
	PasswordResetToken createPasswordResetToken (PasswordResetToken passwordResetToken);
	PasswordResetToken getPasswordResetTokenByToken(String token);
	AppUser getUserByUserId(Long id);
}
