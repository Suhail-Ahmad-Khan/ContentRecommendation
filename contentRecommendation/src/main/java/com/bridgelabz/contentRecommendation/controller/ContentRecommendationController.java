package com.bridgelabz.contentRecommendation.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.bridgelabz.contentRecommendation.model.ContentRecommendationModel;
import com.bridgelabz.contentRecommendation.service.ContentRecommendationServiceInterface;
import com.bridgelabz.framework.utility.ContentRecommendationUtility;
import com.google.gson.stream.JsonReader;

@RestController
public class ContentRecommendationController {

	@Autowired
	ContentRecommendationServiceInterface contentRecommendationServiceInterface;

	// This method is called at application startup
	@EventListener
	public void onStartUp(ContextRefreshedEvent event) {
		FileReader fileReader;
		String[] entryData;
		String firstColumnEntry = "visitor_id";
		JsonReader jsonReader;
		int i;
		try {
			HashMap<String, String> hashMap = new HashMap<>();
			jsonReader = new JsonReader(
					new FileReader("/home/bridgelabz/ContentRecommendation/contentRecommendation/src/main/resources/dataFile/abc.json"));
			jsonReader.beginObject();
			while (jsonReader.hasNext()) {
				hashMap.put(jsonReader.nextName(), jsonReader.nextString());
			}
			fileReader = new FileReader("/home/bridgelabz/contentDb.csv");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String entry;
			entry = bufferedReader.readLine();
			entryData = entry.split("\\,");
			for (i = 0; i < entryData.length; i++) {
			}
			while (entry != null) {
				entryData = entry.split("\\,");
				for (i = 0; i < entryData.length; i++) {
					entryData[i] = entryData[i].replace("\"", "");
				}
				if (!(entryData[0].equals(firstColumnEntry))) {
					ContentRecommendationModel contentRecommendationModel = new ContentRecommendationModel(entryData[0],
							entryData[1], entryData[2], entryData[3], entryData[4], entryData[5],
							hashMap.get(entryData[3]));
					contentRecommendationServiceInterface.addVisitor(contentRecommendationModel);
					contentRecommendationServiceInterface.addToDao(contentRecommendationModel);
				}
				entry = bufferedReader.readLine();
			}
			contentRecommendationServiceInterface.createContentIDMap();
			jsonReader.close();
			bufferedReader.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/getContentID", headers = "Accept=application/json", method = RequestMethod.POST)
	public ModelAndView getSuggestionByJson(@RequestBody String content) {
		ContentRecommendationUtility contentRecommendationUtility = new ContentRecommendationUtility();
		ContentRecommendationModel contentRecommendationModel = contentRecommendationUtility.fromJson(content);
		contentRecommendationServiceInterface.addToDao(contentRecommendationModel);

		List<ContentRecommendationModel> suggestionList = new ArrayList<ContentRecommendationModel>();
		contentRecommendationServiceInterface.addVisitor(contentRecommendationModel);
		contentRecommendationServiceInterface.addToContentMap(contentRecommendationModel);
		Set<String> suggestionSet = contentRecommendationServiceInterface.getSuggestion(contentRecommendationModel);
		Iterator<String> suggestionSetIterator = suggestionSet.iterator();
		while (suggestionSetIterator.hasNext()) {
			suggestionList.add(contentRecommendationServiceInterface.getbyContentID(suggestionSetIterator.next()));
		}

		suggestionList.forEach(x -> System.out.print(" " + x.getContentId() + " " + x.getCategoryName()));
		return new ModelAndView("suggestionPage", "suggestionList", suggestionList);
	}

	@RequestMapping(value = "/getContentName", method = RequestMethod.POST)
	public ModelAndView getSuggestionByString(@ModelAttribute("contentName") String contentName) {
		ContentRecommendationModel record = contentRecommendationServiceInterface.getbyContentName(contentName);
		
		List<ContentRecommendationModel> suggestionList = new ArrayList<ContentRecommendationModel>();
		if (record != null) {
			Set<String> suggestionSet = contentRecommendationServiceInterface.getSuggestion(record);
			Iterator<String> suggestionSetIterator = suggestionSet.iterator();
			while (suggestionSetIterator.hasNext()) {
				suggestionList.add(contentRecommendationServiceInterface.getbyContentID(suggestionSetIterator.next()));
			}
		}
		return new ModelAndView("suggestionPage", "suggestionList", suggestionList);
	}

}
