package dailyplanner;

public enum Repeatability {
    ONE_TIME("однократная"),
    DAILY("ежедневная"),
    WEEKLY("еженедельная"),
    MONTHLY("ежемесячная"),
    YEARLY("ежегодная");

    private final String name;

    Repeatability(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}