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
import org.bson.codecs.pojo.ClassModel;
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

        MetricsData metricsData = new MetricsData(
                new LinkedHashMap<>() {{
                    put("a1", new ValueMetricsData(1, 1));
                    put("b1", new ValueMetricsData(2, 2));
                }});

        System.out.println(ClassModel.builder(MetricsData.class).build().getPropertyModels());
    }

    private QuickStart() {
    }
}
