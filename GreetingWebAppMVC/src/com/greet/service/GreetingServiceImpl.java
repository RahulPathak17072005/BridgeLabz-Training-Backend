package com.greet.service;

import com.greet.model.Greeting;

public class GreetingServiceImpl implements GreetingService {
    /**
     * The prefix for the greeting message.
     * Injected via Spring XML configuration: <property name="prefix" value="Hello"/>
     */
    private String prefix;
    // ───────────────────────── Getter & Setter ─────────────────────────
    /**
     * Returns the current prefix.
     *
     * @return the prefix string
     */
    public String getPrefix() {
        return prefix;
    }
    /**
     * Sets the prefix. Called by Spring via setter injection.
     *
     * @param prefix the greeting prefix (e.g., "Hello", "Welcome", "Hi")
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    // ───────────────────────── Lifecycle Methods ─────────────────────────
    /**
     * Initialization method — called by Spring after the bean is created
     * and all properties are set.
     *
     * Configured in XML as: init-method="init"
     */
    public void init() {
        System.out.println("=== GreetingServiceImpl initialized ===");
        System.out.println(" Prefix set to: " + prefix);
    }
    /**
     * Destroy method — called by Spring when the application context
     * is being shut down.
     *
     * Configured in XML as: destroy-method="destroy"
     */
    public void destroy() {
        System.out.println("=== GreetingServiceImpl destroyed ===");
    }
    // ───────────────────────── Business Logic ─────────────────────────
    /**
     * Generates a personalized greeting.
     *
     * Example: If prefix = "Hello" and name = "Kiran"
     * → returns Greeting with message "Hello, Kiran!"
     *
     * @param name the name of the user
     * @return a Greeting object with the composed message
     */
    @Override
    public Greeting greet(String name) {
        String message = prefix + ", " + name + "!";
        System.out.println(" GreetingServiceImpl.greet() → " + message);
        return new Greeting(message);
    }
}
