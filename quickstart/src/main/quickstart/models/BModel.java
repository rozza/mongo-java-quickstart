package quickstart.models;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

import java.util.Objects;

@BsonDiscriminator(key = AbstractModelBase.DISCRIMINATOR_KEY, value = "BRAVO")
public class BModel extends AbstractModelBase {

    private String onlyForB;

    @Override
    public String getSomeSharedMethod() {
        return "BBB";
    }

    public String getOnlyForB() {
        return onlyForB;
    }

    public void setOnlyForB(String onlyForB) {
        this.onlyForB = onlyForB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BModel bModel = (BModel) o;
        return Objects.equals(onlyForB, bModel.onlyForB);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(onlyForB);
    }

    @Override
    public String toString() {
        return "BModel{" +
                "onlyForB='" + onlyForB + '\'' +
                '}';
    }
}
