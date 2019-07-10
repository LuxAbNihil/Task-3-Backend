package org.demartino.videosharingsite.service;

import org.demartino.videosharingsite.view.Upload;

public interface VideoService {
	public Upload getVideoByTitleAndUsername(String title, String username);
}
