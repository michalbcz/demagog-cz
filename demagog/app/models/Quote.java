package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;



@Entity
public class Quote {
	
	public static final String AUTHOR_EMPTY_FILTER = "";

	@Id
	public ObjectId id;
	
	/**
	 * Source url of given quote
	 */
	public String url;
	
	public String quoteText;
	
	public String author;
	
	public Date creationDate;
		
	public QuoteState quoteState;
	
	public Date approvalDate;

	public int voteCount;
	
	public enum QuoteState {
		NEW, APPROVED, CHECKED
	}
	
	public Quote() {
	}
	
	public Quote(String quoteText, String url, String author, QuoteState state) {
		this.url = url;
		this.quoteText = quoteText;
		this.author = author;
		this.creationDate = new Date();
		this.quoteState = state;
		if (state != QuoteState.NEW)
			approvalDate = new Date();
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

	public static void setChecked(ObjectId id) {
		ds.update(ds.createQuery(Quote.class).field("_id").equal(id), ds.createUpdateOperations(Quote.class).set("quoteState", QuoteState.CHECKED));
	}
	
	public static void approve(ObjectId id, String text, String author) {
		ds.update(ds.createQuery(Quote.class).field("_id").equal(id), ds.createUpdateOperations(Quote.class).set("quoteState", QuoteState.APPROVED).set("quoteText", text).set("author", author));
	}
	
	public static void upVote(ObjectId id) {
		ds.update(ds.createQuery(Quote.class).field("_id").equal(id), ds.createUpdateOperations(Quote.class).inc("voteCount"));
	}
	
	public static Quote findById(ObjectId id) {
		return ds.find(Quote.class, "_id", id).get();
	}
	
	public static List<Quote> findAll() {
		return ds.find(Quote.class).asList();
	}
	
	public static List<Quote> findAllWithApprovedState(QuoteState state) {
		return ds.find(Quote.class).field("quoteState").equal(state).asList();
	}
	
	public static void delete(ObjectId id) {
		ds.delete(ds.createQuery(Quote.class).field("_id").equal(id));
	}
	
	public static void deleteAll() {
		ds.delete(ds.createQuery(Quote.class));
	}
	
	public int vote() {
		return ++voteCount;
	}
	
	public static List<Quote> findAllSortedByVote(boolean onlyApproved) {
		Query<Quote> query = ds.find(Quote.class);
		if (onlyApproved) {
			query = query.field("quoteState").equal(QuoteState.APPROVED);
		}
		return query.order("-voteCount").asList();
	}
	
	public static List<Quote> findAllSortedByCreationDate(boolean onlyApproved) {
		Query<Quote> query = ds.find(Quote.class);
		if (onlyApproved) {
			query = query.field("quoteState").equal(QuoteState.APPROVED);
		}
		return query.order("-creationDate").asList();
	}
	
	public static List<Quote> findAllSortedByVoteFilteredByAuthor(String author, boolean onlyApproved) {
		Query<Quote> query = ds.find(Quote.class);
		if (onlyApproved) {
			query = query.field("quoteState").equal(QuoteState.APPROVED);
		}
		return query.filter("author", author).order("-voteCount").asList();
	}

	public static List<String> getAllAuthorNames() {
		Set<String> names = new TreeSet<String>();
		names.add(AUTHOR_EMPTY_FILTER);
		
		for (Quote quote : findAll()) {
			if (quote.author != null) {
				names.add(quote.author);
			}
		}
		return new ArrayList<String>(names);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((approvalDate == null) ? 0 : approvalDate.hashCode());
		result = prime * result + (quoteState == QuoteState.NEW ? 1231 : 1237);
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
		if (quoteState != other.quoteState) {
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
