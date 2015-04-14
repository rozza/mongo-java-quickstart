package quickstart.models;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

import java.util.Objects;

@BsonDiscriminator(key = AbstractModelBase.DISCRIMINATOR_KEY, value = "ALPHA")
public class AModel extends AbstractModelBase {

    private String onlyForA;

    @Override
    public String getSomeSharedMethod() {
        return "AModel";
    }

    public String getOnlyForA() {
        return onlyForA;
    }

    public void setOnlyForA(String onlyForA) {
        this.onlyForA = onlyForA;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AModel aModel = (AModel) o;
        return Objects.equals(onlyForA, aModel.onlyForA);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(onlyForA);
    }

    @Override
    public String toString() {
        return "AModel{" +
                "onlyForA='" + onlyForA + '\'' +
                '}';
    }
}
