package org.demartino.videosharingsite.controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.demartino.videosharingsite.service.VideoServiceImpl;
import org.demartino.videosharingsite.view.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.net.HttpHeaders;
import com.google.common.primitives.Longs;

@RestController
@CrossOrigin(origins="http://localhost:4200", methods= {RequestMethod.OPTIONS, RequestMethod.GET}, allowedHeaders="*")
@RequestMapping("/video/")
public class VideoController {
	
	@Autowired
	private VideoServiceImpl videoService;
	
	private final static Logger logger = LogManager.getLogger(VideoController.class);
	
	@RequestMapping(value="/{username}/{title}", method=RequestMethod.GET)
	public ResponseEntity<InputStream> streamVideo(@PathVariable("username") String username,
			@PathVariable("title") String title, @RequestHeader(value="Range",
			required=false) String range) throws IOException {
		
		logger.debug("In streamVideo method");
		
		Upload upload = videoService.getVideoByTitleAndUsername(title, username);
		
		File file = new File(upload.getPath());
		String fullFilePath = upload.getPath();
		String extension = null;
		String[] filePathComponents = fullFilePath.split("\\\\");
		
		//looks for last element in array which will be the title and 
		//extension of the video that was uploaded. It then extracts
		//the file name and discards it. 
		for(int i = 0; i < filePathComponents.length; i++) {
			if(i == filePathComponents.length - 1) {
				String[] fileNameAndExtension = filePathComponents[i].split("[.]");
				extension = fileNameAndExtension[1];
			}
		}
		
		long rangeStart = Longs.tryParse(range.replace("bytes=","").split("-")[0]);
		long rangeEnd = Longs.tryParse(range.replace("bytes=","").split("-")[0]);
		
		InputStream inputStream = FileUtils.openInputStream(file);
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		
		headers.add(HttpHeaders.CONTENT_TYPE, "video/" + extension);
		headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
		headers.add(HttpHeaders.EXPIRES, "0");
		headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");
		headers.add(HttpHeaders.CONNECTION, "Keep-alive");
		headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(rangeEnd-rangeStart));
		
		return new ResponseEntity<InputStream>(inputStream, headers, HttpStatus.OK);
	}
	
}
