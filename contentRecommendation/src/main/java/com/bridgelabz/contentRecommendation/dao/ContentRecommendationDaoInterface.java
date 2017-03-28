package com.bridgelabz.contentRecommendation.dao;

import org.springframework.stereotype.Repository;

import com.bridgelabz.contentRecommendation.model.ContentRecommendationModel;

@Repository
public interface ContentRecommendationDaoInterface {

	public void addRecord(ContentRecommendationModel contentRecommendationModel);

	public ContentRecommendationModel getByContentID(String ContentId);

	public ContentRecommendationModel getByContentName(String ContentName);

}
