package org.demartino.videosharingsite.view;

import java.util.List;

public class UserAndVideoListContainer {
	private List<User> users;
	private List<Upload> videos;
	
	public UserAndVideoListContainer() {}
	
	public UserAndVideoListContainer(List<User> users, List<Upload> uploads) {
		this.users = users;
		this.videos = uploads;
	}
	
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public List<Upload> getVideos() {
		return videos;
	}
	public void setVideos(List<Upload> videos) {
		this.videos = videos;
	}
	
	
}
