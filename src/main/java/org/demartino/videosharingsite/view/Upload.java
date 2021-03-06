package org.demartino.videosharingsite.view;

import org.demartino.videosharingsite.entity.UploadedVideo;

public class Upload  {
	private Long id;
	private String title;
	private String path;
	private String username;
	
	public Upload() {};
	
	public Upload(UploadedVideo uploadedVideo)
	{
		this.title = uploadedVideo.getTitle();
		this.id = uploadedVideo.getId();
		this.path = uploadedVideo.getPath();
		this.username = uploadedVideo.getUsername();
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
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "Upload [id=" + id + ", title=" + title + "]";
	}

	

}

