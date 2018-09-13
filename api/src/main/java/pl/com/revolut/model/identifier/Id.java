package pl.com.revolut.model.identifier;

import lombok.Data;
import lombok.NonNull;

@Data
public abstract class Id {
    protected transient String DASH = "-";
    @NonNull
    String id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Id id1 = (Id) o;

        return id != null ? id.equals(id1.id) : id1.id == null;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
