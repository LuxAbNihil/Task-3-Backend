package org.demartino.videosharingsite.view;

import org.demartino.videosharingsite.entity.UploadedVideo;

public class Upload  {
	private Long id;
	private String title;
	private String path;
	
	public Upload() {};
	
	public Upload(UploadedVideo uploadedVideo)
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
	

	@Override
	public String toString() {
		return "Upload [id=" + id + ", title=" + title + "]";
	}

	

}

