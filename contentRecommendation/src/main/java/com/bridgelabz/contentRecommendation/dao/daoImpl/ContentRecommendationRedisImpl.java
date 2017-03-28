package com.bridgelabz.contentRecommendation.dao.daoImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.bridgelabz.contentRecommendation.dao.ContentRecommendationRedisInterface;
import com.bridgelabz.contentRecommendation.model.ContentRecommendationModel;
import com.bridgelabz.framework.utility.ContentRecommendationUtility;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class ContentRecommendationRedisImpl implements ContentRecommendationRedisInterface{
	
	/* Connection to local redis server */
	final static Jedis redisConnect = new Jedis("localhost");
	static ContentRecommendationUtility contentRecommendationUtility = new ContentRecommendationUtility();
	Properties properties = contentRecommendationUtility.getProperties();
	Logger log = Logger.getLogger(ContentRecommendationRedisImpl.class);

	/*
	 * To check, fetch visitor_view_hashset from redis. If set exist add new
	 * visitor to set otherwise create new set.
	 */
	public void addVisitorView(ContentRecommendationModel contentRecommendationModel) {
		redisConnect.select(3);

		/* create visitor_id_set */
		redisConnect.sadd(properties.getProperty("VISITOR_SET"), contentRecommendationModel.getVisitorId()
				.substring(Integer.parseInt(properties.getProperty("low")), Integer.parseInt(properties.getProperty("high"))));

		/* Fetch visitor_id_hashset from redis */
		String record = redisConnect.hget(
				properties.getProperty("VISITOR_ID_VIEW_SET") + ":" + contentRecommendationModel.getVisitorId().substring(
						Integer.parseInt(properties.getProperty("low")), Integer.parseInt(properties.getProperty("high"))),
				contentRecommendationModel.getVisitorId());

		/* If set exist add new visitor to set otherwise create new set. */
		Set<String> recordSet = new HashSet<String>();
		if (record != null) {
			//System.out.println(record);
			if (Integer.parseInt(contentRecommendationModel.getView()) > 0) {
				recordSet = contentRecommendationUtility.toSet(record);
				addToViewSet(recordSet, contentRecommendationModel.getVisitorId(), contentRecommendationModel.getContentId());
			}

		} else {
			addToViewSet(recordSet, contentRecommendationModel.getVisitorId(), contentRecommendationModel.getContentId());
		}
	}

	/* Add record to visitor_view_hashset */
	private void addToViewSet(Set<String> recordSet, String visitorID, String contentID) {
		String contentIDString = null;
		try {
			recordSet.add(contentID);
		} catch (NullPointerException e) {
			log.debug(e);
			log.info(e);
		}
		contentIDString = contentRecommendationUtility.toJson(recordSet);
		redisConnect.hset(
				properties.getProperty("VISITOR_ID_VIEW_SET") + ":" + visitorID.substring(
						Integer.parseInt(properties.getProperty("low")), Integer.parseInt(properties.getProperty("high"))),
				visitorID, contentIDString);
	}

	/*
	 * To check, fetch visitor_download_set from redis. If set exist add new
	 * visitor to set otherwise create new set.
	 */
	public void addVisitorDownload(ContentRecommendationModel contentRecommendationModel) {
		redisConnect.select(3);
		/* create visitor_id_set */
		redisConnect.sadd(properties.getProperty("VISITOR_SET"), contentRecommendationModel.getVisitorId()
				.substring(Integer.parseInt(properties.getProperty("low")), Integer.parseInt(properties.getProperty("high"))));

		/* Fetch visitor_id_hashset from redis */
		String record = redisConnect.hget(
				properties.getProperty("VISITOR_ID_DOWNLOAD_SET") + ":" + contentRecommendationModel.getVisitorId().substring(
						Integer.parseInt(properties.getProperty("low")), Integer.parseInt(properties.getProperty("high"))),
				contentRecommendationModel.getVisitorId());

		/* If set exist add new visitor to set otherwise create new set. */
		Set<String> recordSet = new HashSet<String>();
		if (record != null) {
			if (Integer.parseInt(contentRecommendationModel.getDownload()) > 0) {
				recordSet = contentRecommendationUtility.toSet(record);
				addToDownloadSet(recordSet, contentRecommendationModel.getVisitorId(), contentRecommendationModel.getContentId());
			}
		} else {
			addToDownloadSet(recordSet, contentRecommendationModel.getVisitorId(), contentRecommendationModel.getContentId());
		}
	}

	/* Add record to visitor_download_set */
	private void addToDownloadSet(Set<String> recordSet, String visitorID, String contentID) {
		String contentIDString = null;
		try {
			recordSet.add(contentID);
		} catch (NullPointerException e) {
			log.debug(e);
			log.info(e);
		}
		contentIDString = contentRecommendationUtility.toJson(recordSet);
		redisConnect.hset(
				properties.getProperty("VISITOR_ID_DOWNLOAD_SET") + ":" + visitorID.substring(
						Integer.parseInt(properties.getProperty("low")), Integer.parseInt(properties.getProperty("high"))),
				visitorID, contentIDString);
	}

	/* To create content_id_set for */
	public void addContentID(ContentRecommendationModel contentRecommendationModel) {
		redisConnect.select(3);
		System.out.println("Content id add : " + contentRecommendationModel.getContentId());

		String contentString = null;

		/* Create json string of content_id information */
		contentString = contentRecommendationUtility.createContent(contentRecommendationModel.getContentName(), contentRecommendationModel.getCategoryName());

		/* Set contentId json string in content_id_hash */
		if (Integer.parseInt(contentRecommendationModel.getContentId()) > 100) {

			redisConnect.hset(
					properties.getProperty("CONTENT_ID_SET") + ":" + contentRecommendationModel.getContentId().substring(
							Integer.parseInt(properties.getProperty("low")), Integer.parseInt(properties.getProperty("high"))),
					contentRecommendationModel.getContentId(), contentString);
		} else {

			redisConnect.hset(properties.getProperty("CONTENT_ID_SET") + ":" + contentRecommendationModel.getContentId(), contentRecommendationModel.getContentId(),
					contentString);
		}
	}

	/* Create content map for recommendation using visitor_views_set */
	public void createContentIDMap() {
		redisConnect.select(3);
		String nextView, nextDownload;
		String[] contentIDViewSetArr = null, contentIDDownloadSetArr = null;
		Set<String> contentIDDownloadSet, contentIDViewSet;
		Iterator<String> mapViewKeySetIterator, mapDownloadKeySetIterator;

		try {
			/* To import the visitor_id hash keys */
			Set<String> visitorSet = redisConnect.smembers(properties.getProperty("VISITOR_SET"));
			String[] visitorSetArray = (String[]) visitorSet.toArray(new String[visitorSet.size()]);
			Arrays.sort(visitorSetArray);
			for (int i = 0; i < visitorSetArray.length; i++) {
				System.out.print(" " + visitorSetArray[i]);
			}

			/**/
			for (int i = 0; i < visitorSetArray.length; i++) {
				System.out.print(" " + visitorSetArray[i]);
				Set<String> mapViewKeySet = getVisitorIDSet(properties.getProperty("VISITOR_ID_VIEW_SET"),
						visitorSetArray[i]);
				Set<String> mapDownloadKeySet = getVisitorIDSet(properties.getProperty("VISITOR_ID_DOWNLOAD_SET"),
						visitorSetArray[i]);

				mapViewKeySetIterator = mapViewKeySet.iterator();
				mapDownloadKeySetIterator = mapDownloadKeySet.iterator();

				while (mapDownloadKeySetIterator.hasNext() || mapViewKeySetIterator.hasNext()) {

					nextView = mapViewKeySetIterator.next();
					nextDownload = mapDownloadKeySetIterator.next();
					contentIDViewSet = getcontentIDArray("VISITOR_ID_VIEW_SET", visitorSetArray[i], nextView);
					contentIDDownloadSet = getcontentIDArray("VISITOR_ID_DOWNLOAD_SET", visitorSetArray[i],
							nextDownload);

					contentIDViewSet.removeAll(contentIDDownloadSet);

					contentIDViewSetArr = (String[]) contentIDViewSet.toArray(new String[contentIDViewSet.size()]);
					contentIDDownloadSetArr = (String[]) contentIDDownloadSet
							.toArray(new String[contentIDDownloadSet.size()]);

					System.out.println("View length : " + contentIDViewSetArr.length + " Download length: "
							+ contentIDDownloadSetArr.length);

					if ((contentIDDownloadSetArr.length) > 1) {

						setContentMap(contentIDDownloadSetArr, properties.getProperty("two"));

					}
					if ((contentIDViewSetArr.length) > 1) {

						setContentMap(contentIDViewSetArr, properties.getProperty("one"));

					}
				}
			}

		} catch (NullPointerException e) {
			log.debug(e);
			log.info(e);
		} catch (JedisConnectionException e) {
			log.debug(e);
			log.info(e);
		}
	}

	private void setContentMap(String[] contentIDSetArr, String value) {
		String contentString = null;

		for (int k = 0; k < contentIDSetArr.length; k++) {
			for (int j = 0; j < contentIDSetArr.length; j++) {
				if (contentIDSetArr[k] != contentIDSetArr[j]) {
					contentString = getContentMap(contentIDSetArr[k], contentIDSetArr[j]);
					if (contentString != null) {
						contentString = String.valueOf((Integer.parseInt(contentString) + 1));
						setContentMapValue(contentIDSetArr[k], contentIDSetArr[j], contentString);
					} else {
						setContentMapValue(contentIDSetArr[k], contentIDSetArr[j], value);
					}
				}
			}
		}
	}

	private Set<String> getVisitorIDSet(String setName, String setKey) {
		/* To import the map of each visitor_id key */
		Map<String, String> visitorMapView = redisConnect.hgetAll(setName + ":" + setKey);
		Set<String> mapKeySet = visitorMapView.keySet();
		return mapKeySet;
	}

	private Set<String> getcontentIDArray(String setName, String setKey, String next) {
		/*
		 * To import json of content_id of view_set for particular visitor_id
		 */
		String contentIDList = redisConnect.hget(properties.getProperty(setName) + ":" + setKey, next);

		/*
		 * To convert json containing content_id of view_set for particular
		 * visitor_id to set and return it.
		 */
		return contentRecommendationUtility.toSet(contentIDList);
	}

	/* Add to content_map */
	private void setContentMapValue(String visitorID1, String visitorID2, String contentString) {
		if (Integer.parseInt(visitorID1) > 100) {
			redisConnect.hset(
					properties.getProperty("CONTENT_MAP") + ":"
							+ visitorID1.substring(Integer.parseInt(properties.getProperty("low")),
									Integer.parseInt(properties.getProperty("high"))),
					visitorID1 + ":" + visitorID2, contentString);
		} else {
			redisConnect.hset(properties.getProperty("CONTENT_MAP") + ":" + visitorID1, visitorID1 + ":" + visitorID2,
					contentString);
		}

	}

	/* Fetch from content_map */
	private String getContentMap(String visitorID1, String visitorID2) {
		String contentString;
		if (Integer.parseInt(visitorID1) > 100) {
			contentString = redisConnect.hget(
					properties.getProperty("CONTENT_MAP") + ":" + visitorID1.substring(
							Integer.parseInt(properties.getProperty("low")), Integer.parseInt(properties.getProperty("high"))),
					visitorID1 + ":" + visitorID2);
		} else {
			contentString = redisConnect.hget(properties.getProperty("CONTENT_MAP") + ":" + visitorID1,
					visitorID1 + ":" + visitorID2);
		}
		return contentString;
	}

	public void addToContentMap(ContentRecommendationModel contentRecommendationModel) {
		String contentIDViewString = redisConnect.hget("VISITOR_ID_VIEW_SET:" + contentRecommendationModel.getVisitorId()
				.substring(Integer.parseInt(properties.getProperty("low")), Integer.parseInt(properties.getProperty("high"))),
				contentRecommendationModel.getVisitorId());
		Set<String> contentIDViewSet = contentRecommendationUtility.toSet(contentIDViewString);
		String contentIDDownloadString = redisConnect.hget("VISITOR_ID_DOWNLOAD_SET:" + contentRecommendationModel.getVisitorId()
				.substring(Integer.parseInt(properties.getProperty("low")), Integer.parseInt(properties.getProperty("high"))),
				contentRecommendationModel.getVisitorId());
		Set<String> contentIDDownloadSet = contentRecommendationUtility.toSet(contentIDDownloadString);

		contentIDViewSet.removeAll(contentIDDownloadSet);

		String[] contentIDViewSetArr = (String[]) contentIDViewSet.toArray(new String[contentIDViewSet.size()]);
		String[] contentIDDownloadSetArr = (String[]) contentIDDownloadSet
				.toArray(new String[contentIDDownloadSet.size()]);

		System.out.println(
				"View length : " + contentIDViewSetArr.length + " Download length: " + contentIDDownloadSetArr.length);

		if ((contentIDDownloadSetArr.length) > 1) {
			System.out.println("inside download");
			setContentMap(contentIDDownloadSetArr, properties.getProperty("two"));

		}
		if ((contentIDViewSetArr.length) > 1) {
			System.out.println("inside view");
			setContentMap(contentIDViewSetArr, properties.getProperty("one"));

		}
	}

	public Set<String> getSuggestion(String contentID, String visitorID) {
		redisConnect.select(3);
		/* Fetching content_map from redis */
		Map<String, String> contentMap = redisConnect.hgetAll(properties.getProperty("CONTENT_MAP") + ":" + contentID
				.substring(Integer.parseInt(properties.getProperty("low")), Integer.parseInt(properties.getProperty("high"))));

		/* Getting keys set from content_map */
		Set<String> contentIDKeySet = contentMap.keySet();

		String[] contentIDKeyArray = (String[]) contentIDKeySet.toArray(new String[contentIDKeySet.size()]);

		/*
		 * To create ordered set of content_id for suggestion with content_map
		 * values as score
		 */
		for (int i = 0; i < contentIDKeyArray.length; i++) {
			String[] contentIDValues = contentIDKeyArray[i].split(":");

			if (contentIDValues[0].equals(contentID)) {
				redisConnect.zadd(
						properties.getProperty("SUGGESTION_LIST") + ":"
								+ contentIDValues[Integer.parseInt(properties.getProperty("low"))],
						Double.parseDouble(contentMap.get(contentIDKeyArray[i])),
						contentIDValues[Integer.parseInt(properties.getProperty("one"))]);
			}
		}

		/* Getting suggestion set from redis for given content_id */
		Set<String> redisSuggestionList = redisConnect.zrevrange(properties.getProperty("SUGGESTION_LIST") + ":" + contentID,
				0, -1);
		System.out.println("Size = " + redisSuggestionList.size());

		if (visitorID != null) {
			/* Fetching download_set for given visitor_id */
			String visitorContentIDDownload = redisConnect.hget(
					properties.getProperty("VISITOR_ID_DOWNLOAD_SET") + ":" + visitorID.substring(
							Integer.parseInt(properties.getProperty("low")), Integer.parseInt(properties.getProperty("high"))),
					visitorID);

			Set<String> visitorContentIDDownloadSet = contentRecommendationUtility.toSet(visitorContentIDDownload);
			System.out.println("SUGGESTION_LIST:" + contentID);
			System.out.println("Size = " + visitorContentIDDownloadSet.size());

			/* Removing already downloaded content_id from suggestion_set */
			redisSuggestionList.removeAll(visitorContentIDDownloadSet);
		}

		// long result = redisConnect.del(prop.getProperty("SUGGESTION_LIST") +
		// ":" + contentID);

		//System.out.println("Result : " + res + " Size = " + redisSuggestionList.size());
		return redisSuggestionList;
	}


}
