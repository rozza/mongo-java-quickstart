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

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;

public final class QuickStart {

    /**
     * Run this main method to see the output of this quick example.
     *
     * ./gradlew quickStart
     * ./gradlew quickStart -PconnectionString=mongodb://localhost
     *
     * @param args takes an optional single argument for the connection string
     * @throws UnknownHostException if
     */
    public static void main(final String[] args) throws UnknownHostException {
        System.out.println("======= Start =======");

        MongoClient mongoClient;

        if (args.length == 0) {
            // connect to the local database server
            mongoClient = new MongoClient();
        } else {
            mongoClient = new MongoClient(new MongoClientURI(args[0]));
        }

        // get handle to "mydb" database
        DB database = mongoClient.getDB("mydb");


        // get a handle to the "test" collection
        DBCollection collection = database.getCollection("test");

        System.out.println("Collection has " + collection.count() + " documents");

        // close resources
        mongoClient.close();
        System.out.println("======= Finish =======");
    }

    private QuickStart() {
    }
}
