package org.demartino.videosharingsite.controller;

import org.demartino.videosharingsite.remote.UploadRemote;
import org.demartino.videosharingsite.service.UploadService;
import org.demartino.videosharingsite.view.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/upload/")
@RestController
public class UploadController {	
	
	
	@Autowired
	private UploadService uploadService;
	/**
	 * Uploads a video to the file system and creates a path field to that file in the database. (Will in final form, not completely implemented yet.)
	 * @param upload : Contains the upload information for the video
	 * @param username : The username of the account uploading the video
	 * @return A ResponseEntity<Upload> where the Upload is the video that was added to the file system and database.
	 */

	@RequestMapping(value="/{username}", method=RequestMethod.POST)
	public ResponseEntity<UploadRemote> uploadVideo(@RequestBody UploadRemote uploadRemote, @PathVariable("username") String username) {
		UploadRemote uploadToBeReturned = uploadService.createVideo(uploadRemote, username);
		return new ResponseEntity<UploadRemote>(uploadToBeReturned, HttpStatus.OK);
	}
	
	/**
	 * Edits a video identified by username
	 * @param upload : The video to be updated
	 * @param username : The username of the account the video belongs to. 
	 * @return A ResponseEntity<Upload> where the Upload is the video that was edited. 
	 */
	@RequestMapping(value="/{username}", method=RequestMethod.PUT)
	public ResponseEntity<UploadRemote> editVideo(@RequestBody UploadRemote uploadRemote, @PathVariable("username") String username) {
		UploadRemote uploadToBeReturned = uploadService.updateVideo(uploadRemote);
		return new ResponseEntity<UploadRemote>(uploadToBeReturned, HttpStatus.OK);
	}
}