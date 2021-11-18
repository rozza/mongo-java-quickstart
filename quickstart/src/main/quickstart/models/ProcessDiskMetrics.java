package quickstart.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class ProcessDiskMetrics {

    public static final String DISK_SPACE_USED_FIELD = "diskSpaceUsed";
    public static final String SAMPLE_TIME_FIELD = "sampleTime";

    private Long _diskSpaceUsed;
    private long _sampleTime;

    public Long getDiskSpaceUsed() {
        return _diskSpaceUsed;
    }

    ProcessDiskMetrics() {}

    @BsonCreator
    public ProcessDiskMetrics(
            @BsonProperty(SAMPLE_TIME_FIELD) final long pTimestamp,
            @BsonProperty(DISK_SPACE_USED_FIELD) final Long pDiskSpaceUsed) {
        _diskSpaceUsed = pDiskSpaceUsed;
        _sampleTime = pTimestamp;
    }

    public long getSampleTime() {
        return _sampleTime;
    }
}