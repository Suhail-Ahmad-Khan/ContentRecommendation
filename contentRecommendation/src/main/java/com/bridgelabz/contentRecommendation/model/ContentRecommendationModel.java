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
	@Column(name = "contentId")
	private String contentId;

	@Transient
	private String visitorId;

	@Column(name = "contentName")
	private String contentName;

	@Column(name = "categoryName")
	private String categoryName;

	@Transient
	private String view;

	@Transient
	private String download;

	@Column(name = "imageUrl")
	private String imageUrl;

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getVisitorId() {
		return visitorId;
	}

	public void setVisitorId(String visitorId) {
		this.visitorId = visitorId;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
