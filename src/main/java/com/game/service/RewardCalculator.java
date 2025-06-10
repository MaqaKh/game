package com.game.service;

import com.game.model.GameConfig;
import com.game.model.Symbol;
import com.game.model.WinCombination;

import java.util.List;
import java.util.Map;

public class RewardCalculator {

    public double calculateReward(double betAmount, Map<String, List<String>> winnings,
                                  String bonusSymbol, GameConfig config) {
        if (winnings.isEmpty()) {
            return 0.0;
        }

        double totalReward = 0.0;

        for (Map.Entry<String, List<String>> entry : winnings.entrySet()) {
            String symbol = entry.getKey();
            List<String> combinations = entry.getValue();

            double symbolReward = calculateSymbolReward(symbol, combinations, betAmount, config);
            totalReward += symbolReward;
        }

        if (totalReward > 0 && bonusSymbol != null) {
            totalReward = applyBonusSymbol(totalReward, bonusSymbol, config);
        }

        return totalReward;
    }

    private double calculateSymbolReward(String symbol, List<String> combinations,
                                         double betAmount, GameConfig config) {
        Symbol symbolConfig = config.symbols().get(symbol);
        if (symbolConfig == null || symbolConfig.rewardMultiplier() == null) {
            return 0.0;
        }

        double reward = betAmount * symbolConfig.rewardMultiplier();

        for (String combinationName : combinations) {
            WinCombination combination = config.winCombinations().get(combinationName);
            if (combination != null) {
                reward *= combination.rewardMultiplier();
            }
        }

        return reward;
    }

    private double applyBonusSymbol(double baseReward, String bonusSymbol, GameConfig config) {
        Symbol bonusConfig = config.symbols().get(bonusSymbol);
        if (bonusConfig == null) {
            return baseReward;
        }

        String impact = bonusConfig.impact();
        if (impact == null) {
            return baseReward;
        }

        switch (impact) {
            case "multiply_reward":
                if (bonusConfig.rewardMultiplier() != null) {
                    return baseReward * bonusConfig.rewardMultiplier();
                }
                break;
            case "extra_bonus":
                if (bonusConfig.extra() != null) {
                    return baseReward + bonusConfig.extra();
                }
                break;
            case "miss":
            default:
                break;
        }

        return baseReward;
    }
}
