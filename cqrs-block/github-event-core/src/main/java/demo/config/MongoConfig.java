package demo.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;

@EnableMongoAuditing
@Profile({"development", "cloud"})
@Configuration
public class MongoConfig extends AbstractCloudConfig {

    @Bean
    public MongoDbFactory mongoFactory() {
        return connectionFactory().mongoDbFactory();
    }

    @Bean
    public ConnectionFactory rabbitFactory() {
        return connectionFactory().rabbitConnectionFactory();
    }
    
    @Bean
    CommandLineRunner commandLineRunner(MongoOperations operations) {
        return (args) -> {
            // Setup the streaming data endpoint
            if (operations.collectionExists("tightCouplingEvent")) {
                operations.dropCollection("tightCouplingEvent");
            }
            CollectionOptions options = new CollectionOptions(5242880, 5000, true);
            operations.createCollection("tightCouplingEvent", options);
        };
    }

}
