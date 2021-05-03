package by.moon.masterassistant.enums;

public enum TimeValue {
    NONE(0),
    FIRST_WORK_HOUR(6),
    LAST_WORK_HOUR(23);

    int value;

    TimeValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
