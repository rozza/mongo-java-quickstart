package quickstart.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Map;

public class MetricsData {

    private final Map<String, ValueMetricsData> metricsMap;

    @BsonCreator
    public MetricsData(
            @BsonProperty("metrics") final Map<String, ValueMetricsData> metricsMap) {
        this.metricsMap = metricsMap;
    }

    public Map<String, ValueMetricsData> getMetricsMap() {
        return metricsMap;
    }
}