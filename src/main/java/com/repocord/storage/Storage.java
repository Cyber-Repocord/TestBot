package com.repocord.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.internal.utils.Checks;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Storage {
    private final File location;
    private final Map<String, Map<String, MemberData>> members;

    public Storage(File location) throws IOException {
        Checks.check(location.isDirectory(), "Provided file is not a directory.");
        Checks.check(location.exists(), "Provided directory doesn't exist.");
        Checks.check(location.canRead(), "Cannot read the contents of the directory.");
        Checks.check(location.canWrite(), "Cannot write to the directory.");

        this.location = location;
        this.members = new HashMap<>();

        reload();
    }

    public MemberData getMember(Member member) {
        return getMember(member.getGuild().getId(), member.getId());
    }

    public MemberData getMember(String guildId, String id) {
        MemberData memberData = members.computeIfAbsent(guildId, t -> new HashMap<>()).get(id);

        if (memberData == null) {
            memberData = new MemberData(guildId, id, 0);

            members.get(guildId).put(id, memberData);
        }

        return memberData;
    }

    public void reload() throws IOException {
        Gson gson = new Gson();
        File[] guildDirectories = location.listFiles(File::isDirectory);

        if (guildDirectories == null) {
            throw new IOException("Subdirectories are not available.");
        }

        for (File guildDirectory : guildDirectories) {
            Checks.check(guildDirectory.isDirectory(), "Provided file is not a directory.");
            Checks.check(guildDirectory.exists(), "Provided directory doesn't exist.");

            File[] memberFiles = guildDirectory.listFiles(File::isFile);

            if (memberFiles == null) {
                throw new IOException("Files are not available.");
            }

            for (File memberFile : memberFiles) {
                if (memberFile.getName().endsWith(".json")) {
                    FileReader memberReader = new FileReader(memberFile);

                    MemberData memberData = gson.fromJson(memberReader, MemberData.class);
                    memberReader.close();
                    if (memberData == null) continue;

                    if (!this.members.containsKey(memberData.getGuildId())) {
                        this.members.put(memberData.getGuildId(), new HashMap<>());
                    }

                    this.members.get(memberData.getGuildId()).put(memberData.getId(), memberData);
                }
            }
        }
    }

    public void save() {
        for (Map<String, MemberData> guild : members.values()) {
            for (MemberData memberData : guild.values()) {
                saveMember(memberData);
            }
        }
    }


    public void saveMember(MemberData memberData) {
        Map<String, MemberData> guild = members.computeIfAbsent(memberData.getGuildId(), k -> new HashMap<>());
        guild.put(memberData.getId(), memberData);

        File guildDirectory = new File(location, memberData.getGuildId());

        Checks.check(guildDirectory.isDirectory() || !guildDirectory.exists(), "Provided file is a not directory.");

        if (!guildDirectory.exists()) {
            if (!guildDirectory.mkdir()) {
                throw new RuntimeException("Couldn't create a directory.");
            }
        }

        File memberFile = new File(guildDirectory, memberData.getId() + ".json");

        Checks.check(memberFile.isFile() || !memberFile.exists(), "Provided file is a directory.");

        if (!memberFile.exists()) {
            try {
                if (!memberFile.createNewFile()) {
                    throw new RuntimeException("Couldn't create a file.");
                }
            } catch (IOException e) {
                throw new RuntimeException("Couldn't create a file.");
            }
        }

        try {
            FileWriter memberWriter = new FileWriter(memberFile);

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            gson.toJson(memberData, memberWriter);
            memberWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't write to a file.");
        }
    }
}
