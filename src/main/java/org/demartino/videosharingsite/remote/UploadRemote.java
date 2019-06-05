package org.demartino.videosharingsite.remote;

import org.demartino.videosharingsite.entity.UploadedVideo;

public class UploadRemote {
	private Long id;
	private String title;
	private String path;
	
	
	public UploadRemote() {};
	
	public UploadRemote(UploadedVideo uploadedVideo)
	{
		this.title = uploadedVideo.getTitle();
		this.id = uploadedVideo.getId();
		this.path = uploadedVideo.getPath();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
