package models;

public class Greeting {
    public String greeting;
    public String name;
    public Greeting(String greeting, String name) {
        this.greeting = greeting;
        this.name = name;
    }
    private Greeting() {}
}
