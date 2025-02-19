package dev.FredyRedaTeam.model;

public class Message {
    private final String uuid;
    private final long timestamp;
    private final String content;

    public Message(String uuid, String content) {
        this.uuid = uuid;
        this.timestamp = System.currentTimeMillis();
        this.content = content;
    }

    public String getUuid() {
        return uuid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }
}
