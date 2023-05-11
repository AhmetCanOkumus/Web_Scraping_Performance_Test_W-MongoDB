package org.example;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Dbo {




    final MongoClient mongoClient=new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    final MongoDatabase database=mongoClient.getDatabase("yazlab");
    final MongoCollection collection=database.getCollection("deneme");



    public void insert(String name,String price){

        Document a = new Document();

        a.append("name",name)
                .append("price",price);

        collection.insertOne(a);

    }


    public boolean checkIfRecordExists(String name, String price) {
        Document query = new Document();
        query.append("name", name)
                .append("price", price);
        return collection.countDocuments(query) > 0;
    }



    public String getFiyat(String isim) {
        String fiyat = "";
        try {
            // Sorguyu hazırla
            BasicDBObject query = new BasicDBObject();
            query.put("name", isim);

            FindIterable<Document> result = collection.find(query);
            for (Document doc : result) {
                fiyat = doc.getString("fiyat");
            }

        } catch (MongoException e) {
            e.printStackTrace();
        }
        return fiyat;
    }


    public void updateFiyat(String isim, String yeniFiyat) {
        try {

            BasicDBObject query = new BasicDBObject();
            query.put("name", isim);

            BasicDBObject newValues = new BasicDBObject();
            newValues.put("$set", new BasicDBObject("price", yeniFiyat));

            collection.updateOne(query, newValues);

        } catch (MongoException e) {
            e.printStackTrace();
        }
    }





}
