package Generator;

import java.util.List;

public interface Generator<T> {
    List<T> generator(int size);
}
