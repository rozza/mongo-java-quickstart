package quickstart.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Map;
import java.util.Objects;

public class MetricsData {

    public static final String VERSION_FIELD = "version";
    public static final String REPEATED_FIELD = "repeated";
    public static final String METRICS_MAP_FIELD = "metricsMap";

    private int _version;
    private boolean _repeated;
    private Map<String, ValueMetricsData> _metricsMap;

    @BsonCreator
    public MetricsData(
            @BsonProperty(VERSION_FIELD) final int pVersion,
            @BsonProperty(REPEATED_FIELD) final boolean pRepeated,
            @BsonProperty(METRICS_MAP_FIELD) final Map<String, ValueMetricsData> pMetricsMap) {
        _version = pVersion;
        _repeated = pRepeated;
        _metricsMap = pMetricsMap;
    }

    public int getVersion() {
        return _version;
    }

    public boolean isRepeated() {
        return _repeated;
    }

    public Map<String, ValueMetricsData> getMetricsMap() {
        return _metricsMap;
    }

    private MetricsData() {
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MetricsData that = (MetricsData) o;
        return _version == that._version && _repeated == that._repeated && Objects.equals(_metricsMap, that._metricsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_version, _repeated, _metricsMap);
    }

    @Override
    public String toString() {
        return "MetricsData{" +
                "_version=" + _version +
                ", _repeated=" + _repeated +
                ", _metricsMap=" + _metricsMap +
                '}';
    }
}