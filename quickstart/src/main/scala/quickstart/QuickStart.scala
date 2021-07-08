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
package quickstart

import com.mongodb.{ServerApi, ServerApiVersion}
import com.mongodb.connection.{ClusterDescription, ServerVersion}
import com.mongodb.event.{ClusterClosedEvent, ClusterDescriptionChangedEvent, ClusterListener, ClusterOpeningEvent, CommandFailedEvent, CommandListener}
import org.mongodb.scala._
import org.mongodb.scala.model.{Projections, UpdateOptions}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.util.{Failure, Success, Using}

/**
 * Quick Start test
 */
object QuickStart {
  val logger: Logger = LoggerFactory.getLogger("AppLogger")

  /**
   * Run this main method to see the output of this quick example.
   *
   * @param args takes an optional single argument for the connection string
   * @throws Throwable if an operation fails
   */
  def main(args: Array[String]): Unit = {
    val cleanedArgs = args.toSeq.filter(s => s.trim.nonEmpty)
    val uri = if (cleanedArgs.isEmpty) "mongodb://localhost/" else cleanedArgs.head
    val mongoClientSettings = MongoClientSettings.builder().applyConnectionString(ConnectionString(uri))
      .serverApi(ServerApi.builder().version(ServerApiVersion.V1).build())
      .build()

    Using(MongoClient(mongoClientSettings)) { mongoClient =>

      // get handle to "mydb" database
      val database: MongoDatabase = mongoClient.getDatabase("mydb")

      // get a handle to the "test" collection
      val collection: MongoCollection[Document] = database.getCollection("test")

      val insert = collection.insertOne(Document("hello" -> "world"))
      Await.ready(insert.toFuture(), 1.minute)
      logger.info("Finished")
    }
  }



}