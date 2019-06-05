package org.demartino.videosharingsite.service;

import java.util.ArrayList;
import java.util.List;

import org.demartino.videosharingsite.dao.VideoDao;
import org.demartino.videosharingsite.entity.UploadedVideo;
import org.demartino.videosharingsite.remote.UploadRemote;
import org.demartino.videosharingsite.view.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UploadServiceImpl implements UploadService{

	@Autowired
	private VideoDao videoDao;
	
	public UploadRemote createVideo(UploadRemote uploadRemote, String username) {
		//Upload Entity constructor grabs file path 
		if(uploadRemote == null || videoDao.findVideoByTitle(uploadRemote.getTitle()) != null)
		{
			return null; //need to modify logic so videos can have same title if uploaded by different users
		}
		UploadedVideo uploadedVideo = new UploadedVideo(uploadRemote);
		UploadedVideo returnedUploadedVideo = videoDao.createVideo(uploadedVideo);
		UploadRemote uploadToBeReturned = new UploadRemote(returnedUploadedVideo);
		return uploadToBeReturned;
	}

	
	public boolean deleteVideoById(Long id) {
		if(id == null)
		{
			return false;
		}
		boolean success = videoDao.deleteVideoById(id);
		return success;
	}

	
	public UploadRemote findVideoByTitle(String title) {
		if(title == null) {
			return null;
		}
		UploadedVideo returnedUploadedVideo = videoDao.findVideoByTitle(title);
		if(returnedUploadedVideo == null) {
			return null;
		}
		UploadRemote uploadToBeReturned = new UploadRemote(returnedUploadedVideo);
		return uploadToBeReturned;
	}


	public UploadRemote updateVideo(UploadRemote uploadRemote) {
		if(uploadRemote == null) {
			return null;
		}
		UploadedVideo uploadedVideo = videoDao.findVideoByTitle(uploadRemote.getTitle());
		if(uploadedVideo == null)
		{
			return null;
		}
		UploadedVideo returnedUploadedVideo = videoDao.updateVideo(uploadedVideo);
		UploadRemote uploadToBeReturned = new UploadRemote(returnedUploadedVideo);	
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
