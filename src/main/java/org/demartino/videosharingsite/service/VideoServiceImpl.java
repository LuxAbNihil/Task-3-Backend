package org.demartino.videosharingsite.service;

import java.util.List;

import org.demartino.videosharingsite.dao.VideoDao;
import org.demartino.videosharingsite.entity.UploadedVideo;
import org.demartino.videosharingsite.view.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VideoServiceImpl {
	
	@Autowired
	private VideoDao videoDao;
	
	public Upload getVideoByTitleAndUsername(String title, String username) {
		List<UploadedVideo> uploadedVideos = videoDao.getVideosByTitle(title);
		Upload upload = null;
		for(UploadedVideo uploadedVideo: uploadedVideos) {
			if(uploadedVideo.getUsername().equals(username)) {
				upload = new Upload(uploadedVideo);
				return upload;
			}
		}
		return upload;
	}
}
