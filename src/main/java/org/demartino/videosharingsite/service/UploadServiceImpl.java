package org.demartino.videosharingsite.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.demartino.videosharingsite.dao.UserDao;
import org.demartino.videosharingsite.dao.VideoDao;
import org.demartino.videosharingsite.entity.AppUser;
import org.demartino.videosharingsite.entity.UploadedVideo;
import org.demartino.videosharingsite.remote.UploadRemote;
import org.demartino.videosharingsite.view.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional
public class UploadServiceImpl implements UploadService{

	@Autowired
	private VideoDao videoDao;
	
	@Autowired 
	private UserDao userDao;
	
	@Value("${upload_directory}")
	private String fileDirectory;
	
	/**
	 * Creates a video entry in the database and stores the uploaded video
	 * onto a directory in the file system. The directory structure is 
	 * Uploads/{username}/{file name}.
	 *
	 */
	public Upload createVideo(MultipartHttpServletRequest request, String username) throws FileNotFoundException {
		Upload upload = null;
		String userDirectory = username;
		Iterator<String> iterator = request.getFileNames();
		MultipartFile multipartFile = null;
		String fullFilePath = null;
		File file = new File(fileDirectory + "\\" + userDirectory);
		
		//creates a directory for the user who uploaded the video
		//if one does not already exist.
		if(!file.exists()) {
			file.mkdir();
		}
		
		//This loop grabs the uploaded file from the request and 
		//stores it on the file system.
		while(iterator.hasNext()) {
			multipartFile = request.getFile(iterator.next());
			fullFilePath = fileDirectory + "\\" + userDirectory + "\\" + multipartFile.getOriginalFilename().replace(" ", "-");
			
			try {
				FileOutputStream outputStream = new FileOutputStream(fullFilePath);
				byte[] videoBytes = multipartFile.getBytes();
				outputStream.write(videoBytes);
				outputStream.close();
			} catch(IOException ioe) {
				ioe.getStackTrace();
				return null;
			}
		
			String[] filePathComponents = fullFilePath.split("\\\\");
			String videoTitle = null;
			
			//looks for last element in array which will be the title of the 
			//video that was uploaded. It then extracts the file extension and 
			//discards it. 
			for(int i = 0; i < filePathComponents.length; i++) {
				if(i == filePathComponents.length - 1) {
					String[] fileNameAndExtension = filePathComponents[i].split("[.]");
					videoTitle = fileNameAndExtension[0];
				}
			}
			
			AppUser user = userDao.findUserByUsername(username);
			
			if(videoDao.getVideoByTitle(videoTitle, username) == null) {
				UploadedVideo uploadedVideo = new UploadedVideo();
				uploadedVideo.setPath(fullFilePath);
				uploadedVideo.setTitle(videoTitle);
				uploadedVideo.setUsername(username);
				uploadedVideo.setUserEntity(user);
				uploadedVideo = videoDao.createVideo(uploadedVideo);
				upload = new Upload(uploadedVideo);
			}
			
		}
		return upload;
	}
	
	//All methods below here are to be moved into VideoServiceImpl
	
	public boolean deleteVideoById(Long id) {
		if(id == null)
		{
			return false;
		}
		boolean success = videoDao.deleteVideoById(id);
		return success;
	}
	/**
	 * Gets a list of videos with titles matching the search term or 
	 * 	similar to the search term. 
	 * @param title: The term with which to match video titles 
	 * @return A List<Upload> containing the title and id of the video entry 
	 * 	that matches the term. 
	 */
	public List<Upload> getVideosByTitle (String title) {
		if (title == null) {
			return null;
		}
		List<UploadedVideo> returnedUploadedVideos = videoDao.getVideosByTitle(title);
		if (returnedUploadedVideos.size() == 0) {
			return null;
		}
		List<Upload> uploads = new ArrayList<Upload>();
		for(UploadedVideo uploadedVideo: returnedUploadedVideos) {
			Upload upload = new Upload(uploadedVideo);
			uploads.add(upload);
		}
		return uploads;
	}
	
	public UploadRemote updateVideo(UploadRemote uploadRemote) {
		if(uploadRemote == null) {
			return null;
		}
		String username = null;
		UploadedVideo uploadedVideo = videoDao.getVideoByTitle(uploadRemote.getTitle(), username);
		if(uploadedVideo == null)
		{
			return null;
		}
		UploadedVideo returnedUploadedVideo = videoDao.updateVideo(uploadedVideo);
		UploadRemote uploadToBeReturned = new UploadRemote(returnedUploadedVideo);	
		return uploadToBeReturned;
	}
	
	public Upload getVideoByTitle(String title, String username) {
		UploadedVideo uploadedVideo = videoDao.getVideoByTitle(title, username);
		Upload upload = new Upload(uploadedVideo);
		return upload;
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
