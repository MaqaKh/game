package com.game.service;

import com.game.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WinningCombinationEvaluatorTest {

    @Test
    void testEvaluateWinnings_SameSymbols() {
        WinningCombinationEvaluator evaluator = new WinningCombinationEvaluator();

        String[][] matrix = {
                {"A", "A", "B"},
                {"A", "B", "B"},
                {"A", "A", "C"}
        };

        GameConfig config = createTestConfigForWinnings();
        Map<String, List<String>> winnings = evaluator.evaluateWinnings(matrix, config);

        assertTrue(winnings.containsKey("A"));
        assertEquals(2, winnings.get("A").size()); // Should find both same_symbol_5_times AND same_symbols_vertically
        assertTrue(winnings.get("A").contains("same_symbol_5_times"));
        assertTrue(winnings.get("A").contains("same_symbols_vertically"));
    }

    @Test
    void testEvaluateWinnings_OnlySameSymbols() {
        WinningCombinationEvaluator evaluator = new WinningCombinationEvaluator();

        // Matrix where A appears 5 times but NOT in any linear pattern
        String[][] matrix = {
                {"A", "B", "C"},
                {"B", "A", "C"},
                {"A", "A", "A"}  // A's are scattered, no full column/row/diagonal
        };

        GameConfig config = createTestConfigForWinnings();
        Map<String, List<String>> winnings = evaluator.evaluateWinnings(matrix, config);

        assertTrue(winnings.containsKey("A"));
        assertEquals(1, winnings.get("A").size());
        assertEquals("same_symbol_5_times", winnings.get("A").get(0));
    }

    private GameConfig createTestConfigForWinnings() {
        Map<String, Symbol> symbols = Map.of(
            "A", new Symbol(5.0, "standard", null, null),
            "B", new Symbol(3.0, "standard", null, null),
            "C", new Symbol(2.5, "standard", null, null)
        );

        Map<String, WinCombination> winCombinations = Map.of(
            "same_symbol_5_times", new WinCombination(2.0, "same_symbols", 5, "same_symbols", null),
            "same_symbols_vertically", new WinCombination(2.0, "linear_symbols", null, "vertically_linear_symbols",
                List.of(List.of("0:0", "1:0", "2:0"), List.of("0:1", "1:1", "2:1"), List.of("0:2", "1:2", "2:2")))
        );

        return new GameConfig(3, 3, symbols, null, winCombinations);
    }
}
