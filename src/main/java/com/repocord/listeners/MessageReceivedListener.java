package com.repocord.listeners;

import com.repocord.storage.MemberData;
import com.repocord.storage.Storage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReceivedListener extends ListenerAdapter {
    private final Storage storage;

    public MessageReceivedListener(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getMember() == null) return;

        MemberData memberData = storage.getMember(event.getGuild().getId(), event.getMember().getId());
        memberData.incrementSentMessages();
        storage.saveMember(memberData);
    }
}
