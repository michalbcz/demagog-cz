package models;

import org.bson.types.ObjectId;

import utils.DBHolder;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class User {
	
	@Id
	public ObjectId id;
	
	public String username;
	public String password;
	
	public User() {}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [username=").append(username)
				.append(", password=").append(password).append("]");
		return builder.toString();
	}

	public void save() {
		DBHolder.ds.save(this /*, WriteConcern.SAFE/**/);
	}
	
	public static User findByName(String username) {
		return DBHolder.ds.find(User.class).field("username").equal(username).get();
	}
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
