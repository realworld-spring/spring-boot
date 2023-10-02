package real.world.domain.global;

import lombok.Getter;

@Getter
public class Page {

    private final int offset;

    private final int limit;

    public Page(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

}
