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

import com.mongodb.MongoClientSettings;
import org.bson.BsonBinaryReader;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.RawBsonDocument;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import quickstart.models.DataHolder;
import quickstart.models.MetricsData;
import quickstart.models.ValueMetricsData;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class QuickStart {

    /**
     * Run this main method to see the output of this quick example.
     */
    public static void main(final String[] args) {
        System.out.println("======= Start =======");

        CodecRegistry codecRegistry = fromRegistries(
                fromProviders(PojoCodecProvider.builder().register("quickstart.models").build()),
                MongoClientSettings.getDefaultCodecRegistry());

        System.out.println(" Create initial data and convert to base64 bytes ");

        DataHolder myDataHolder = new DataHolder(
                asList(new MetricsData(1, true,
                                       new LinkedHashMap<String, ValueMetricsData>() {{
                                           put("a1", new ValueMetricsData(1, 1));
                                           put("b1", new ValueMetricsData(2, 2));
                                       }}),
                       new MetricsData(2, true,
                                       new LinkedHashMap<String, ValueMetricsData>() {{
                                           put("a2", new ValueMetricsData(11, 11));
                                           put("b2", new ValueMetricsData(22, 22));
                                       }})));

        BsonDocument myDataHolderAsBsonDocument = BsonDocumentWrapper.asBsonDocument(myDataHolder, codecRegistry);
        System.out.println(" " + myDataHolderAsBsonDocument.toJson());

        assertEquals(
                BsonDocument.parse("""
                {"data": [
                {"metricsMap": {"a1": {"someDataField": 1, "timestamp": 1}, "b1": {"someDataField": 2, "timestamp": 2}}, "repeated": true, "version": 1},
                {"metricsMap": {"a2": {"someDataField": 11, "timestamp": 11}, "b2": {"someDataField": 22, "timestamp": 22}}, "repeated": true, "version": 2}
                ]}"""),
                myDataHolderAsBsonDocument);

        RawBsonDocument rawBsonDocument = RawBsonDocument.parse(myDataHolderAsBsonDocument.toJson());
        String base64StringData =  Base64.getEncoder().encodeToString(rawBsonDocument.getByteBuffer().array());
        System.out.println(" Base64 String = " + base64StringData);
        byte[] decodedBytes = Base64.getDecoder().decode(base64StringData);

        // Confirm round trip to and from Base64 String.
        assertEquals(rawBsonDocument, new RawBsonDocument(decodedBytes));

        System.out.println(" ====================== ");
        System.out.println(" Using package based codec registry ");
        BsonBinaryReader reader = new BsonBinaryReader(ByteBuffer.wrap(decodedBytes));
        DataHolder dataHolderFromBytes = codecRegistry.get(DataHolder.class).decode(reader, DecoderContext.builder().build());

        assertEquals(myDataHolder, dataHolderFromBytes);
        System.out.println(" " + dataHolderFromBytes);
        System.out.println(" OK!");

        System.out.println(" ====================== ");
        System.out.println(" Check using automatic codec registry ");
        CodecRegistry codecRegistry2 = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        BsonBinaryReader reader2 = new BsonBinaryReader(ByteBuffer.wrap(decodedBytes));
        DataHolder dataHolderFromBytesUsingCodecRegistry2 = codecRegistry2.get(DataHolder.class).decode(reader2, DecoderContext.builder().build());

        assertEquals(myDataHolder, dataHolderFromBytesUsingCodecRegistry2);
        System.out.println(" " + dataHolderFromBytesUsingCodecRegistry2);
        System.out.println(" OK!");
        System.out.println(" ====================== ");
        System.out.println(" Check using provider ");
        CodecProvider provider = PojoCodecProvider.builder().automatic(true).build();
        BsonBinaryReader reader3 = new BsonBinaryReader(ByteBuffer.wrap(decodedBytes));
        DataHolder dataHolderFromProvider = provider.get(DataHolder.class, codecRegistry2).decode(reader3, DecoderContext.builder().build());

        assertEquals(myDataHolder, dataHolderFromProvider);
        System.out.println(" " + dataHolderFromProvider);
        System.out.println(" OK!");
        System.out.println("======= Finish =======");
    }

    private QuickStart() {
    }
}
