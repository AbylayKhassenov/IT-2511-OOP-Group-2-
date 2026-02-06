package cfg;

public class FinePolicy {

    private static final FinePolicy instance = new FinePolicy();

    private final int finePerDay;
    private final int maxLoanDays;

    protected FinePolicy() {
        this.finePerDay = 10000;
        this.maxLoanDays = 7;
    }

    public static FinePolicy getInstance() {
        return instance;
    }

    public int getFinePerDay() {
        return finePerDay;
    }

    public int getMaxLoanDays() {
        return maxLoanDays;
    }
}
