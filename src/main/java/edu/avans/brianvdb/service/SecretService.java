package edu.avans.brianvdb.service;

import com.mongodb.*;
import edu.avans.brianvdb.model.Secret;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brian on 6-6-2016.
 */
public class SecretService {

    private final DB db;
    private final DBCollection collection;

    public SecretService(DB db) {
        this.db = db;
        this.collection = db.getCollection("secrets");
    }

    public List<Secret> findAll() {
        List<Secret> secrets = new ArrayList<>();
        DBCursor dbObjects = collection.find();
        while(dbObjects.hasNext()) {
            DBObject dbObject = dbObjects.next();
            secrets.add(new Secret((BasicDBObject) dbObject));
        }
        return secrets;
    }

    public Secret getByName(String name) {
        BasicDBObject dbObject = (BasicDBObject) collection.findOne(new BasicDBObject("name", name));
        if(dbObject != null) return new Secret(dbObject);
        return null;
    }

    public boolean isNameTaken(String name) {
        return getByName(name) != null;
    }

    public void addSecret(Secret secret) {
        BasicDBObject object = new BasicDBObject("name", secret.getName());
        object.append("secretMessageCiphered", secret.getSecretMessageCiphered());
        object.append("secretMessageIv", secret.getSecretMessageIv());
        object.append("salt", secret.getSalt());
        collection.insert(object);
    }

}
