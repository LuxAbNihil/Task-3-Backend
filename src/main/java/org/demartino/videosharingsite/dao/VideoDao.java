package org.demartino.videosharingsite.dao;

import java.util.List;

import org.demartino.videosharingsite.entity.UploadedVideo;

public interface VideoDao {
	UploadedVideo createVideo(UploadedVideo upload);
	boolean deleteVideoById(Long id);
	UploadedVideo findVideoByTitle(String title);
	UploadedVideo updateVideo(UploadedVideo upload); //UploadEntity converted from upload in Service layer
	List<UploadedVideo> getAllVideosForUser(String username);
}
