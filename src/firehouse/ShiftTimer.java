package firehouse;

public final class ShiftTimer {
    private int remainingMinutes;

    public ShiftTimer(int shiftMinutes) {
        remainingMinutes = shiftMinutes;
    }

    public int remainingMinutes() {
        return remainingMinutes;
    }

    public boolean isExpired() {
        return remainingMinutes <= 0;
    }

    /** One submitted command spends one minute. */
    public void spendCommand() {
        remainingMinutes -= 1;
        if (remainingMinutes < 0) {
            remainingMinutes = 0;
        }
    }
}
