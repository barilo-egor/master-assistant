package by.moon.masterassistant.enums;

public enum Day {
    NONE("none","n"),
    MONDAY("Понедельник", "ПН"),
    TUESDAY("Вторник", "ВТ"),
    WEDNESDAY("Среда", "СР"),
    THURSDAY("Четверг", "ЧТ"),
    FRIDAY("Пятница", "ПТ"),
    SATURDAY("Суббота", "СБ"),
    SUNDAY("Воскресенье", "ВСК");

    String dayName;
    String reduction;

    Day(String dayName, String reduction) {
        this.dayName = dayName;
        this.reduction = reduction;
    }

    public String getDayName() {
        return dayName;
    }

    public String getReduction() {
        return reduction;
    }

    public static Day fromString(String dayName) {
        for (Day day : Day.values()) {
            if (day.getDayName().equals(dayName)) return day;
        }
        return NONE;
    }
}
