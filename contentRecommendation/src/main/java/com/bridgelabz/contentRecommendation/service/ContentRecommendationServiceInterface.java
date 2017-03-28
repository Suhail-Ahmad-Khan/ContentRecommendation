package com.bridgelabz.contentRecommendation.service;

import java.util.Set;

import com.bridgelabz.contentRecommendation.model.ContentRecommendationModel;

public interface ContentRecommendationServiceInterface {

	public void addVisitor(ContentRecommendationModel contentRecommendationModel);

	public void createContentIDMap();

	public Set<String> getSuggestion(ContentRecommendationModel contentRecommendationModel);

	public void addToContentMap(ContentRecommendationModel contentRecommendationModel);

	public void addToDao(ContentRecommendationModel contentRecommendationModel);

	public ContentRecommendationModel getbyContentID(String contentId);

	public ContentRecommendationModel getbyContentName(String contentName);

}
