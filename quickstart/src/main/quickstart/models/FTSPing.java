package quickstart.models;

import java.util.List;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class FTSPing {
    @BsonProperty("data")
    public List<FTSMetrics> data;

    public FTSPing() {}
}