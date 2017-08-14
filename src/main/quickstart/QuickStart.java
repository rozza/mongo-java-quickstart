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

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.mongodb.client.model.Filters.eq;
import static java.lang.String.format;

public final class QuickStart {

    private static final Bson QUERY = eq("_id", 1);
    private static final Map<Integer, String> entry = Collections.synchronizedMap(new LinkedHashMap<Integer, String>());
    private static final Map<Integer, String> read = Collections.synchronizedMap(new LinkedHashMap<Integer, String>());

    /**
     * Run this main method to see the output of this quick example.
     *
     * ./gradlew quickStart
     * ./gradlew quickStart -DfindAndModify=true
     *
     * @param args takes an optional single argument for the findAndModify
     */
    public static void main(final String[] args) {
        boolean findAndModify = args.length == 1 && Boolean.parseBoolean(args[0]);
        MongoClient mongoClient = new MongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("coll");

        System.out.println("== Initialising the collection ==");
        mongoCollection.drop();
        Document original = Document.parse("{_id: 1, a: 'A_00', b: '00'}");
        mongoCollection.insertOne(original);

        System.out.println(format("Original document: %s",original.toJson()));
        System.out.println(format("Using FindAndModify: %s", findAndModify));

        ExecutorService executor = Executors.newFixedThreadPool(100);

        for (int i = 0; i < 20; i++) {
            Runnable worker = new WorkerThread(i, mongoCollection, findAndModify);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Busy loop
        }

        System.out.println("\nThreads incoming order:\n--------------------------");
        for (Map.Entry<Integer, String> m : entry.entrySet()) {
            System.out.println(format("workerId: %02d | %s", m.getKey(), m.getValue()));
        }

        System.out.println("\nAfter DB Update- Records status:\n-------------------------------------");
        for (Map.Entry<Integer, String> m : read.entrySet()) {
            System.out.println(format("workerId: %02d | %s", m.getKey(), m.getValue()));
        }

        System.out.println("\nFinished");
    }

    static class WorkerThread implements Runnable {
        private final int workerId;
        private final MongoCollection<Document> mongoCollection;
        private final boolean findAndModify;

        WorkerThread(final int id, final MongoCollection<Document> mongoCollection, final boolean findAndModify){
            this.workerId = id;
            this.mongoCollection = mongoCollection;
            this.findAndModify = findAndModify;
        }

        public void run() {
            // search document where _id=1 and update it with new values
            Document newDocument = new Document();

            int k = workerId % 2;
            if(k == 0) {
                newDocument.append("a", format("A_%02d", workerId));
            } else {
                newDocument.append("b", format("%02d", workerId));
            }

            Document updateObj = new Document("$set", newDocument);

            entry.put(workerId, format("%s", format("Updating: %s", updateObj.toJson())));

            if (findAndModify) {
                Document updated = mongoCollection.findOneAndUpdate(QUERY, updateObj,
                        new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));
                read.put(workerId, updated.toJson());
            } else {
                mongoCollection.updateOne(QUERY, updateObj);
                mongoCollection.find(QUERY).forEach((Block<Document>) document -> {
                    read.put(workerId, document.toJson());
                });
            }

        }

    }

    private QuickStart() {
    }
}
