package dev.FredyRedaTeam.model;

import org.json.JSONObject;

/**
 * Return values sent by the server in response to actions
 */
public class Return {
    private final int code;
    private final JSONObject data;

    public int getCode() {
        return code;
    }

    public JSONObject getData() {
        return data;
    }

    public Return(int code, JSONObject data) {
        this.code = code;
        this.data = data;
    }

    public Return(int code) {
        this.code = code;
        this.data = new JSONObject();
    }

    public Return() {
        this.code = 0;
        this.data = new JSONObject();
    }

    // --- JSON utility ---

    public JSONObject toJson() {
        JSONObject out = new JSONObject();
        out.put("code", this.code);
        out.put("data", this.data);
        return out;
    }
}
