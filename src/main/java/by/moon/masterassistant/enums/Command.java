package by.moon.masterassistant.enums;

public enum Command {
    NONE("none"),
    ADMIN_PASSWORD("1111"),
    MASTER_PASSWORD("2222"),
    START("/start"),
    BACK("Вернуться"),
    CANCEL("Отмена"),

    MY_APPOINTMENT("\uD83D\uDD8D Моя запись"),
    SCHEDULE("\uD83D\uDCC5 Расписание"),
    CONTACTS("☎️ Контакты"),
    SERVICES_AND_PRICES("\uD83D\uDCC1 Услуги и цены"),
    READ_MASTER_SERVICE("READ_MASTER_SERVICE"),
    MESSAGE_EDIT_MENU("Редактирование сообщений"),
    CONTACTS_EDIT_MENU("Редактирование контактов"),
    SERVICES_EDIT_MENU("Редактирование услуг"),
    OPERATING_MODE_MENU("Режим работы"),
    OUT("Выйти"),
    GREETING_EDIT("Изменить приветсвие"),
    USER_MENU_EDIT("Изенить сообщение гл.меню"),
    USER_CONTACTS_MENU_EDIT("Изменить сообщение контактов"),
    USER_SERVICES_MENU_EDIT("Изменить сообщение услуг"),
    ADD_CONTACT("Добавить контакт"),
    DELETE_CONTACT("Удалить контакт"),
    ADD_SERVICE("Добавить услугу"),
    DELETE_SERVICE("Удалить услугу"),
    ADD_WORKING_DAY("Добавить рабочий день"),
    DELETE_WORKING_DAY("Удалить рабочий день"),
    SKIP("Пропустить"),
    BACK_TO_SERVICES_AND_PRICES("BACK_TO_SERVICES_AND_PRICES"),
    DELETING_CONTACT("DELETING_CONTACT"),
    DELETING_SERVICE("DELETING_SERVICE"),
    DELETING_WORKING_DAY("DELETING_WORKING_DAY"),
    ANOTHER_MONTH("ANOTHER_MONTH"),
    CHOOSE_DATE("CHOOSE_DATE"),
    CREATE_APPOINTMENT("CREATE_APPOINTMENT"),
    APPLY_APPOINTMENT("APPLY_APPOINTMENT"),
    CANCEL_APPOINTMENT("CANCEL_APPOINTMENT"),
    BACK_TO_SCHEDULE("BACK_TO_SCHEDULE"),
    NEW_APPOINTMENTS("\uD83C\uDD95 Новые записи"),
    MONEY_ACCOUNTING("\uD83D\uDCC8 Учет средств"),
    DAYS_APPOINTMENTS("DAYS_APPOINTMENTS"),
    SEND_CONTACT("Отправить контакт"),
    MASTER_OUT("/out"),
    BACK_TO_MASTER_SCHEDULE("BACK_TO_MASTER_SCHEDULE"),
    ADD_APPOINTMENT("ADD_APPOINTMENT"),
    MASTER_ADD_APPOINTMENT("MASTER_ADD_APPOINTMENT"),
    CONFIRM_APPOINTMENT("CONFIRM_APPOINTMENT"),
    REJECT_APPOINTMENT("REJECT_APPOINTMENT"),
    CHOOSING_DURATION("CHOOSING_DURATION"),
    DEPOSIT("➕ Приход"),
    WITHDRAW("➖ Расход"),
    DELETE_APPOINTMENT("DELETE_APPOINTMENT"),
    DELETING_APPOINTMENT("DELETING_APPOINTMENT"),
    BACK_TO_DAYS_APPOINTMENTS("BACK_TO_DAYS_APPOINTMENTS"),
    LOAD_STATISTIC("⬇️ Выгрузить статистику"),
    CLOSEST_APPOINTMENTS("\uD83D\uDCD6 Ближайшие записи");

    String command;

    Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static Command fromString(String command) {
        for (Command botCommand : Command.values()) {
            if (botCommand.getCommand().equals(command)) return botCommand;
        }
        return NONE;
    }
}
