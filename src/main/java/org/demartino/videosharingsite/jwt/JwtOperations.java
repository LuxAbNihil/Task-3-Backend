package org.demartino.videosharingsite.jwt;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;



@Configuration
public class JwtOperations {
	
	private Key key;
	
	@PostConstruct
	public Key createKey() {
	    key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		return key;
	}
	
	public String createJws(String username) {
		//LocalDateTime expirationTime = LocalDateTime.now().plusHours(2L);
		Calendar expirationTime = Calendar.getInstance();
		expirationTime.setTime(new Date());
		expirationTime.add(Calendar.HOUR_OF_DAY, 2);
		Date expTime = expirationTime.getTime();
		return Jwts.builder().setSubject(username).setExpiration(expTime).signWith(SignatureAlgorithm.HS256, key).compact();
	}
	
	public boolean verifyJws(String token) {
		try {
			Jwts.parser().setSigningKey(key).parseClaimsJws(token);
			return true;
		} catch(JwtException e) {
			return false;
		}
	}
}
