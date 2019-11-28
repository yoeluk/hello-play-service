package models;

import javax.persistence.*;

@Entity
@Table(name = "greeting_table", schema = "public")
public class Greeting {

    @Id
    @Column(name = "greeting", nullable = false)
    public String greeting;

    @Column(name = "message", nullable = false)
    public String message;

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
