package edu.avans.brianvdb;

import com.mongodb.*;
import edu.avans.brianvdb.resources.SecretsResource;
import edu.avans.brianvdb.service.SecretService;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import static spark.Spark.get;

/**
 * Created by Brian on 6-6-2016.
 */
public class Server {

    public static void main(String[] args) throws Throwable {
        get("/", (rq, rs) -> new ModelAndView(null, "index.html"), new HandlebarsTemplateEngine());
        get("/makesecret", (rq, rs) -> new ModelAndView(null, "makesecret.html"), new HandlebarsTemplateEngine());
        get("/unlocksecret", (rq, rs) -> new ModelAndView(null, "unlocksecret.html"), new HandlebarsTemplateEngine());

        new SecretsResource(new SecretService(getMongoDatabase()));
    }

    private static DB getMongoDatabase() throws Throwable {
        int port = 27017;
        String dbname = "secrets_db";
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().build();
        MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", port), mongoClientOptions);
        mongoClient.setWriteConcern(WriteConcern.SAFE);
        return mongoClient.getDB(dbname);
    }

}
