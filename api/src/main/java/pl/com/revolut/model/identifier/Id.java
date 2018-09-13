package pl.com.revolut.model.identifier;

import lombok.Data;
import lombok.NonNull;

/**
 * Core Id of the classes. It consist of the common mathods of the AccountId, CustomerId and TransactionId
 */
@Data
public abstract class Id {
    protected transient String DASH = "-";
    @NonNull
    String id;

    @Override
    /**
     * unfortunately lombok does not provide a perfect equals method so i write for the id
     */
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
