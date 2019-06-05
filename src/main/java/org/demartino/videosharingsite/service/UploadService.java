package org.demartino.videosharingsite.service;

import java.util.List;

import org.demartino.videosharingsite.remote.UploadRemote;
import org.demartino.videosharingsite.view.Upload;

public interface UploadService {
	UploadRemote createVideo(UploadRemote uploadRemote, String username);
	boolean deleteVideoById(Long id);
	UploadRemote findVideoByTitle(String title);
	UploadRemote updateVideo(UploadRemote uploadRemote);
	List<Upload> getAllVideosForUser(String username);
}
