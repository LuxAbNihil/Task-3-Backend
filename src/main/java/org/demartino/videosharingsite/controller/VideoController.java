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
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.io.Resources;
import com.google.common.net.MediaType;
import com.google.common.primitives.Longs;

@RestController
@CrossOrigin(origins="http://localhost:4200", methods= {RequestMethod.OPTIONS, RequestMethod.GET}, allowedHeaders="*")
@RequestMapping("/video/")
public class VideoController {
	
	@Autowired
	private VideoServiceImpl videoService;
	
	private final static Logger logger = LogManager.getLogger(VideoController.class);
	
	@RequestMapping(value="/{username}/{title}", method=RequestMethod.GET)
	public ResponseEntity<InputStreamResource> streamVideo(@PathVariable("username") String username,
			@PathVariable("title") String title, @RequestHeader(value="Range",
			required=false) String range) throws IOException {
		
		logger.debug("In streamVideo method");
		logger.debug("Range is: ", range);
		Upload upload = videoService.getVideoByTitleAndUsername(title, username);
		
		File file = new File(upload.getPath());
		String fullFilePath = upload.getPath();
		String extension = null;
		String[] filePathComponents = fullFilePath.split("\\\\");
		long contentLength = file.length();

		//looks for last element in array which will be the title and 
		//extension of the video that was uploaded. It then extracts
		//the file name and discards it. 
		for(int i = 0; i < filePathComponents.length; i++) {
			if(i == filePathComponents.length - 1) {
				String[] fileNameAndExtension = filePathComponents[i].split("[.]");
				extension = fileNameAndExtension[1];
			}
		}
		
		if(range == null) {
			range="bytes=0-";
		}
		long rangeStart = Longs.tryParse(range.replace("bytes=","").split("-")[0]);
		long rangeEnd = Longs.tryParse(range.replace("bytes=","").split("-")[0]);
		
		//InputStream inputStream = FileUtils.openInputStream(file);
		InputStream inputStream = new FileInputStream(file);
		InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
		//MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		HttpHeaders headers = new HttpHeaders();

		headers.set("Content-Type", "video/" + extension);
		headers.set("Accept-Ranges", "bytes");
		headers.set("Cache-Control", "no-cache, no-store");
		headers.set("Connection", "keep-alive");
		headers.set("Content-Transfer-Encoding", "binary");
		headers.set("Content-Length", String.valueOf(contentLength));
		headers.set("Content-Range", String.format("bytes %s-%s/%s", rangeStart, rangeStart, contentLength));
//		headers.set("Content-Type", "video/" + extension);
//		headers.set("Accept-Ranges", "bytes");
//		headers.set("Expires", "0");
//		headers.set("Cahce-Control", "no-cache, no-store");
//		headers.set("Connection", "keep-alive");
//		headers.set("Content-Length", String.valueOf(contentLength - rangeStart));
//		headers.set("Content-Transfer-Encoding", "binary");
//		headers.set("Content-Range", String.format("bytes %s-%s/%s", rangeStart, rangeStart, contentLength));
		
		return new ResponseEntity<InputStreamResource>(inputStreamResource, headers, HttpStatus.OK);
	}
}
