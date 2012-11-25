package models;

import javax.persistence.Id;

import org.bson.types.ObjectId;

import utils.DBHolder;

import com.google.code.morphia.annotations.Entity;

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
	
	public void save() {
		DBHolder.ds.save(this);
	}
	
	public void updateSID(String sid) {
		//UPDATE
		DBHolder.ds.update(DBHolder.ds.createQuery(User.class).field("id").equal(this.id), DBHolder.ds.createUpdateOperations(User.class).set("sessionid", sid));
	}
	
	public static User findUser(String username) {
		return DBHolder.ds.find(User.class).field("username").equal(username).get();
	}
	
	public static User findSID(String sessionid) {
		return DBHolder.ds.find(User.class).field("sessionid").equal(sessionid).get();
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
