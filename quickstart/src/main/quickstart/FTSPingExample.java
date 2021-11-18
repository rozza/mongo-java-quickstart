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
import com.mongodb.client.MongoClient;
import org.bson.BsonBinaryReader;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.RawBsonDocument;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.ClassModelBuilder;
import org.bson.codecs.pojo.Convention;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import quickstart.models.DataHolder;
import quickstart.models.FTSMetrics;
import quickstart.models.FTSPing;
import quickstart.models.MetricsData;
import quickstart.models.ProcessDiskMetrics;
import quickstart.models.ValueMetricsData;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;

import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class FTSPingExample {

    /**
     * Run this main method to see the output of this quick example.
     */
    public static void main(final String[] args) {
        System.out.println("======= Start =======");

        CodecRegistry codecRegistry = fromRegistries(
                fromProviders(PojoCodecProvider.builder().register("quickstart.models").build()),
                MongoClientSettings.getDefaultCodecRegistry());

        System.out.println(ClassModel.builder(FTSMetrics.class).build().getPropertyModels());

        RawBsonDocument exampleData = RawBsonDocument.parse("{ data: [{ version: 1, repeated: true, diskMetrics: { '1': { sampleTime: 3, diskSpaceUsed: 8 } }}] }");
        String base64StringData =  Base64.getEncoder().encodeToString(exampleData.getByteBuffer().array());
        System.out.println(" Base64 String = " + base64StringData);
        byte[] decodedBytes = Base64.getDecoder().decode(base64StringData);

        BsonBinaryReader binaryReader = new BsonBinaryReader(ByteBuffer.wrap(decodedBytes));
        FTSPing ftsPing =
                codecRegistry
                        .get(FTSPing.class)
                        .decode(binaryReader, DecoderContext.builder().build());
        assertEquals(1, ftsPing.data.size());
        assertNotNull(ftsPing.data.get(0).getProcessDiskMetrics());
        assertEquals(3, ftsPing.data.get(0).getProcessDiskMetrics().get("1").getSampleTime());
        assertEquals(ftsPing.data.get(0).getProcessDiskMetrics().get("1").getDiskSpaceUsed(), (Long) 8L);
    }

    private FTSPingExample() {
    }
}
