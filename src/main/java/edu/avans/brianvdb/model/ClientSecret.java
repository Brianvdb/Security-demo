package edu.avans.brianvdb.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Brian on 6-6-2016.
 */
public class ClientSecret {
    private String name;
    @SerializedName("secret_message")
    private String secretMessage;
    private String password;

    public String getName() {
        return name;
    }

    public String getSecretMessage() {
        return secretMessage;
    }

    public String getPassword() {
        return password;
    }
}
