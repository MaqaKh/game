# Scratch Game Application

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   ├── com/scratchgame/
│   │   │   ├── ScratchGameApplication.java          // Main entry point
│   │   │   ├── model/                               // Data models (Records)
│   │   │   │   ├── GameConfig.java                  // Root config record
│   │   │   │   ├── Symbol.java                      // Symbol definition
│   │   │   │   ├── Probabilities.java               // Probability definitions
│   │   │   │   ├── WinCombination.java              // Win combination rules
│   │   │   │   ├── GameResult.java                  // Game output result
│   │   │   │   └── CellProbability.java             // Cell-specific probabilities
│   │   │   ├── service/                             // Business logic
│   │   │   │   ├── ConfigParser.java                // JSON config parser
│   │   │   │   ├── MatrixGenerator.java             // Matrix generation logic
│   │   │   │   ├── WinningCombinationEvaluator.java // Win detection
│   │   │   │   ├── RewardCalculator.java            // Reward calculation
│   │   │   │   └── GameEngine.java                  // Main game orchestrator
│   │   │   └── util/
│   │   │       ├── RandomGenerator.java             // Probability-based random
│   │   │       └── JsonUtil.java                    // JSON utilities
│   │   └── resources/
│   │       └── config.json                          // Sample config
│   └── test/
│       └── java/
│           └── com/scratchgame/
│               ├── service/
│               └── util/
```

## Core Classes

### MatrixGenerator.java

**Purpose:** Generate the game matrix based on probabilities

**Key Methods:**
- `generateMatrix(GameConfig config)` → `String[][]`
- `selectSymbolForCell(int row, int col, List<CellProbability> probabilities)` → `String`
- `selectBonusSymbol(BonusSymbolProbability bonusProb)` → `String`
- `calculateWeightedRandom(Map<String, Integer> symbolWeights)` → `String`

### WinningCombinationEvaluator.java

**Purpose:** Detect winning combinations in the matrix

**Key Methods:**
- `evaluateWinnings(String[][] matrix, GameConfig config)` → `Map<String, List<String>>`
- `checkSameSymbolCombinations(String[][] matrix, Map<String, WinCombination> winCombos)` → `Map<String, List<String>>`
- `checkLinearCombinations(String[][] matrix, Map<String, WinCombination> winCombos)` → `Map<String, List<String>>`
- `countSymbolOccurrences(String[][] matrix, String symbol)` → `int`

### RewardCalculator.java

**Purpose:** Calculate final reward based on winning combinations

**Key Methods:**
- `calculateReward(double betAmount, Map<String, List<String>> winnings, String bonusSymbol, GameConfig config)` → `double`
- `calculateSymbolReward(String symbol, List<String> combinations, double betAmount, GameConfig config)` → `double`
- `applyBonusSymbol(double baseReward, String bonusSymbol, GameConfig config)` → `double`

### GameEngine.java

**Purpose:** Orchestrate the entire game flow

**Key Methods:**
- `playGame(GameConfig config, double betAmount)` → `GameResult`
- `hasWinningCombinations(Map<String, List<String>> winnings)` → `boolean`

## Algorithm: Weighted Random Selection

The app uses a **Weighted Random Selection Algorithm** for symbol generation:

```java
// Weighted Random Selection Algorithm
// 1. Calculate total weight
// 2. Generate random value from 0 to totalWeight
// 3. Initialize currentWeight = 0
// 4. Loop through entries, adding weight to currentWeight
// 5. Return entry when randomValue < currentWeight
```

### Example:
Given symbols with weights: A(1), B(2), C(3), D(4), E(5), F(6)

1. **Total Weight** = 21 (1+2+3+4+5+6)
2. **Random Range** = 0 to 20 (21 possible values)
3. **Symbol Ranges:**
    - A: Gets number [0] → 1 number out of 21 → 4.8% chance
    - B: Gets numbers [1,2] → 2 numbers out of 21 → 9.5% chance
    - C: Gets numbers [3,4,5] → 3 numbers out of 21 → 14.3% chance
    - D: Gets numbers [6,7,8,9] → 4 numbers out of 21 → 19.0% chance
    - E: Gets numbers [10,11,12,13,14] → 5 numbers out of 21 → 23.8% chance
    - F: Gets numbers [15,16,17,18,19,20] → 6 numbers out of 21 → 28.6% chance

## Building and Running

### Build the Application

Create a production JAR:
```bash
./gradlew clean release
```

This command will:
- Clean previous builds
- Compile the code
- Run tests
- Create a fat JAR with all dependencies

### Run the Application

Execute the JAR file:
```bash
java -jar build/libs/game-prod.jar --config config.json --betting-amount  10
```

**Parameters:**
- `--config`: Path to the game configuration JSON file
- `--bet`: Bet amount for the game

### Development Commands

```bash
# Build only
./gradlew build

# Run tests
./gradlew test

# Create shadow JAR only
./gradlew shadowJar
```