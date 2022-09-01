package com.repocord;

import com.repocord.listeners.MessageReceivedListener;
import com.repocord.storage.Storage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;

public class TestBot {
    public static void main(String[] args) throws LoginException, InterruptedException, IOException {
        JDA jda = JDABuilder
                .createLight("MTAxNDYxMDg3MjE3NTQ0ODA4OA.GobNQY.MJumI1QwAQKuK2YvJAA8IhYaEQdDXf85n6orcI")
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .build()
                .awaitReady();

        Storage storage = new Storage(new File("."));
        storage.save();

        jda.addEventListener(new MessageReceivedListener(storage));
    }
}
