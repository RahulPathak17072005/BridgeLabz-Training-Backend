package com.greet.model;

public class Greeting {
    private String message;
    // ───────────────────────── Constructors ─────────────────────────
    /**
     * Default no-arg constructor.
     * Required for Spring bean instantiation.
     */
    public Greeting() {
    }
    /**
     * Parameterized constructor.
     *
     * @param message the greeting message
     */
    public Greeting(String message) {
        this.message = message;
    }
    // ───────────────────────── Getter & Setter ─────────────────────────
    /**
     * Returns the greeting message.
     *
     * @return the message string
     */
    public String getMessage() {
        return message;
    }
    /**
     * Sets the greeting message.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    // ───────────────────────── toString ─────────────────────────
    @Override
    public String toString() {
        return "Greeting{message='" + message + "'}";
    }
}
