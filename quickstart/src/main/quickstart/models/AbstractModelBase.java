package quickstart.models;

import com.mongodb.MongoClientSettings;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

@BsonDiscriminator(key = AbstractModelBase.DISCRIMINATOR_KEY)
public abstract class AbstractModelBase {

    public static final String DISCRIMINATOR_KEY = "_discriminator";

    @BsonIgnore
    public abstract String getSomeSharedMethod();

    @BsonId
    private ObjectId id;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
