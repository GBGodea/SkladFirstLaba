package Generator;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ThreadLocalRandom;

public class TimeGenerator {
    public static LocalDateTime[] between() {
        LocalDateTime rangeStart = LocalDateTime.of(2000, 1, 1, 0, 0);
        LocalDateTime rangeEnd = LocalDateTime.now();

        long minOffsetSeconds = 60;
        long maxOffsetSeconds = 24 * 3600;

        long startEpoch = rangeStart.toEpochSecond(ZoneOffset.UTC);
        long endEpoch = rangeEnd.toEpochSecond(ZoneOffset.UTC);

        long firstEpoch = ThreadLocalRandom.current().nextLong(startEpoch, endEpoch);
        LocalDateTime firstDate = LocalDateTime.ofEpochSecond(firstEpoch, 0, ZoneOffset.UTC);

        long offset = ThreadLocalRandom.current().nextLong(minOffsetSeconds, maxOffsetSeconds + 1);
        LocalDateTime secondDate = firstDate.plusSeconds(offset);

        return new LocalDateTime[]{firstDate, secondDate};
    }


}
