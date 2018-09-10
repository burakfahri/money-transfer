package pl.com.revolut.model.identifier;

import lombok.Data;
import lombok.NonNull;

@Data
public abstract class Id {
    @NonNull
    String id;
}
