package org.demartino.videosharingsite.dao;

import java.util.List;

import org.demartino.videosharingsite.entity.AppUser;
import org.demartino.videosharingsite.view.Login;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public AppUser createUser(AppUser user) { //why does it make me remove the @Override annotation?
		Session session = sessionFactory.getCurrentSession(); 
		session.persist(user);
		AppUser returnedUser = findUserByUsername(user.getUsername());
		return returnedUser;		
	}

	public boolean deleteUserByUsername(String username) {
		Query query = sessionFactory.getCurrentSession().createQuery("delete from AppUser where username = :username");
		query.setParameter("username", username);
		int result = query.executeUpdate();
		return result == 1;
	}

	public AppUser findUserByUsername(String username) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AppUser.class);
		criteria.add(Restrictions.eq("username", username));
		return (AppUser) criteria.uniqueResult();
	}

	public AppUser updateUser(AppUser user) {
		System.out.println("In updateUser Dao method.");
		System.out.println(user);
		sessionFactory.getCurrentSession().update(user);
		return user;
	}
	
	public List<AppUser> getAllUsers() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AppUser.class);
		@SuppressWarnings("unchecked")
		List<AppUser> users = criteria.list(); 
		return users;
	}
	
	
	public boolean isValidLogin(Login login)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AppUser.class);
		criteria.add(Restrictions.eq("username", login.getUsername()));
		criteria.add(Restrictions.eq("password", login.getPassword()));
		AppUser userEntity = (AppUser) criteria.uniqueResult();
		if(userEntity == null)
		{
			return false;
		}
		return true; 
	}

}
