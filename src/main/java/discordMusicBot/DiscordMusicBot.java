package discordMusicBot;

import discordMusicBot.commands.Music;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

public class DiscordMusicBot {

    public static void main(String[] args) {
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream("config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String token = properties.getProperty("discord.api.key");

        JDA jda = JDABuilder.createLight(token, Collections.emptyList())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_VOICE_STATES)
                .setMemberCachePolicy(MemberCachePolicy.VOICE)
                .enableCache(CacheFlag.VOICE_STATE)
                .addEventListeners(new Music())
                .build();

        jda.updateCommands().addCommands(
                Commands.slash("play", "재생하고 싶은 노래 제목과 아티스트명을 입력해주세요!")
                        .addOption(OptionType.STRING, "title", "제목 + 아티스트 or 유튜브 링크")
        ).queue();

        jda.getPresence().setStatus(OnlineStatus.ONLINE);
    }
}
