package org.demartino.videosharingsite.httpinterceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.demartino.videosharingsite.controller.Controller;
import org.demartino.videosharingsite.jwt.JwtOperations;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired 
	private JwtOperations jwtOperations;

	private static Logger logger = Logger.getLogger(AuthenticationInterceptor.class);
	/**
	 * This method is called whenever an HTTP request is received
	 * and calls verifyJws to determine whether a given token is valid 
	 * or not.
	 * @throws IOException 
	 */
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

		
		logger.debug("request URI is: " + request.getRequestURI());
		logger.debug("request method is: " + request.getMethod());
		if("POST".equals(request.getMethod()) && "/videosharebe/user/".equals(request.getRequestURI())) {
			return true;
		}
		if("GET".equals(request.getMethod()) && "/videosharebe/upload/".equals(request.getRequestURI())) {
			return true;
		}
		if("OPTIONS".equals(request.getMethod())) {
			return true;
		}
		String token = request.getHeader("Authorization");
		boolean validAuth = jwtOperations.verifyJws(token);
		return validAuth;
	}
}
