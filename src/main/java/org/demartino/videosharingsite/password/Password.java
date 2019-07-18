package org.demartino.videosharingsite.password;

import java.security.SecureRandom;

import org.springframework.security.crypto.bcrypt.BCrypt;
// code from com.worscipe.bright.ideas.auth
public class Password {
	
	public static final Integer ROUNDS = 12;
	
	public static String hash(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt(ROUNDS));
	}
	
	public static boolean validate(String password, String hashedPassword) {
		return BCrypt.checkpw(password, hashedPassword);
	}
}
