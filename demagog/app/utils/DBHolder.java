package utils;

import play.Logger;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import com.typesafe.config.ConfigFactory;
import play.Play;

public class DBHolder {

	public static Datastore ds;

	static {
		Morphia morphia = new Morphia();  
		Mongo mongo;
		MongoURI mongoURI;
		try {
			String name;
			String password;

			String mongoDbUrl = Play.application().configuration().getString("mongodb.url");

			Logger.info("Connecting to MongoDB instance at url: " + mongoDbUrl);

			if (mongoDbUrl.contains("@")) {
				int nameStart = mongoDbUrl.indexOf("://") + 3;
				int nameEnd = mongoDbUrl.indexOf("@");

				String nameAndPassword = mongoDbUrl.substring(nameStart, nameEnd);

				mongoDbUrl = mongoDbUrl.replace(nameAndPassword + "@", "");

				String[] split = nameAndPassword.split(":");
				name = split[0];
				password = split[1];
			} else {
				name = null;
				password = null;
			}

			mongoURI = new MongoURI(mongoDbUrl);
			DB db = mongoURI.connectDB();
			if (name != null) {
				db.authenticate(name, password.toCharArray());
			}
			mongo = db.getMongo();

		} catch (Exception e) {
			throw new RuntimeException("Failed to init MongoDB database connection.", e);
		}  

		ds = morphia.createDatastore(mongo, mongoURI.getDatabase());	    
	}

}
