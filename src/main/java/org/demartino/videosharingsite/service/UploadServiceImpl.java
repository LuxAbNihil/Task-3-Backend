package org.demartino.videosharingsite.service;

import java.util.ArrayList;
import java.util.List;

import org.demartino.videosharingsite.dao.VideoDao;
import org.demartino.videosharingsite.entity.UploadedVideo;
import org.demartino.videosharingsite.view.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UploadServiceImpl implements UploadService{

	@Autowired
	private VideoDao videoDao;
	
	public Upload createVideo(Upload upload, String username) {
		//Upload Entity constructor grabs file path 
		UploadedVideo uploadedVideo = new UploadedVideo(upload);
		UploadedVideo returnedUploadedVideo = videoDao.createVideo(uploadedVideo);
		Upload uploadToBeReturned = new Upload(returnedUploadedVideo);
		//Add logic to grab file from path
		return uploadToBeReturned;
	}

	
	public boolean deleteVideoById(Long id) {
		boolean success = videoDao.deleteVideoById(id);
		return success;
	}

	
	public Upload findVideoByTitle(String title) {
		UploadedVideo returnedUploadedVideo = videoDao.findVideoByTitle(title);
		Upload uploadToBeReturned = new Upload(returnedUploadedVideo);
		//add logic to get video from path
		return uploadToBeReturned;
	}


	public Upload updateVideo(Upload upload) {
		UploadedVideo uploadedVideo = new UploadedVideo(upload);
		UploadedVideo returnedUploadedVideo = videoDao.updateVideo(uploadedVideo);
		Upload uploadToBeReturned = new Upload(returnedUploadedVideo);	
		return uploadToBeReturned;
	}
	
	public List<Upload> getAllVideosForUser(String username) {
		List<UploadedVideo> uploadedVideos = videoDao.getAllVideosForUser(username);
		List<Upload> uploads = new ArrayList<Upload>();
		for(int i = 0; i < uploadedVideos.size(); i++)
		{
			Upload upload = new Upload(uploadedVideos.get(i));
			uploads.add(upload);			
		}
		return uploads;
	}

}
