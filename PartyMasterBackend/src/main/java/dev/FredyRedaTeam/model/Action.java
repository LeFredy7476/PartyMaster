package dev.FredyRedaTeam.model;
import org.json.*;

public class Action {
    public String uuid;
    public long timestamp;
    public String[] target;
    public JSONObject content;

    public Action(String uuid, String target, JSONObject content) {
        this.uuid = uuid;
        this.timestamp = System.currentTimeMillis();
        this.target = target.split(":", 5);
        this.content = content;
    }
}
