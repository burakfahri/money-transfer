package pl.com.revolut.model.identifier;

import lombok.Data;
import lombok.NonNull;

@Data
public abstract class Id {
    protected transient String DASH = "-";
    @NonNull
    String id;
}
