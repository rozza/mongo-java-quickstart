// Copyright 2015 MongoDB, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package quickstart

import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, FiniteDuration, SECONDS, TimeUnit}



object QuickStart {

    private val maxWaitTime: FiniteDuration = Duration(5, SECONDS)

    /**
     * Run this main method to see the output of this quick example.
     *
     * ./gradlew quickStart
     * ./gradlew quickStart -PconnectionString=mongodb://localhost
     *
     * @param args takes an optional single argument for the connection string
     */
    def main(args: Array[String]): Unit = {
        println("======= Start =======")

        val mongoClient: MongoClient = if (args.isEmpty) MongoClient() else MongoClient(args.head)

        // get handle to "mydb" database
        val database: MongoDatabase = mongoClient.getDatabase("mydb")

        // get a handle to the "test" collection
        val collection: MongoCollection[Document] = database.getCollection[Document]("test")

        val obs1 = collection.drop()
        val doc: Document = Document(
            "_id" -> 0,
            "name" -> "MongoDB",
            "type" -> "database",
            "count" -> 1,
            "info" -> Document("x" -> 203, "y" -> 102)
        )
        val obs2 = collection.insertOne(doc)


        val setup = for {
            _ <- collection.drop().head()
            _ <- collection.insertOne(doc).head()
        } yield None

        Await.result(setup, maxWaitTime)

        val found = collection.find().first().head()



        println(Await.result(found, maxWaitTime))
        mongoClient.close()
        println("======= Finish =======")

    }


}
