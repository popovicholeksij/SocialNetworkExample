package com.socialnetwork.entities;

public class Album {
        public static final int ALBUM_PHOTOS_COUNT_PER_PAGE = 3;
	private int id;
	private int ownerId;
	private long creationDate;
	private int photosCount;
	private String title;
	private String formatedDate;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	
	public long getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
	
	public int getPhotosCount() {
		return photosCount;
	}
	
	public void setPhotosCount(int photosCount) {
		this.photosCount = photosCount;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getFormatedDate() {
		return formatedDate;
	}
	
	public void setFormatedDate(String formatedDate) {
		this.formatedDate = formatedDate;
	}
}
