package main.java.utils;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

/**
 * 
 * 
 */
public class MongoDBAccess {

	private  MongoClient mongoClient;
	private  DB mongoDB;
	private  DBCollection mongoCollection;	

	
	
	public MongoDBAccess(String mongodbHost, int mongodbPort) {
		 createMongoClient(mongodbHost, mongodbPort);
	}

	private void createMongoClient(String host, int port) {

		try {

			this.mongoClient = new MongoClient(host, port);

		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
	public MongoClient getMongoClient() {

		return this.mongoClient;

	}
	
	
	public void closeMongoClient() {

		this.mongoClient.close();

	}
	
	
	
	@SuppressWarnings("deprecation")
	public DB getMongoDB(String dataBaseName) {

		try {

			this.mongoDB = this.mongoClient.getDB(dataBaseName);

		} catch (MongoException e) {
			e.printStackTrace();
		}

		return mongoDB;

	}
	
	public DBCollection getMongoCollection(String collectionName) {

		try {

			this.mongoCollection = this.mongoDB.getCollection(collectionName);

		} catch (MongoException e) {
			e.printStackTrace();
		}

		return mongoCollection;

	}
	
	
	
	public void insertNewMongoDocument(DBCollection mongoCollection, LinkedHashMap<String, String> documentContent) {

		BasicDBObject mongoDocument = new BasicDBObject();

		try {

			for (Map.Entry<String, String> entry : documentContent.entrySet()) {
				mongoDocument.put(entry.getKey(), entry.getValue());
			}

			mongoCollection.insert(mongoDocument);

		} catch (MongoException e) {
			e.printStackTrace();
		}

	}
	
	
	public void updateMongoDocument(DBCollection mongoCollection, String testCase, String newValue) {
		
				
		try {
			
			BasicDBObject newDocument = new BasicDBObject();			
			newDocument.append("$set", new BasicDBObject().append("Status", newValue));	
			BasicDBObject searchQuery = new BasicDBObject().append("TC_ID", testCase);
			mongoCollection.update(searchQuery, newDocument);
			
		} catch (MongoException e) {
			e.printStackTrace();
		}

		
	}
	

}