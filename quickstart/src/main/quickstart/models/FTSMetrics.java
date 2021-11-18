package quickstart.models;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class FTSMetrics {

    public static final String VERSION_FIELD = "version";
    public static final String REPEATED_FIELD = "repeated";
    public static final String DISK_METRICS_FIELD = "diskMetrics";

    private int _version;

    private boolean _repeated;

    private Map<String, ProcessDiskMetrics> _processDiskMetrics;

    private FTSMetrics() {}

    @BsonCreator
    public FTSMetrics(
            @BsonProperty(VERSION_FIELD) final int pVersion,
            @BsonProperty(REPEATED_FIELD) final boolean pRepeated,
            @BsonProperty(DISK_METRICS_FIELD) final Map<String, ProcessDiskMetrics> pDiskMetrics) {
        _version = pVersion;
        _repeated = pRepeated;
        _processDiskMetrics = pDiskMetrics;
    }

    public int getVersion() {
        return _version;
    }

    public boolean isRepeated() {
        return _repeated;
    }

    @BsonProperty(DISK_METRICS_FIELD)
    public Map<String, ProcessDiskMetrics> getProcessDiskMetrics() {
        return _processDiskMetrics;
    }

    public Set<String> getProcessDiskHostnameAndPorts() {
        return _processDiskMetrics == null ? Collections.emptySet() : _processDiskMetrics.keySet();
    }
}