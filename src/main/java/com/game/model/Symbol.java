package com.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Symbol(
    @JsonProperty("reward_multiplier")
    Double rewardMultiplier,
    String type,
    String impact,
    Integer extra
) {}
