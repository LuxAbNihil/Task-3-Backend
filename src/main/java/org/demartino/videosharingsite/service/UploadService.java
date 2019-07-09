package org.demartino.videosharingsite.service;

import java.io.FileNotFoundException;
import java.util.List;

import org.demartino.videosharingsite.remote.UploadRemote;
import org.demartino.videosharingsite.view.Upload;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface UploadService {
	Upload createVideo(MultipartHttpServletRequest request, String username) throws FileNotFoundException;
	boolean deleteVideoById(Long id);
	Upload getVideoByTitle(String title, String username);
	List<Upload> getVideosByTitle(String title);
	UploadRemote updateVideo(UploadRemote uploadRemote);
	List<Upload> getAllVideosForUser(String username);
}
