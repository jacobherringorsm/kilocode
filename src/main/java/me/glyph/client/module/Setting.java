package me.glyph.client.module;

public class Setting<T> {
    private final String name;
    private final String description;
    private T value;
    private final T min;
    private final T max;
    
    private Setting(String name, String description, T value, T min, T max) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.min = min;
        this.max = max;
    }
    
    public static <T extends Number> Setting<T> number(String name, String description, T value, T min, T max) {
        return new Setting<>(name, description, value, min, max);
    }
    
    public static Setting<Boolean> bool(String name, String description, boolean value) {
        return new Setting<>(name, description, value, null, null);
    }
    
    public static Setting<String> mode(String name, String description, String value, String... modes) {
        return new Setting<>(name, description, value, null, null);
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public T getValue() {
        return value;
    }
    
    public void setValue(T value) {
        this.value = value;
    }
    
    public T getMin() {
        return min;
    }
    
    public T getMax() {
        return max;
    }
    
    public boolean isNumber() {
        return min != null && max != null;
    }
    
    public boolean isBoolean() {
        return value instanceof Boolean;
    }
    
    public boolean isMode() {
        return value instanceof String;
    }
}
