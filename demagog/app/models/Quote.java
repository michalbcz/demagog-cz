package models;

import java.util.Date;
import java.util.List;


import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.query.UpdateOperations;
import com.mongodb.Mongo;

@Entity
public class Quote {
	
	@Id
	public ObjectId id;
	
	public String url;
	
	public String quoteText;
	
	public String author;
	
	public Date creationDate;
	
	public boolean approved = false;
	
	public Date approvalDate;

	public int voteCount;
	
	public Quote() {
	}
	
	public Quote(String quoteText, String url, String author) {
		this.url = url;
		this.quoteText = quoteText;
		this.author = author;
		this.creationDate = new Date();
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
	
	public static void upVote(ObjectId id) {
		ds.update(ds.createQuery(Quote.class).field("_id").equal(id), ds.createUpdateOperations(Quote.class).inc("voteCount"));
	}
	
	public static List<Quote> findAll() {
		return ds.find(Quote.class).asList();
	}
	
	public static List<Quote> findAllWithApprovedState(boolean approved) {
		return ds.find(Quote.class).field("approved").equal(approved).asList();
	}
	
	public static void deleteAll() {
		ds.delete(ds.createQuery(Quote.class));
	}
	
	public int vote() {
		return ++voteCount;
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
		result = prime * result + voteCount;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Quote)) {
			return false;
		}
		Quote other = (Quote) obj;
		if (approvalDate == null) {
			if (other.approvalDate != null) {
				return false;
			}
		} else if (!approvalDate.equals(other.approvalDate)) {
			return false;
		}
		if (approved != other.approved) {
			return false;
		}
		if (author == null) {
			if (other.author != null) {
				return false;
			}
		} else if (!author.equals(other.author)) {
			return false;
		}
		if (creationDate == null) {
			if (other.creationDate != null) {
				return false;
			}
		} else if (!creationDate.equals(other.creationDate)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (quoteText == null) {
			if (other.quoteText != null) {
				return false;
			}
		} else if (!quoteText.equals(other.quoteText)) {
			return false;
		}
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		if (voteCount != other.voteCount) {
			return false;
		}
		return true;
	}
	
}
