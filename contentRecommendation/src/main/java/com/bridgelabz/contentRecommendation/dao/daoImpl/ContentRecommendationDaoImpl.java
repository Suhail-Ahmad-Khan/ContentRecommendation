package com.bridgelabz.contentRecommendation.dao.daoImpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import com.bridgelabz.contentRecommendation.dao.ContentRecommendationDaoInterface;
import com.bridgelabz.contentRecommendation.model.ContentRecommendationModel;

public class ContentRecommendationDaoImpl implements ContentRecommendationDaoInterface {

	@Autowired
	SessionFactory sessionFactory;

	public void addRecord(ContentRecommendationModel contentRecommendationModel) {
		if (getByContentID(contentRecommendationModel.getContentId()) == null) {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			// System.out.println("Inside dao add " + record.getmVisitorID());
			try {
				// System.out.println("Transaction started..");
				session.save(contentRecommendationModel);
				// System.out.println("Transaction commit");
				tr.commit();
			} catch (Exception e) {
				tr.rollback();
				session.close();
			}
		} else {
			// System.out.println("Already exists");
		}
	}

	/*
	 * @SuppressWarnings("unchecked") public boolean getByContentID(String
	 * pContentID) { Session session = sessionFactory.openSession();
	 * 
	 * Query query =
	 * session.createQuery("from RecModel u where u.mContentID= :ContentID");
	 * query.setParameter("ContentID", pContentID); //query. List<String> result
	 * = query.getResultList(); // getting hql result List<RecModel> res =
	 * query.getResultList();
	 * System.out.println("Inside getcontentbyid : "+res.size()); // checking
	 * result if (result != null && result.size() > 0){
	 * System.out.println("Inside getcontentbyid : contentid "+res.get(0).
	 * getmContentID()); return true;} else return false;
	 * 
	 * }
	 */

	@SuppressWarnings("unchecked")
	public ContentRecommendationModel getByContentID(String pContentID) {
		Session session = sessionFactory.openSession();

		Query<ContentRecommendationModel> query = session.createQuery("from RecModel u where u.mContentID= :ContentID");
		query.setParameter("ContentID", pContentID);
		// query.
		// List<String> result = query.getResultList(); // getting hql result
		List<ContentRecommendationModel> res = query.getResultList();
		System.out.println("Inside getcontentbyid : " + res.size());
		// checking result
		if (res != null && res.size() > 0) {
			System.out.println("Inside getcontentbyid : contentid " + res.get(0).getContentId());
			return res.get(0);
		} else
			return null;

	}

	@SuppressWarnings("unchecked")
	public ContentRecommendationModel getByContentName(String pContentName) {
		Session session = sessionFactory.openSession();

		Query<ContentRecommendationModel> query = session.createQuery("from RecModel u where u.mContentName= :ContentName");
		query.setParameter("ContentName", pContentName);
		// query.
		// List<String> result = query.getResultList(); // getting hql result
		List<ContentRecommendationModel> res = query.getResultList();
		System.out.println("Inside getcontentbyname : " + res.size());
		// checking result
		if (res != null && res.size() > 0) {
			System.out.println("Inside getcontentbyname : contentid " + res.get(0).getContentId() + " content name:"
					+ res.get(0).getContentName());
			return res.get(0);
		} else
			return null;

	}

}
