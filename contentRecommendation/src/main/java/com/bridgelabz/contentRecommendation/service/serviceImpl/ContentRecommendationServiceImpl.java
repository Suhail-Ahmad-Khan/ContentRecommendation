package com.bridgelabz.contentRecommendation.service.serviceImpl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.contentRecommendation.dao.ContentRecommendationDaoInterface;
import com.bridgelabz.contentRecommendation.dao.ContentRecommendationRedisInterface;
import com.bridgelabz.contentRecommendation.model.ContentRecommendationModel;
import com.bridgelabz.contentRecommendation.service.ContentRecommendationServiceInterface;

@Service
public class ContentRecommendationServiceImpl implements ContentRecommendationServiceInterface{
	
	@Autowired
	ContentRecommendationRedisInterface contentRecommendationRedisInterface;
	
	@Autowired
	ContentRecommendationDaoInterface contentRecommendationDaoInterface;
	
	public void addVisitor(ContentRecommendationModel contentRecommendationModel) {
		contentRecommendationRedisInterface.addVisitorView(contentRecommendationModel);
		contentRecommendationRedisInterface.addVisitorDownload(contentRecommendationModel);
		contentRecommendationRedisInterface.addContentID(contentRecommendationModel);
	}
	
	public void createContentIDMap(){
		contentRecommendationRedisInterface.createContentIDMap();
	}
	
	public Set<String> getSuggestion(ContentRecommendationModel contentRecommendationModel){
		return contentRecommendationRedisInterface.getSuggestion(contentRecommendationModel.getContentId(), contentRecommendationModel.getVisitorId());
		
	}
	public void addToContentMap(ContentRecommendationModel contentRecommendationModel){
		contentRecommendationRedisInterface.addToContentMap(contentRecommendationModel);
	}
	
	public void addToDao(ContentRecommendationModel contentRecommendationModel){
		contentRecommendationDaoInterface.addRecord(contentRecommendationModel);
	}
	
	public ContentRecommendationModel getbyContentID(String contentId){
		return contentRecommendationDaoInterface.getByContentID(contentId);
	}
	
	public ContentRecommendationModel getbyContentName(String contentName) {
		return contentRecommendationDaoInterface.getByContentName(contentName);
		
	}

}
