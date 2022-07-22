package edu.uob;

import java.util.concurrent.ThreadLocalRandom;

public class TestTools {

    public static int getTestAccountId() {
        //https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
        // Any 9-digit number beginning with 9 is reserved for testing
        return ThreadLocalRandom.current().nextInt(900000000, 1000000000);
    }

}
