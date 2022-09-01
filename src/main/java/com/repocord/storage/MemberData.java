package com.repocord.storage;

public class MemberData {
    private final String guildId;
    private final String id;
    private long messagesSent;

    public MemberData(String guildId, String id, long messagesSent) {
        this.guildId = guildId;
        this.id = id;
        this.messagesSent = messagesSent;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getId() {
        return id;
    }

    public long getMessagesSent() {
        return messagesSent;
    }

    public long incrementSentMessages() {
        return messagesSent++;
    }
}
