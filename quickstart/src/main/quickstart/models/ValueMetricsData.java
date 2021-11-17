package quickstart.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Objects;

public class ValueMetricsData {

    public static final String TIME_FIELD = "timestamp";
    public static final String SOME_DATA_FIELD = "someDataField";

    private Integer _timestamp;
    private Integer _someDataField;

    @BsonCreator
    public ValueMetricsData(
            @BsonProperty(TIME_FIELD) final Integer pTimestamp,
            @BsonProperty(SOME_DATA_FIELD) final Integer pSomeDataField) {
        _timestamp = pTimestamp;
        _someDataField = pSomeDataField;
    }

    public Integer getTimestamp() {
        return _timestamp;
    }

    public Integer getSomeDataField() {
        return _someDataField;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ValueMetricsData that = (ValueMetricsData) o;
        return Objects.equals(_timestamp, that._timestamp) && Objects.equals(_someDataField, that._someDataField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_timestamp, _someDataField);
    }

    @Override
    public String toString() {
        return "ValueMetricsData{" +
                "_timestamp=" + _timestamp +
                ", _someDataField=" + _someDataField +
                '}';
    }

    private ValueMetricsData() {}
}