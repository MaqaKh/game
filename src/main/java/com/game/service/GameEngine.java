package com.game.service;

import com.game.model.GameConfig;
import com.game.model.GameResult;
import com.game.model.Symbol;

import java.util.List;
import java.util.Map;

public class GameEngine {
    private final MatrixGenerator matrixGenerator;
    private final WinningCombinationEvaluator winningEvaluator;
    private final RewardCalculator rewardCalculator;

    public GameEngine() {
        this.matrixGenerator = new MatrixGenerator();
        this.winningEvaluator = new WinningCombinationEvaluator();
        this.rewardCalculator = new RewardCalculator();
    }

    public GameEngine(MatrixGenerator matrixGenerator,
                      WinningCombinationEvaluator winningEvaluator,
                      RewardCalculator rewardCalculator) {
        this.matrixGenerator = matrixGenerator;
        this.winningEvaluator = winningEvaluator;
        this.rewardCalculator = rewardCalculator;
    }

    public GameResult playGame(GameConfig config, double betAmount) {
        // Generate the game matrix
        String[][] matrix = matrixGenerator.generateMatrix(config);

        // Find bonus symbol in the matrix
        String appliedBonusSymbol = findBonusSymbol(matrix, config);

        // Evaluate winning combinations
        Map<String, List<String>> appliedWinningCombinations =
            winningEvaluator.evaluateWinnings(matrix, config);

        // Calculate reward
        double reward = rewardCalculator.calculateReward(
            betAmount, appliedWinningCombinations, appliedBonusSymbol, config);

        // If no winnings, bonus symbol should not be applied (set to null)
        if (appliedWinningCombinations.isEmpty()) {
            appliedBonusSymbol = null;
        }

        return new GameResult(
            matrix,
            reward,
            appliedWinningCombinations,
            appliedBonusSymbol
        );
    }

    private String findBonusSymbol(String[][] matrix, GameConfig config) {
        for (String[] row : matrix) {
            for (String symbol : row) {
                if (isBonusSymbol(symbol, config)) {
                    return symbol;
                }
            }
        }
        return null;
    }

    private boolean isBonusSymbol(String symbol, GameConfig config) {
        Symbol symbolConfig = config.symbols().get(symbol);
        return symbolConfig != null && "bonus".equals(symbolConfig.type());
    }
}
