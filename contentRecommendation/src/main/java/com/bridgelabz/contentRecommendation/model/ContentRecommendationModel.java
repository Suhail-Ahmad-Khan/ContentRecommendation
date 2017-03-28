package com.bridgelabz.contentRecommendation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ContentRecommendationData")
public class ContentRecommendationModel {

	public ContentRecommendationModel() {

	}

	public ContentRecommendationModel(String visitorId, String contentId, String contentName, String categoryName,
			String view, String download, String imageUrl) {
		this.setContentId(contentId);
		this.setCategoryName(categoryName);
		this.setContentName(contentName);
		this.setVisitorId(visitorId);
		this.setView(view);
		this.setDownload(download);
		this.setImageUrl(imageUrl);
	}

	@Id
	@Column(name = "ContentId")
	private String ContentId;

	@Transient
	private String VisitorId;

	@Column(name = "ContentName")
	private String ContentName;

	@Column(name = "CategoryName")
	private String CategoryName;

	@Transient
	private String View;

	@Transient
	private String Download;

	@Column(name = "ImageUrl")
	private String ImageUrl;

	public String getContentId() {
		return ContentId;
	}

	public void setContentId(String contentId) {
		this.ContentId = contentId;
	}

	public String getVisitorId() {
		return VisitorId;
	}

	public void setVisitorId(String visitorId) {
		this.VisitorId = visitorId;
	}

	public String getContentName() {
		return ContentName;
	}

	public void setContentName(String contentName) {
		this.ContentName = contentName;
	}

	public String getCategoryName() {
		return CategoryName;
	}

	public void setCategoryName(String categoryName) {
		this.CategoryName = categoryName;
	}

	public String getView() {
		return View;
	}

	public void setView(String view) {
		this.View = view;
	}

	public String getDownload() {
		return Download;
	}

	public void setDownload(String download) {
		this.Download = download;
	}

	public String getImageUrl() {
		return ImageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.ImageUrl = imageUrl;
	}

}
