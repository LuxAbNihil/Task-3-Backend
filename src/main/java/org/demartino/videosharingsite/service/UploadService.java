package org.demartino.videosharingsite.service;

import java.util.List;

import org.demartino.videosharingsite.entity.UploadedVideo;
import org.demartino.videosharingsite.view.Upload;

public interface UploadService {
	Upload createVideo(Upload upload, String username);
	boolean deleteVideoById(Long id);
	Upload findVideoByTitle(String title);
	Upload updateVideo(Upload upload);
	List<Upload> getAllVideosForUser(String username);
}
