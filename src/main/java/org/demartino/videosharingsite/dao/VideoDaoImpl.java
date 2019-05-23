package org.demartino.videosharingsite.dao;

import java.util.List;

import org.demartino.videosharingsite.entity.UploadedVideo;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("videoDao")
public class VideoDaoImpl implements VideoDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public UploadedVideo createVideo(UploadedVideo upload) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(upload);
		UploadedVideo returnedUploadEntity = findVideoByTitle(upload.getTitle());
		return returnedUploadEntity;	
	}
	
	public boolean deleteVideoById(Long id) {
		Query query = sessionFactory.getCurrentSession().createQuery("delete upload where id = :id");
		query.setParameter("id", id);
		int result = query.executeUpdate();
		if (result == 1)
		{
			return true;
		}
		return false;
	}

	public UploadedVideo findVideoByTitle(String title) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UploadedVideo.class);
		criteria.add(Restrictions.eq("title", title));
		return(UploadedVideo) criteria.uniqueResult();
	}

	public UploadedVideo updateVideo(UploadedVideo upload) {
		Session session = sessionFactory.getCurrentSession();
		session.update(upload);
		return upload;
	}
	
	@SuppressWarnings("unchecked")
	public List<UploadedVideo> getAllVideosForUser(String username)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UploadedVideo.class);
		criteria.add(Restrictions.eq("username", username));
		return criteria.list();
		
	}
	
}
