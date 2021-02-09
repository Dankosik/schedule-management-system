package com.foxminded.university.management.schedule.service.data.generation.utils;

import java.util.Random;

public class RandomUtils {
    private static final Random random = new Random();

    public static int random(int min, int max) {
        return max - min == 0 ? 0 : min + random.nextInt(max - min);
    }
}
