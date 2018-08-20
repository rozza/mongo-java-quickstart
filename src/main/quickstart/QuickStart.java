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
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

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
        System.out.println("======= Start =======");

        MongoClientSettings.Builder mongoClientSettingsBuilder = MongoClientSettings.builder();
        if (args.length > 0) {
            mongoClientSettingsBuilder.applyConnectionString(new ConnectionString(args[0]));
        }
        MongoClient mongoClient = MongoClients.create(mongoClientSettingsBuilder.build());


        // get handle to "mydb" database
        MongoDatabase database = mongoClient.getDatabase("mydb");


        // get a handle to the "test" collection
        MongoCollection<Document> collection = database.getCollection("test");

        System.out.println("Collection has " + collection.count() + " documents");

        // close resources
        mongoClient.close();
        System.out.println("======= Finish =======");

    }

    private QuickStart() {
    }
}
