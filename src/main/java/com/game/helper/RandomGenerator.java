package com.game.helper;

import java.util.Map;
import java.util.Random;

public class RandomGenerator {
    private final Random random;

    public RandomGenerator() {
        this.random = new Random();
    }

    public String selectSymbolByWeight(Map<String, Integer> symbolWeights) {
        if (symbolWeights == null || symbolWeights.isEmpty()) {
            return null;
        }

        int totalWeight = symbolWeights.values().stream()
            .mapToInt(Integer::intValue)
            .sum();

        if (totalWeight == 0) {
            return null;
        }

        int randomValue = random.nextInt(totalWeight);
        int currentWeight = 0;

        for (Map.Entry<String, Integer> entry : symbolWeights.entrySet()) {
            currentWeight += entry.getValue();
            if (randomValue < currentWeight) {
                return entry.getKey();
            }
        }

        return null;
    }

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

}
