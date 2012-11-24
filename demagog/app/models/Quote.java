package models;

import java.util.Date;
import java.util.List;

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
	
	public Date creationDate;
	
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
	
	public void save() {
		ds.save(this);
	}
	
	public static List<Quote> findAll() {
		return ds.find(Quote.class).asList();
	}
	
	public static void deleteAll() {
		ds.delete(ds.createQuery(Quote.class));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((approvalDate == null) ? 0 : approvalDate.hashCode());
		result = prime * result + (approved ? 1231 : 1237);
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((quoteText == null) ? 0 : quoteText.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Quote other = (Quote) obj;
		if (approvalDate == null) {
			if (other.approvalDate != null)
				return false;
		} else if (!approvalDate.equals(other.approvalDate))
			return false;
		if (approved != other.approved)
			return false;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (quoteText == null) {
			if (other.quoteText != null)
				return false;
		} else if (!quoteText.equals(other.quoteText))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
}
