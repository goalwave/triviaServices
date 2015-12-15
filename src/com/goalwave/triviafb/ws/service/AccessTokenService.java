package com.goalwave.triviafb.ws.service;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.goalwave.triviafb.ws.model.AccessToken;

public class AccessTokenService {
	private static final Logger LOGGER = Logger.getLogger(AccessTokenService.class.getName());

	
	public void addOrUpdate(AccessToken at) throws Exception{
		AccessToken newToken = null;
		try{
			newToken = this.getByEmailAddress(at);

			//found it - let's remove it...
			this.remove(newToken);
			
		}catch(Exception e){
			//not found...suppress...
		}
		this.persist(at);
	}
	
	public void persist(AccessToken at) throws Exception {
		EntityManagerFactory emf = null;
		EntityManager  em = null;
		
		try{
			emf = Persistence.createEntityManagerFactory("triviafb");
            	
			em = emf.createEntityManager();
    		
			em.getTransaction().begin();
        	
			em.persist(at);

			em.getTransaction().commit();
		}finally{
			em.close();
			emf.close();
		}
		
	}
	
	public AccessToken getByEmailAddress(String email) throws Exception {
		EntityManagerFactory emf = null;
		EntityManager  em = null;
		AccessToken newToken = null;
		try{
			emf = Persistence.createEntityManagerFactory("triviafb");
            	
			em = emf.createEntityManager();
    		
    		// Retrieve all the Point objects from the database:
    		TypedQuery<AccessToken> query = em.createQuery("SELECT at FROM AccessToken at WHERE at.email = :email", AccessToken.class);
            
    		newToken = query.setParameter("email", email).getSingleResult();

			
		}finally{
			em.close();
			emf.close();
		}
		
		return newToken;
		
	}
	
	public AccessToken getByEmailAddress(AccessToken at) throws Exception {
		return this.getByEmailAddress(at.getEmail());
	}
	
	public void remove(AccessToken at) throws Exception {
		EntityManagerFactory emf = null;
		EntityManager  em = null;

		try{
			emf = Persistence.createEntityManagerFactory("triviafb");
            	
			em = emf.createEntityManager();
    		
			em.getTransaction().begin();
			
			int deletedCount = em.createQuery("Delete from AccessToken at WHERE at.userId = :id").setParameter("id", at.getUserId()).executeUpdate();

        
			em.getTransaction().commit();

			LOGGER.info("Deleted rows: " + deletedCount);
			
		}finally{
			em.close();
			emf.close();
		}
		
		
	}
	
}
