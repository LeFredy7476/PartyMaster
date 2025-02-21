package dev.FredyRedaTeam.model;

public class Return {
    private final int code;
    private final String data;

    public int getCode() {
        return code;
    }

    public String getData() {
        return data;
    }

    public Return(int code, String data) {
        this.code = code;
        this.data = data;
    }
}
