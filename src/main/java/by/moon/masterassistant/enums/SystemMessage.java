package by.moon.masterassistant.enums;

public enum SystemMessage {
    NONE("none"),
    ADMIN_MENU("Меню администратора."),
    EMPTY("Пока что тут пусто."),
    MESSAGE_EDIT_MENU("Меню редактирования сообщений."),
    CONTACTS_EDIT_MENU("Меню редактирования контактов."),
    SERVICES_EDIT_MENU("Меню редактирования услуг."),
    OPERATING_MODE_EDIT_MENU("Меню редактирования режима работы."),
    ADMIN_OUT("Вы покинули панель администратора и стали пользователем."),
    ENTER_NEW_GREETING("Введите новый текст приветствия."),
    ENTER_NEW_USER_MENU_MESSAGE("Введите новый текст для сообщения главного меню."),
    ENTER_NEW_CONTACTS_MENU_MESSAGE("Введите текст для сообщения меню контактов."),
    ENTER_NEW_SERVICES_AND_PRICES_MESSAGE("Введите текст для сообщения меню услуг и цен."),
    SAVED("Сохранено."),
    ENTER_NAME_FOR_CONTACT("Введите отображаемый текст для кнопки-ссылки."),
    ENTER_LINK_FOR_CONTACT("Введите ссылку."),
    ENTER_NAME_FOR_SERVICE("Введите наименование услуги."),
    SEND_PHOTO_FOR_SERVICE("Отправьте фото услуги.\n(Опционально.Если фото не нужно," +
            " нажмите кнопку \"Пропустить\"."),
    ENTER_DESCRIPTION_FOR_SERVICE("Введите описание услуги."),
    ENTER_PRICE_FOR_SERVICE("Введите цену услуги.\nФормат не принципиален: " +
            "отображаться будет так, как введено. Не забудьте добавить валюту, если это необходимо.\n" +
            "Цена необязательна. Для пропуска ввода нажмите \"Пропустить\""),
    NEED_PHOTO("Нужна фотография. Можно пропустить добавление фотографии, нажав \"Пропустить\""),
    WRONG_DOUBLE_VALUE("Неверное значение. Введите"),
    CHOOSE_DAY("Выберите день недели."),
    CHOOSE_TIME_FROM("Выберите время, с которого начинается этот рабочий день."),
    CHOOSE_TIME_TO("Выберите крайнее время дня, на которую можно произвести запись."),
    WRONG_DAY("Что-то пошло не так. Выберите день из предложенных."),
    DAY_EXISTS("Такой день уже присутствует в графике."),
    WRONG_TIME("Неверное значение времени. Выберите время из предложенного, " +
            "либо введите своё в формате \"ЧЧ:ММ\"."),
    CHOOSE_CONTACT_FOR_DELETE("Выберите контакт для удаления."),
    CHOOSE_SERVICE_FOR_DELETE("Выбеите услугу для удаления."),
    CHOOSE_WORKING_DAY_FOR_DELETE("Выберите рабочий день для удаления."),
    SCHEDULE("Расписание."),
    DATE_IS_BEFORE("Выберите дату, начиная с завтрашнего дня."),
    OPERATING_MODE("Время для записи: "),
    DAY_IS_FREE("День полностью свободен."),
    REST_DAY("Этот день выходной."),
    CHOOSE_TIME("Выберите желаемое время записи."),
    APPOINTMENT_LIST("Занятое время:"),
    NOT_CONFIRMED("(не подтверждено)"),
    TIME_NOT_FREE("Время уже занято."),
    CONFIRM_DATA("Подтвердите, пожалуйста, что всё верно."),
    APPOINTMENT_CREATED("Спасибо! Теперь, для окончательной записи, осталось дождаться подтверждения мастера. " +
            "Бот обязательно отправит Вам оповещение."),
    HAS_ACTIVE_APPOINTMENT("У Вас уже есть активная запись."),
    SEND_CONTACT("Последний шаг, отправьте, пожалуйста, Ваш контакт, нажав на кнопку \"Отправить контакт\"," +
            " чтобы у мастера была возможность связаться с Вами."),
    DAYS_APPOINTMENTS("Записи на "),
    ADD_COMMENT("Добавьте комментарий к записи. Например имя клиента или адрес."),
    NEED_CONTACT("Отправьте контакт, либо пропустите этот шаг."),
    CONTACT_SAVED("Спасибо, Ваш контакт сохранен."),
    CHOOSE_DURATION("Выберите продолжительность."),
    NEW_APPOINTMENT("Поступила новая запись. Нужно подтверждение."),
    HELLO_MASTER("Привет, мастер!"),
    ENTER_SUM("Введите сумму."),
    WRONG_AMOUNT("Неверное значение."),
    ENTER_BALANCE_CHANGE_COMMENT("Введите комментарий, либо нажмите \"Пропустить\"."),
    BALANCE_CHANGE_MENU("Меню учета средств."),
    MASTER_ADDED_APPOINTMENT("Запись мастера. Без комментария."),
    MASTER_CONFIRMED_APPOINTMENT("Мастер подтвердил вашу запись!"),
    MASTER_REJECTED_APPOINTMENT("Мастер не может принять вашу запись."),
    APPOINTMENT_DELETED("Запись удалена."),
    CHOOSE_APPOINTMENT_FOR_DELETE("Выберите запись для удаления."),
    APPOINTMENT_HOUR_REMIND_FOR_USER("До приема Вас мастером остался один час."),
    APPOINTMENT_HOUR_REMIND_FOR_MASTER("Прием клиента через час:"),
    BALANCE_CHANGES_EMPTY("Пока что не было записей расходов/приходов."),
    NO_ACTIVE_APPOINTMENT("У вас нет активной записи."),
    NO_APPOINTMENTS("Нет записей.");

    String command;

    SystemMessage(String command) {
        this.command = command;
    }

    public String getSystemMessage() {
        return command;
    }

    public static SystemMessage fromString(String message) {
        for (SystemMessage systemMessage : SystemMessage.values()) {
            if (systemMessage.getSystemMessage().equals(message)) return systemMessage;
        }
        return NONE;
    }
}
