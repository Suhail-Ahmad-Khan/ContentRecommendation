package com.bridgelabz.contentRecommendation.dao;

import java.util.Set;

import com.bridgelabz.contentRecommendation.model.ContentRecommendationModel;

public interface ContentRecommendationRedisInterface {
	
public void addVisitorView(ContentRecommendationModel contentRecommendationModel);
	
	public void addVisitorDownload(ContentRecommendationModel contentRecommendationModel);
	
	public void addContentID(ContentRecommendationModel contentRecommendationModel);
	
	public void createContentIDMap();
	
	public void addToContentMap(ContentRecommendationModel contentRecommendationModel);
	
	public Set<String> getSuggestion(String contentId, String visitorId);

}
