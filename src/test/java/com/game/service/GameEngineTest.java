package com.game.service;

import com.game.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

class GameEngineTest {

    @Test
    void testPlayGame() {
        // Create mocks
        MatrixGenerator mockMatrixGenerator = Mockito.mock(MatrixGenerator.class);
        WinningCombinationEvaluator mockWinningEvaluator = Mockito.mock(WinningCombinationEvaluator.class);
        RewardCalculator mockRewardCalculator = Mockito.mock(RewardCalculator.class);

        // Configure mocks
        String[][] testMatrix = {
            {"A", "A", "B"},
            {"A", "+1000", "B"},
            {"A", "A", "B"}
        };

        Map<String, List<String>> testWinnings = Map.of(
            "A", List.of("same_symbol_5_times", "same_symbols_vertically"),
            "B", List.of("same_symbol_3_times", "same_symbols_vertically")
        );

        when(mockMatrixGenerator.generateMatrix(any())).thenReturn(testMatrix);
        when(mockWinningEvaluator.evaluateWinnings(any(), any())).thenReturn(testWinnings);
        when(mockRewardCalculator.calculateReward(anyDouble(), any(), any(), any())).thenReturn(6600.0);

        // Create game engine with mocks
        GameEngine gameEngine = new GameEngine(mockMatrixGenerator, mockWinningEvaluator, mockRewardCalculator);

        // Create test config
        GameConfig config = createTestGameConfig();

        // Play game
        GameResult result = gameEngine.playGame(config, 100.0);

        // Assertions
        assertNotNull(result);
        assertEquals(6600.0, result.reward());
        assertEquals("+1000", result.appliedBonusSymbol());
        assertArrayEquals(testMatrix, result.matrix());
        assertEquals(testWinnings, result.appliedWinningCombinations());
    }

    private GameConfig createTestGameConfig() {
        Map<String, Symbol> symbols = Map.of(
            "A", new Symbol(5.0, "standard", null, null),
            "B", new Symbol(3.0, "standard", null, null),
            "+1000", new Symbol(null, "bonus", "extra_bonus", 1000)
        );

        return new GameConfig(3, 3, symbols, null, Map.of());
    }
}
