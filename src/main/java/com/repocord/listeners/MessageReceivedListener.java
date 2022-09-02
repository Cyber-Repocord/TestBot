package com.repocord.listeners;

import com.repocord.graphics.GraphicsUtils;
import com.repocord.storage.MemberData;
import com.repocord.storage.Storage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

        try {
            BufferedImage image = GraphicsUtils.readImage("/rankcard.png");

            GraphicsUtils.overlayText(image, "1", new Color(0x9C40BF), new Font("Amaranth", Font.PLAIN, 150), 800, 400);

            GraphicsUtils.writeImage(image, "C:\\work\\test.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
