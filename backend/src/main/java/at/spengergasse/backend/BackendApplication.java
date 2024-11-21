package at.spengergasse.backend;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import static com.mongodb.client.model.Filters.eq;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {

		// Replace the placeholder with your MongoDB deployment's connection string
		String uri = "mongodb+srv://clemil:emil@clemil0.wbzbs.mongodb.net/?retryWrites=true&w=majority&appName=Clemil0";

		try (MongoClient mongoClient = MongoClients.create(uri)) {
			MongoDatabase database = mongoClient.getDatabase("sample_mflix");
			MongoCollection<org.bson.Document> collection = database.getCollection("movies");

			Document doc = collection.find(eq("title", "Back to the Future")).first();
			if (doc != null) {
				System.out.println(doc.toJson());
			} else {
				System.out.println("No matching documents found.");
			}
		}

		SpringApplication.run(BackendApplication.class, args);
	}

}
