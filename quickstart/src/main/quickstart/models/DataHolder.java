package quickstart.models;

import java.util.List;
import java.util.Objects;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class DataHolder {
    @BsonProperty("data")
    public List<MetricsData> data;

    public DataHolder() {}

    public DataHolder(final List<MetricsData> data) {
        this.data = data;
    }

    public List<MetricsData> getData() {
        return data;
    }

    public DataHolder setData(final List<MetricsData> data) {
        this.data = data;
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final DataHolder that = (DataHolder) o;
        return Objects.equals(getData(), that.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getData());
    }

    @Override
    public String toString() {
        return "DataHolder{" +
                "data=" + data +
                '}';
    }
}