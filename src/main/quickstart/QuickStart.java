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

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.embedded.client.MongoClientSettings;
import com.mongodb.embedded.client.MongoClients;
import com.mongodb.embedded.client.MongoEmbeddedSettings;
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
        //System.out.println(args[0]);
        MongoClients.init(MongoEmbeddedSettings.builder().libraryPath("/home/rozza/Code/mongodb/mongo-embedded-sdk/lib").build());
        MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder().dbPath("/tmp/mongotest").build());


        // get handle to "mydb" database
        MongoDatabase database = mongoClient.getDatabase("mydb");


        // get a handle to the "test" collection
        MongoCollection<Document> collection = database.getCollection("test");

        System.out.println("Collection has " + collection.count() + " documents");

        // close resources
        mongoClient.close();
        MongoClients.close();
        System.out.println("======= Finish =======");

    }

    private QuickStart() {
    }
}
