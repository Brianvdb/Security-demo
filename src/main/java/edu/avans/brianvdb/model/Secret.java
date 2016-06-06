package edu.avans.brianvdb.model;

import com.mongodb.BasicDBObject;

/**
 * Created by Brian on 6-6-2016.
 */
public class Secret {

    private String id;
    private String name;
    private String secretMessageCiphered;
    private String secretMessageIv;
    private String salt;

    public Secret() {

    }

    public Secret(BasicDBObject dbObject) {
        this.id = dbObject.get("_id").toString();
        this.name = dbObject.getString("name");
        this.secretMessageCiphered = dbObject.getString("secretMessageCiphered");
        this.secretMessageIv = dbObject.getString("secretMessageIv");
        this.salt = dbObject.getString("salt");
    }

    public String getName() {
        return this.name;
    }

    public String getSecretMessageCiphered() {
        return this.secretMessageCiphered;
    }

    public String getSecretMessageIv() {
        return this.secretMessageIv;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setSecretMessageCiphered(String secretMessageCiphered) {
        this.secretMessageCiphered = secretMessageCiphered;
    }

    public void setSecretMessageIv(String secretMessageIv) {
        this.secretMessageIv = secretMessageIv;
    }

    public String getSalt() {
        return this.salt;
    }

    public String toString() {
        return "[name: " + name + ", salt: " + getSalt() + ", ciphered: " + getSecretMessageCiphered() + ", iv: " + getSecretMessageIv() + "]";
    }
}
