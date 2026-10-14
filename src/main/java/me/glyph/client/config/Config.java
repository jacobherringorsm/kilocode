package me.glyph.client.config;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public Map<String, Boolean> moduleStates = new HashMap<>();
    public Map<String, Integer> moduleKeys = new HashMap<>();
    public Map<String, Object> settings = new HashMap<>();
}
