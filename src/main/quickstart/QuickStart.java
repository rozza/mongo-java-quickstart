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
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import org.bson.Document;
import rx.Observable;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Filters.eq;

public final class QuickStart {

    /**
     * Run this main method to see the output of this quick example.
     *
     * ./gradlew quickStart
     *
     * @param args takes an optional single argument for the connection string
     * @throws UnknownHostException if
     */
    public static void main(final String[] args) throws InterruptedException {
        System.out.println("======= Start =======");

        // init Mongo
        initMongo(args);

        // setup Vertx
        vertx =  Vertx.vertx();

        DeploymentOptions deployOptions = new DeploymentOptions();
        deployOptions.setInstances(4);
        deployOptions.setMultiThreaded(false);
        deployOptions.setWorker(false);

        System.out.println("Deploying verticles");
        vertx.deployVerticle(new TestVerticle());
        vertx.deployVerticle(new TestVerticle());
        vertx.deployVerticle(new TestVerticle());
        // wait for the last verticle to deploy
        Observable.create(subscriber -> {
            vertx.deployVerticle(new TestVerticle(), result -> {
                subscriber.onCompleted();
            });
        }).toBlocking().singleOrDefault(null);

        System.out.println("Verticles deployed");

        HttpClient client = vertx.createHttpClient();


        System.out.println("waiting delay");
        Observable.just("").delay(10, TimeUnit.MILLISECONDS).toBlocking().single();

        // do a request
        Observable.create(subscriber -> {
            System.out.println("First request");
            client.get(8080, "localhost", "/", event -> {
                subscriber.onCompleted();
            }).end();
        }).toBlocking().singleOrDefault(null);

        // wait 5 secs
        System.out.println("waiting delay");
        Observable.just("").delay(5, TimeUnit.SECONDS).toBlocking().single();

        // do another request
        Observable.create(subscriber -> {
            System.out.println("Second request");
            client.get(8080, "localhost", "/", event -> {
                subscriber.onCompleted();
            }).end();
        }).toBlocking().singleOrDefault(null);

        System.out.println("waiting delay 2");
        Observable.just("").delay(10, TimeUnit.SECONDS).toBlocking().single();

        // do another request
        Observable.create(subscriber -> {
            System.out.println("Third request");
            client.get(8080, "localhost", "/", event -> {
                subscriber.onCompleted();
            }).end();
        }).toBlocking().singleOrDefault(null);

        vertx.close();
        System.out.println("Test finished");

        // Netty prevents the exit... force exit
        System.exit(0);

        // close resources
        mongoClient.close();
        System.out.println("======= Finish =======");

    }

    static Vertx vertx;
    static MongoClient mongoClient;
    static MongoDatabase database;
    static UpdateOptions upsertOptions = new UpdateOptions().upsert(true); // used for upserts

    static class TestVerticle extends AbstractVerticle {
        @Override
        public void start() throws Exception {
            System.out.println("Verticle instance started");

            HttpServerOptions options = new HttpServerOptions()
                    .setCompressionSupported(true)
                    .setPort(8080);

            HttpServer server = vertx.createHttpServer(options);

            server.requestHandler(req -> {
                System.out.println("===== HTTP REQUEST START =====");

                MongoCollection<Document> col = database.getCollection("TestCol");

                // do some Mongo queries
                Document doc = new Document();
                doc.put("_id", "my_doc_id");
                doc.put("name", "I am a doc");

                // insert
                System.out.println("Replacing doc in DB");
                col.replaceOne(eq("_id", "my_doc_id"), doc, upsertOptions)
                        .flatMap(updateResult -> {
                            // load it
                            System.out.println("loading doc");
                            return database.getCollection("TestCol").find(eq("_id", "my_doc_id")).first();
                        })
                        .flatMap(document -> {
                            // change name
                            document.put("name", "new name");

                            // update doc
                            System.out.println("Replacing doc again in DB");
                            return database.getCollection("TestCol").replaceOne(eq("_id", "my_doc_id"), document, upsertOptions);
                        })
                        .map(updateResult1 -> {
                            System.out.println("replace completed");
                            return updateResult1;
                        })
                        .subscribe(
                                o -> {
                                    System.out.println("next: " + o);
                                },
                                throwable -> {
                                    System.out.println("error: " + throwable);
                                    System.out.println("===== HTTP REQUEST FAILURE =====");
                                    req.response().end("error");
                                },
                                () -> {
                                    System.out.println("===== HTTP REQUEST COMPLETE =====");
                                    req.response().end("ended");
                                }
                        );
            });

            server.listen(8080);
        }
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


    /**
     * This code does not run correctly with 3.1.0-SNAPSHOT and Rx driver 1.1.0-SNAPSHOT
     * It should display 'result: true' if it runs correctly.
     */
    public static void main2(String[] args) throws Exception {
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
