package at.spengergasse.backend.seeder;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDateTimeGenerator {

    public static LocalDateTime getRandomDateTime(LocalDateTime startInclusive, LocalDateTime endExclusive) {
        long startEpochSecond = startInclusive.toEpochSecond(java.time.ZoneOffset.UTC);
        long endEpochSecond = endExclusive.toEpochSecond(java.time.ZoneOffset.UTC);
        long randomEpochSecond = ThreadLocalRandom.current().nextLong(startEpochSecond, endEpochSecond);

        return LocalDateTime.ofEpochSecond(randomEpochSecond, 0, java.time.ZoneOffset.UTC);
    }
}
