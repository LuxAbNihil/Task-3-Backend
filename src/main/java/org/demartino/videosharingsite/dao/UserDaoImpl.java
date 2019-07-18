package org.demartino.videosharingsite.dao;

import java.util.List;

import org.demartino.videosharingsite.entity.AppUser;
import org.demartino.videosharingsite.entity.PasswordResetToken;
import org.demartino.videosharingsite.view.Login;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
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
		sessionFactory.getCurrentSession().update(user);
		return user;
	}
	
	public List<AppUser> getAllUsers() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AppUser.class);
		criteria.addOrder(Order.desc("id"));
		@SuppressWarnings("unchecked")
		List<AppUser> users = criteria.list(); 
		return users;
	}

	public String getPasswordByUsername(String username) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AppUser.class);
		criteria.add(Restrictions.eq("username", username));
		AppUser appUser = (AppUser) criteria.uniqueResult();
		String hashedPassword = appUser.getPassword();
		return hashedPassword;
	}
	
	public AppUser getUserByEmail(String email) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AppUser.class);
		criteria.add(Restrictions.eq("email", email));
		AppUser appUser = (AppUser) criteria.uniqueResult();
		return appUser; 
	}
	
	public PasswordResetToken createPasswordResetToken(PasswordResetToken passwordResetToken) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(passwordResetToken);
		return passwordResetToken;
	}
}
