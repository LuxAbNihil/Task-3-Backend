package org.demartino.videosharingsite.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.demartino.videosharingsite.remote.UploadRemote;
import org.demartino.videosharingsite.service.UploadService;
import org.demartino.videosharingsite.view.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@CrossOrigin(origins="http://localhost:4200", methods= {RequestMethod.OPTIONS, RequestMethod.POST}, allowedHeaders = "*")
@RequestMapping(value="/upload/")
public class UploadController {	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private UploadService uploadService;
	
	private final static Logger logger = LogManager.getLogger(UploadController.class);
	
	
	/**
	 * Uploads a video to the file system and creates a path field to that file in the database. (Will in final form, not completely implemented yet.)
	 * @param request : Contains the upload information for the video
	 * @param response: 
	 * @param username : The username of the account uploading the video
	 * @return A ResponseEntity<Upload> where the Upload is the video that was added to the file system and database.
	 */

	@RequestMapping(value="/{username}", method=RequestMethod.POST)
	public ResponseEntity<Upload> uploadVideo(MultipartHttpServletRequest request,
		HttpServletResponse response, @PathVariable("username") String username) throws IOException  {
		Upload upload = uploadService.createVideo(request, username);
		if(upload == null) {
			return new ResponseEntity<Upload>(upload, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Upload>(upload, HttpStatus.OK);
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
	} //The above method needs to be edited to remove dependency on UploadRemote which is to be removed. 
	
	
	/**
	 * Grabs a list of videos with titles matching the specified search term
	 * @param search : The term to be matched with the titles of the videos in the db
	 * @return A ResponseEntity<List<Upload>> that contains the videos with a title matching the 
	 * 	search term. 
	 */
	@RequestMapping(value="/{search}", method=RequestMethod.GET)
	public ResponseEntity<List<Upload>> getSearchResultsByTitle(@PathVariable String search) {
		logger.debug("In getSearchResultsByTitle");
		List<Upload> videos = uploadService.getVideosByTitle(search);
		return new ResponseEntity<List<Upload>>(videos, HttpStatus.OK);
	}
}