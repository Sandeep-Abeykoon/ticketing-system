/**
 * Abstract base class representing a person in the ticket management system.
 * Implements the Runnable interface for multithreaded behavior.
 */
public abstract class Person implements Runnable {
    protected String id;
    protected int interval;

    /**
     * Constructs a Person with the specified ID and interval.
     *
     * @param id       Unique identifier for the person.
     * @param interval Time interval (in milliseconds) for the person's actions.
     */
    public Person(String id, int interval) {
        this.id = id;
        this.interval = interval;
    }

    /**
     * Abstract method defining the behavior of the person in a separate thread.
     * Must be implemented by subclasses.
     */
    @Override
    public abstract void run();
}
