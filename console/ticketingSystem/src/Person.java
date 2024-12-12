public abstract class Person implements Runnable {
    protected String id;
    protected int interval;

    public Person(String id, int interval) {
        this.id = id;
        this.interval = interval;
    }

    @Override
    public abstract void run();
}
