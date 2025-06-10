package com.game.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.game.model.GameConfig;
import com.game.model.GameResult;

import java.io.File;
import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static GameConfig parseConfig(String configPath) throws IOException {
        return objectMapper.readValue(new File(configPath), GameConfig.class);
    }

    public static String toJson(GameResult result) throws IOException {
        return objectMapper.writeValueAsString(result);
    }
}
