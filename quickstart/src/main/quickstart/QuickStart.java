/*
 * Copyright 2015 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package quickstart;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import quickstart.models.AModel;
import quickstart.models.AbstractModelBase;
import quickstart.models.BModel;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

public final class QuickStart {

    /**
     * Run this main method to see the output of this quick example.
     *
     * ./gradlew quickStart
     * ./gradlew quickStart -PconnectionString=mongodb://localhost
     *
     * @param args takes an optional single argument for the connection string
     */
    public static void main(final String[] args) {
        System.out.println("======= Start =======\n");

        String connectionString = args.length > 0 && !args[0].trim().isEmpty() ? args[0] : "mongodb://localhost";
        try(MongoClient client = MongoClients.create(connectionString)) {
            MongoCollection<Document> collection = client.getDatabase("testdb")
                    .getCollection("testcoll");
            collection.drop();

            collection.insertOne(Document.parse("{ _id: { '$oid': '01234567890123456789abcd' }, "
                    + "_discriminator: 'ALPHA', 'onlyForA': 'A-ok'}"));

            CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder()
                            .register(AModel.class)
                            .register(BModel.class)
                            .automatic(true)
                            .build()));

            MongoCollection<AbstractModelBase> modelCollection = collection.withDocumentClass(AbstractModelBase.class)
                    .withCodecRegistry(pojoCodecRegistry);
            System.out.println(" >>> " + modelCollection.find().first());

            // automatic codec registry - will fail as it's not seen the class
            CodecRegistry autoPojoRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                    CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
            modelCollection = modelCollection.withCodecRegistry(autoPojoRegistry);

            try {
                modelCollection.find().first();
                throw new RuntimeException("Surprised to be here. "
                        + "The codec registry doesn't have the custom discriminators registered");
            } catch (CodecConfigurationException e) {
                assert(e.getMessage().contains("Decoding errored with: A class could not be found for the discriminator: 'ALPHA'."));
                System.out.println(" >>  Using automatic only errored as expected - no registered the models in the pojo codec.");
            }
        }

        System.out.println("\n======= Finish =======");

    }

    private QuickStart() {
    }
}
