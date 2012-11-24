package models;

import java.util.Date;

import javax.persistence.Id;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.annotations.Entity;
import com.mongodb.Mongo;


@Entity
public class Quote {
	
	@Id
	public ObjectId id;
	
	public String url;
	
	public String quoteText;
	
	public String author;
	
	public String creationDate;
	
	public boolean approved;
	
	public Date approvalDate;
	
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
}
