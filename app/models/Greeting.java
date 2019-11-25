package models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Greeting {
    public final String greeting;
    public final String message;

    /**
     * legacy greeting store for HelloController
     */
    private static ConcurrentHashMap<String, Object> greetings;

    private static Object stub() {
        return new Object();
    }

    public static void addGreeting(String greeting) {
        if (greetings == null) {
            greetings = new ConcurrentHashMap<>();
        }
        greetings.put(greeting, stub());
    }

    public static List<String> allGreetings() {
        if (greetings == null) {
            greetings = new ConcurrentHashMap<>();
        }
        return new ArrayList<>(greetings.keySet());
    }
}
