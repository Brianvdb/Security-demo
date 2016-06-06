package edu.avans.brianvdb.resources;

import com.google.gson.Gson;
import edu.avans.brianvdb.model.ClientSecret;
import edu.avans.brianvdb.model.Message;
import edu.avans.brianvdb.model.Secret;
import edu.avans.brianvdb.service.SecretService;
import edu.avans.brianvdb.utils.SuperSecretMaker;

import static spark.Spark.post;

/**
 * Created by Brian on 6-6-2016.
 */
public class SecretsResource {

    private final SecretService service;

    public SecretsResource(SecretService service) {
        this.service = service;
        setupEndpoints();
    }

    private void setupEndpoints() {
        post("/secrets", (request, response) -> {
            response.type("application/json");
            String body = request.body();
            Gson gson = new Gson();
            try {
                ClientSecret secret = gson.fromJson(body, ClientSecret.class);
                if (secret.getName() == null || secret.getName().isEmpty() || secret.getSecretMessage() == null || secret.getSecretMessage().isEmpty() || secret.getPassword() == null || secret.getPassword().isEmpty()) {
                    response.status(400);
                    return gson.toJson(new Message("name, password and secret_message are required fields"));
                }

                if(service.isNameTaken(secret.getName())) {
                    response.status(400);
                    return gson.toJson(new Message("this name is taken, please pick another name"));
                }

                Secret secretObj = SuperSecretMaker.encrypt(secret.getSecretMessage(), secret.getPassword());
                secretObj.setName(secret.getName());
                service.addSecret(secretObj);

                response.status(201);
                return gson.toJson(new Message("secret has been added"));
            } catch(Throwable t) {
                response.status(400);
                return gson.toJson(new Message("error: " + t.getMessage()));
            }
        });

        post("/getsecret", (request, response) -> {
            response.type("application/json");
            String body = request.body();
            Gson gson = new Gson();
            try {
                ClientSecret secret = gson.fromJson(body, ClientSecret.class);
                if (secret.getName() == null || secret.getName().isEmpty() || secret.getPassword() == null || secret.getPassword().isEmpty()) {
                    response.status(400);
                    return gson.toJson(new Message("name and password are required fields"));
                }

                Secret secretObj = service.getByName(secret.getName());

                String secretMessage = SuperSecretMaker.decrypt(secretObj, secret.getPassword());
                if(secretMessage == null) {
                    response.status(400);
                    return gson.toJson(new Message("verkeerde naam/wachtwoord"));
                }

                response.status(200);
                return gson.toJson(new Message(secretMessage));
            } catch(Throwable t) {
                response.status(400);
                return gson.toJson(new Message("error: " + t.getMessage()));
            }
        });
    }


}
