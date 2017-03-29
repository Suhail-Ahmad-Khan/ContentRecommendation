package com.bridgelabz.framework.utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.bridgelabz.contentRecommendation.model.ContentRecommendationModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class ContentRecommendationUtility {

	Gson obj = new Gson();

	public String toJson(Set<String> contentIDSet) {
		return obj.toJson(contentIDSet);
	}

	public Set<String> toSet(String contentIDString) {
		return obj.fromJson(contentIDString, new TypeToken<Set<String>>() {
		}.getType());
	}

	public List<String> toList(String contentIDString) {
		return obj.fromJson(contentIDString, new TypeToken<List<String>>() {
		}.getType());
	}

	public ContentRecommendationModel toModel(String visitorIDString) {
		return obj.fromJson(visitorIDString, ContentRecommendationModel.class);
	}

	public String createContent(String contentName, String categoryName) {
		JsonObject jObj = new JsonObject();
		jObj.addProperty("contentName", contentName);
		jObj.addProperty("categoryName", categoryName);
		return jObj.toString();
	}

	public ContentRecommendationModel fromJson(String str) {
		return obj.fromJson(str, ContentRecommendationModel.class);
	}

	public Properties getProperties() {

		Properties prop = new Properties();
		try {

			String propFileName = "/home/bridgelabz/ContentRecommendation/contentRecommendation/src/main/resources/propertiesFile/resource.properties";
			// passing propFileName to the FileInputStream class object
			FileInputStream fis;

			fis = new FileInputStream(propFileName);
			if (fis != null) {
				// calling load method of Properties class
				prop.load(fis);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prop;
	}

}
