package at.spengergasse.backend;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;
import java.util.Properties;

import static com.mongodb.client.model.Filters.eq;

@SpringBootApplication
public class BackendApplication {


	public static void main(String[] args) {
		try (InputStream input = BackendApplication.class.getClassLoader().getResourceAsStream("env.properties")) {
			if (input == null) {
				System.out.println("Sorry, unable to find env.properties");
				return;
			}

			Properties prop = new Properties();
			prop.load(input);

			String uri = prop.getProperty("URI");

			try (MongoClient mongoClient = MongoClients.create(uri)) {
				MongoDatabase database = mongoClient.getDatabase("sample_mflix");
				MongoCollection<Document> collection = database.getCollection("movies");

				Document doc = collection.find(eq("title", "Back to the Future")).first();
				if (doc != null) {
					System.out.println(doc.toJson());
				} else {
					System.out.println("No matching documents found.");
				}
			}
		} catch (Exception e) {
			System.out.println("Error reading properties file: " + e.getMessage());
			System.exit(1);
		}

		SpringApplication.run(BackendApplication.class, args);
	}
}
