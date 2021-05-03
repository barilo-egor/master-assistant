package by.moon.masterassistant.enums;

public enum SystemConstant {
    CALLBACK_DATA_DELIMITER("/");

    String constant;

    SystemConstant(String constant) {
        this.constant = constant;
    }

    public String getConstant() {
        return constant;
    }
}
