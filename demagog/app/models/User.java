package models;

import java.util.List;

import javax.persistence.Id;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.logging.MorphiaLoggerFactory;
import com.google.code.morphia.logging.slf4j.SLF4JLogrImplFactory;
import com.mongodb.Mongo;

@Entity
public class User {
	
	@Id
	public ObjectId id;
	
	public String username;
	public String password;
	public String sessionid;
	
	public User() {
		
	}
	
	public User(String uname, String passwd) {
		this.username = uname;
		this.password = passwd;
	}
	
	private static Datastore ds;
	
	static {
		Morphia morphia = new Morphia();  
	    Mongo mongo;
		try {
			mongo = new Mongo("127.0.0.1", 27017);
		} catch (Exception e) {
			throw new RuntimeException("Failed to init MongoDB database connection.", e);
		}  
		
	    ds = morphia.createDatastore(mongo, "demagog");	    
	}
	
	public void save() {
		ds.save(this);
	}
	
	public void updateSID(String sid) {
		//UPDATE
		ds.update(ds.createQuery(User.class).field("id").equal(this.id), ds.createUpdateOperations(User.class).set("sessionid", sid));
	}
	
	public static User findUser(String username) {
		return ds.find(User.class).field("username").equal(username).get();
	}
	
	public static User findSID(String sessionid) {
		return ds.find(User.class).field("sessionid").equal(sessionid).get();
	}
	
	@Override
	public int hashCode() {
		//hashCode
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		//equals
		return super.equals(obj);
	}
}
