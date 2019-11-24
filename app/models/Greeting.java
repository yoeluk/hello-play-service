package models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Greeting {
    public String greeting;
    public String name;
    public Greeting(String greeting, String name) {
        this.greeting = greeting;
        this.name = name;
    }
    private Greeting() {}

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
