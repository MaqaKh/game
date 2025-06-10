package com.game.service;

import com.game.model.GameConfig;
import com.game.model.Symbol;
import com.game.model.WinCombination;

import java.util.*;

public class WinningCombinationEvaluator {

    public Map<String, List<String>> evaluateWinnings(String[][] matrix, GameConfig config) {
        Map<String, List<String>> allWinnings = new HashMap<>();

        // Check same symbol combinations
        Map<String, List<String>> sameSymbolWinnings = checkSameSymbolCombinations(matrix, config);
        mergeWinnings(allWinnings, sameSymbolWinnings);

        // Check linear combinations
        Map<String, List<String>> linearWinnings = checkLinearCombinations(matrix, config);
        mergeWinnings(allWinnings, linearWinnings);

        return allWinnings;
    }

    private Map<String, List<String>> checkSameSymbolCombinations(String[][] matrix, GameConfig config) {
        Map<String, List<String>> winnings = new HashMap<>();
        Map<String, Integer> symbolCounts = countSymbolOccurrences(matrix, config);

        // Group win combinations by group to apply only the best one per group
        Map<String, List<Map.Entry<String, WinCombination>>> combinationGroups = new HashMap<>();

        config.winCombinations().forEach((name, combination) -> {
            if ("same_symbols".equals(combination.when())) {
                String group = combination.group();
                combinationGroups.computeIfAbsent(group, k -> new ArrayList<>())
                    .add(Map.entry(name, combination));
            }
        });

        // For each symbol, check which same symbol combinations it qualifies for
        symbolCounts.forEach((symbol, count) -> {
            combinationGroups.forEach((group, combinations) -> {
                // Sort combinations by count descending to get the best one first
                combinations.sort((a, b) -> Integer.compare(b.getValue().count() == null ? 0 : b.getValue().count(),
                                                            a.getValue().count() == null ? 0 : a.getValue().count()));

                for (Map.Entry<String, WinCombination> entry : combinations) {
                    WinCombination combination = entry.getValue();
                    // Only consider combinations with non-null count
                    if (combination.count() != null && count >= combination.count()) {
                        winnings.computeIfAbsent(symbol, k -> new ArrayList<>())
                            .add(entry.getKey());
                        break; // Only apply the best combination from this group
                    }
                }
            });
        });

        return winnings;
    }

    private Map<String, List<String>> checkLinearCombinations(String[][] matrix, GameConfig config) {
        Map<String, List<String>> winnings = new HashMap<>();

        config.winCombinations().forEach((name, combination) -> {
            if ("linear_symbols".equals(combination.when()) && combination.coveredAreas() != null) {
                for (List<String> area : combination.coveredAreas()) {
                    String firstSymbol = getSymbolAt(matrix, area.get(0));

                    if (firstSymbol != null && isStandardSymbol(firstSymbol, config)) {
                        boolean allSame = area.stream()
                            .allMatch(pos -> firstSymbol.equals(getSymbolAt(matrix, pos)));

                        if (allSame) {
                            winnings.computeIfAbsent(firstSymbol, k -> new ArrayList<>())
                                .add(name);
                        }
                    }
                }
            }
        });

        return winnings;
    }

    private Map<String, Integer> countSymbolOccurrences(String[][] matrix, GameConfig config) {
        Map<String, Integer> counts = new HashMap<>();

        for (String[] row : matrix) {
            for (String symbol : row) {
                if (isStandardSymbol(symbol, config)) {
                    counts.put(symbol, counts.getOrDefault(symbol, 0) + 1);
                }
            }
        }

        return counts;
    }

    private String getSymbolAt(String[][] matrix, String position) {
        try {
            String[] parts = position.split(":");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);

            if (row >= 0 && row < matrix.length && col >= 0 && col < matrix[0].length) {
                return matrix[row][col];
            }
        } catch (Exception e) {
            // Invalid position format
        }
        return null;
    }

    private boolean isStandardSymbol(String symbol, GameConfig config) {
        Symbol symbolConfig = config.symbols().get(symbol);
        return symbolConfig != null && "standard".equals(symbolConfig.type());
    }

    private void mergeWinnings(Map<String, List<String>> target, Map<String, List<String>> source) {
        source.forEach((symbol, combinations) -> {
            target.computeIfAbsent(symbol, k -> new ArrayList<>()).addAll(combinations);
        });
    }
}
