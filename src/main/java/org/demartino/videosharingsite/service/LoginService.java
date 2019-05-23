package org.demartino.videosharingsite.service;

import org.demartino.videosharingsite.view.Login;
import org.demartino.videosharingsite.view.UserAndVideoListContainer;

public interface LoginService {
	UserAndVideoListContainer login(Login login);
}
