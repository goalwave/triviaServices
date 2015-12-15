package com.goalwave.triviafb.ws.service;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.goalwave.triviafb.ws.model.AccessToken;
import com.goalwave.triviafb.ws.model.User;

import com.goalwave.triviafb.ws.util.Secured;

@Path("/user")
@Secured
public class UserService {
	private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<User> getAllUsers() throws Exception{
        // Open a database connection
        // (create a new database if it doesn't exist yet):
        EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("triviafb");
        
        EntityManager em = emf.createEntityManager();
   
        // Retrieve all the Point objects from the database:
        TypedQuery<User> query =
            em.createQuery("SELECT u FROM User u", User.class);
        List<User> users = query.getResultList();
 
        // Close the database connection:
        em.close();
        emf.close();
        
        LOGGER.info("getAllUsers(): found "+users.size()+" goals(s) on DB");
       
        return users; //do not use Response object because this causes issues when generating XML automatically
    }
	
    @GET
    @Path("/{userId}")
    @Produces({MediaType.APPLICATION_JSON})
    public User getUser(@PathParam("userId") int userId) throws Exception{
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("triviafb");
            
            EntityManager em = emf.createEntityManager();
       
            // Retrieve all the Point objects from the database:
            TypedQuery<User> query =
                em.createQuery("SELECT u FROM User u WHERE u.userId = :userId", User.class);
            
            User user = query.setParameter("userId", userId).getSingleResult();
            
            // Close the database connection:
            em.close();
            emf.close();
                       
            return user; //do not use Response object because this causes issues when generating XML automatically
    	
    }
    
    
    public User getOrCreateUserByEmail(String email) throws Exception{
    	User user = null;
    	
    	try{
    		user = this.getUserByEmail(email);
    		
    	}catch(Exception e){
    		user = new User();
    		user.setEmail(email);
    		//Didn't find one...let's create it
    		user = addUser(user);
    		
    	}
    	return user;
    }
    
    
    
    public User getUserByEmail(String email) throws Exception{
    	EntityManagerFactory emf = null;
    	EntityManager em = null;
    	User user = null;
    	
    	try{
    		emf = Persistence.createEntityManagerFactory("triviafb");
            		
    		em = emf.createEntityManager();
       
    		// Retrieve all the Point objects from the database:
    		TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            
    		user = query.setParameter("email", email).getSingleResult();
            
    	}finally{// Close the database connection:
    		em.close();
    		emf.close();
    	}
    	
    	return user; //do not use Response object because this causes issues when generating XML automatically
    	
    }
    
    public AccessToken getAuthenticatedUserByEmail(String email) throws Exception{
    	
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("triviafb");
            
            EntityManager em = emf.createEntityManager();
       
            // Retrieve all the Point objects from the database:
            TypedQuery<AccessToken> query =
                em.createQuery("SELECT au FROM AuthenticatedUser au WHERE au.email = :email", AccessToken.class);
            
            AccessToken au = query.setParameter("email", email).getSingleResult();
            
            // Close the database connection:
            em.close();
            emf.close();
                       
            return au; //do not use Response object because this causes issues when generating XML automatically
    	
    }
    
    public void addAuthenticatedUser(AccessToken au) throws Exception{
        EntityManagerFactory emf =
        		Persistence.createEntityManagerFactory("triviafb");
                	
        EntityManager em = emf.createEntityManager();
        		
        em.getTransaction().begin();
            
        em.persist(au);

        em.getTransaction().commit();

        em.close();
        emf.close();
    }
    
    public void removeAuthenticatedUser(AccessToken au) throws Exception{
    	EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("triviafb");
            
        EntityManager em = emf.createEntityManager();
    	
        em.getTransaction().begin();
        
        int deletedCount = em.createQuery("Delete from AuthenticatedUser au WHERE au.email = :email").setParameter("email", au.getEmail()).executeUpdate();

        
        em.getTransaction().commit();

        LOGGER.info("Deleted rows: " + deletedCount);
       
        
        em.close();
        emf.close();
    }
    
    public void addOrUpdateAuthenticatedUser(AccessToken au) throws Exception{
        AccessToken storedUser = null;
    	try{
    		storedUser = getAuthenticatedUserByEmail(au.getEmail());
    	}catch(Exception e){
    		//suppress
    	}
    	
    	if(storedUser != null){
    		removeAuthenticatedUser(au);
    	}
    	addAuthenticatedUser(au);
    	
    }
    
    
    public User addUser(User user) throws Exception{
    	
		EntityManagerFactory emf = null;

		EntityManager em = null;
		
        try {

            emf = Persistence.createEntityManagerFactory("triviafb");
                
            em = emf.createEntityManager();
        	
            em.getTransaction().begin();
            
            em.persist(user);

            em.getTransaction().commit();

        } finally {
            em.close();
            emf.close();
        }      
        return user;
	}

}
