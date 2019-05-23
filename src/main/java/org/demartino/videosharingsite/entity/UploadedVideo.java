package org.demartino.videosharingsite.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.demartino.videosharingsite.view.Upload;

@Entity
@Table(name="UPLOADEDVIDEO") 
//Class Name Should reflect TableName
//best practice is to have underscore between words
public class UploadedVideo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="upload_id")
	private Long id;
	@Column(name="title", nullable=false)
	private String title;
	@Column(name="path", nullable=false)
	private String path;
	@Column(name="username", nullable=false)
	private String username;
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private AppUser userEntity;
	
	public UploadedVideo() {}

	public UploadedVideo(Upload upload)
	{
		this.title = upload.getTitle();
		this.path = null;	
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	} 
	
	
}
 