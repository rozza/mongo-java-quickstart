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

import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;

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

        try(MongoClient client = MongoClients.create(mongoClientSettingsBuilder.build())) {
            MongoCollection<Document> collection = client.getDatabase("testdb")
                    .getCollection("testcoll");

            final List<String> aList = asList("a", "list", "of", "strings");
            Document document = new Document();
            document.put("_id", 1);
            document.put("value1", aList);
            document.put("value2", "New York");
            document.append("date", new Date());
            collection.insertOne(document);
        }

        System.out.println("======= Finish =======");

    }

    private QuickStart() {
    }
}
