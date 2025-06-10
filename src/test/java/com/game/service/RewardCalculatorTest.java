package com.game.service;

import com.game.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RewardCalculatorTest {

    @Test
    void testCalculateReward_NoWinnings() {
        RewardCalculator calculator = new RewardCalculator();
        GameConfig config = createTestConfigForReward();

        double reward = calculator.calculateReward(100.0, Map.of(), "+1000", config);

        assertEquals(0.0, reward);
    }

    @Test
    void testCalculateReward_WithWinnings() {
        RewardCalculator calculator = new RewardCalculator();
        GameConfig config = createTestConfigForReward();

        Map<String, List<String>> winnings = Map.of(
            "A", List.of("same_symbol_5_times", "same_symbols_vertically")
        );

        double reward = calculator.calculateReward(100.0, winnings, "+1000", config);

        // Expected: 100 * 5 * 2 * 2 + 1000 = 3000
        assertEquals(3000.0, reward);
    }

    private GameConfig createTestConfigForReward() {
        Map<String, Symbol> symbols = Map.of(
            "A", new Symbol(5.0, "standard", null, null),
            "+1000", new Symbol(null, "bonus", "extra_bonus", 1000)
        );

        Map<String, WinCombination> winCombinations = Map.of(
            "same_symbol_5_times", new WinCombination(2.0, "same_symbols", 5, "same_symbols", null),
            "same_symbols_vertically", new WinCombination(2.0, "linear_symbols", null, "vertically_linear_symbols", null)
        );

        return new GameConfig(3, 3, symbols, null, winCombinations);
    }
}
