package com.game.service;

import com.game.model.*;
import com.game.helper.RandomGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MatrixGeneratorTest {

    @Test
    void testGenerateMatrix() {
        // Create mock RandomGenerator
        RandomGenerator mockRandom = Mockito.mock(RandomGenerator.class);
        when(mockRandom.selectSymbolByWeight(any())).thenReturn("F");
        when(mockRandom.nextInt(9)).thenReturn(4); // Center cell

        // Create test configuration
        GameConfig config = createTestConfig();

        MatrixGenerator generator = new MatrixGenerator(mockRandom);
        String[][] matrix = generator.generateMatrix(config);

        assertNotNull(matrix);
        assertEquals(3, matrix.length);
        assertEquals(3, matrix[0].length);
    }

    private GameConfig createTestConfig() {
        Map<String, Symbol> symbols = Map.of(
            "F", new Symbol(1.0, "standard", null, null),
            "+500", new Symbol(null, "bonus", "extra_bonus", 500)
        );

        CellProbability cellProb = new CellProbability(0, 0, Map.of("F", 1));
        BonusSymbolProbability bonusProb = new BonusSymbolProbability(Map.of("+500", 1));
        Probabilities probabilities = new Probabilities(List.of(cellProb), bonusProb);

        return new GameConfig(3, 3, symbols, probabilities, Map.of());
    }
}
