package com.game.service;

import com.game.helper.RandomGenerator;
import com.game.model.BonusSymbolProbability;
import com.game.model.GameConfig;
import com.game.model.CellProbability;

import java.util.List;
import java.util.Optional;

public class MatrixGenerator {
    private final RandomGenerator randomGenerator;

    public MatrixGenerator() {
        this.randomGenerator = new RandomGenerator();
    }

    public MatrixGenerator(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    public String[][] generateMatrix(GameConfig config) {
        String[][] matrix = new String[config.rows()][config.columns()];

        // Fill matrix with standard symbols based on probabilities
        for (int row = 0; row < config.rows(); row++) {
            for (int col = 0; col < config.columns(); col++) {
                String symbol = selectSymbolForCell(row, col, config.probabilities().standardSymbols());
                matrix[row][col] = symbol;
            }
        }

        // Randomly place bonus symbol if applicable
        String bonusSymbol = selectBonusSymbol(config.probabilities().bonusSymbols());
        if (bonusSymbol != null && !"MISS".equals(bonusSymbol)) {
            placeBonusSymbol(matrix, bonusSymbol);
        }

        return matrix;
    }

    private String selectSymbolForCell(int row, int col, List<CellProbability> standardSymbols) {
        CellProbability cellProb = null;
        for (CellProbability prob : standardSymbols) {
            if (prob.row() == row && prob.column() == col) {
                cellProb = prob;
                break;
            }
        }

        // If not found, use default (row 0, col 0) or first available
        if (cellProb == null) {
            for (CellProbability prob : standardSymbols) {
                if (prob.row() == 0 && prob.column() == 0) {
                    cellProb = prob;
                    break;
                }
            }
        }

        // If still not found, use the first available
        if (cellProb == null && !standardSymbols.isEmpty()) {
            cellProb = standardSymbols.get(0);
        }

        if (cellProb != null) {
            return randomGenerator.selectSymbolByWeight(cellProb.symbols());
        }

        return "F"; // Default fallback
    }

    private String selectBonusSymbol(BonusSymbolProbability bonusSymbols) {
        if (bonusSymbols == null || bonusSymbols.symbols() == null) {
            return null;
        }
        return randomGenerator.selectSymbolByWeight(bonusSymbols.symbols());
    }

    private void placeBonusSymbol(String[][] matrix, String bonusSymbol) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int totalCells = rows * cols;

        int randomCellIndex = randomGenerator.nextInt(totalCells);
        int row = randomCellIndex / cols;
        int col = randomCellIndex % cols;

        matrix[row][col] = bonusSymbol;
    }
}

