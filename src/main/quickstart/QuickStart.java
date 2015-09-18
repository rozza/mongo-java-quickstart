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
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.SocketSettings;
import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.MongoDatabase;
import org.bson.Document;
import rx.Observable;

import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Filters.eq;

public final class QuickStart {

    static MongoClient mongoClient;
    static MongoDatabase database;
    static UpdateOptions upsertOptions = new UpdateOptions().upsert(true); // used for upserts

    /**
     * This code does not run correctly with 3.1.0-SNAPSHOT and Rx driver 1.1.0-SNAPSHOT
     * It should display 'result: true' if it runs correctly.
     */
    public static void main(String[] args) throws Exception {
        //
        initMongo(args);
        boolean result = doMongoStuff().toBlocking().singleOrDefault(false);
        // result should be true
        System.out.println("result: " + result);
        mongoClient.close();
        System.out.println("finished");
        // Netty prevents the exit... force exit
        System.exit(0);
    }

    static void initMongo(final String[] args) {
        // enable Netty mode
        System.setProperty("org.mongodb.async.type", "netty");

        ConnectionString connectionString;
        if (args.length == 0) {
            connectionString = new ConnectionString("mongodb://localhost");
        } else {
            connectionString = new ConnectionString(args[0]);
        }

        ClusterSettings clusterSettings = ClusterSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        SocketSettings socketSettings = SocketSettings.builder()
                .applyConnectionString(connectionString)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .build();

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .clusterSettings(clusterSettings)
                .socketSettings(socketSettings)
                .build();

        mongoClient = MongoClients.create(mongoClientSettings);


        database = mongoClient.getDatabase("test");
    }

    static Observable<Boolean> doMongoStuff() {
        MongoCollection<Document> col = database.getCollection("TestCol");

        // do some Mongo queries
        Document doc = new Document();
        doc.put("_id", "my_doc_id");
        doc.put("name", "I am a doc");

        // insert
        System.out.println("Replacing doc in DB");
        return col.replaceOne(eq("_id", "my_doc_id"), doc, upsertOptions)
                .flatMap(updateResult -> {
                    // load it
                    System.out.println("loading doc");
                    return col.find(eq("_id", "my_doc_id")).first();
                })
                .flatMap(document -> {
                    // change name
                    document.put("name", "new name");

                    // update doc
                    System.out.println("Replacing doc again in DB after 30 sec delay");
                    return col.replaceOne(eq("_id", "my_doc_id"), document, upsertOptions).delay(30, TimeUnit.SECONDS);
                })
                .map(updateResult1 -> {
                    System.out.println("replace completed");
                    return true;
                });

    }

    private QuickStart() {
    }
}
