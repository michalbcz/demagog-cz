package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.mongodb.ObjectId;

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
	
}
