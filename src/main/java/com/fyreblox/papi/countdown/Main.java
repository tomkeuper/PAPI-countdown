package com.fyreblox.papi.countdown;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Main extends PlaceholderExpansion{
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public String getIdentifier() {
        return "countdown";
    }

    public String getAuthor() {
        return "MrCeasar";
    }

    public String getVersion() {
        return "1.0";
    }

    public String onRequest(OfflinePlayer p, @NotNull String identifier) {
        switch (identifier.toLowerCase()) {
            case "all":
            default:
                if (identifier.startsWith("countdown_")){
                    String time = identifier.replace("countdown_", "");
                    if (!time.contains("_")) {
                        Date then;
                        try {
                            then = PlaceholderAPIPlugin.getDateFormat().parse(time);
                        } catch (Exception var13) {
                            return null;
                        }

                        Date now = new Date();
                        long between = then.getTime() - now.getTime();
                        return between <= 0L ? "0" : formatTime(Duration.of((long)((int) TimeUnit.MILLISECONDS.toSeconds(between)), ChronoUnit.SECONDS));
                    } else {
                        String[] parts = PlaceholderAPI.setBracketPlaceholders(p, time).split("_");
                        if (parts.length != 2) {
                            return "invalid format and time";
                        } else {
                            time = parts[1];
                            String format = parts[0];

                            SimpleDateFormat f;
                            try {
                                f = new SimpleDateFormat(format);
                            } catch (Exception var15) {
                                return "invalid date format";
                            }

                            Date then;
                            try {
                                then = f.parse(time);
                            } catch (Exception var14) {
                                return "invalid date";
                            }

                            long t = System.currentTimeMillis();
                            long between = then.getTime() - t;
                            return between <= 0L ? "0" : formatTime(Duration.of((long)((int)TimeUnit.MILLISECONDS.toSeconds(between)), ChronoUnit.SECONDS));
                        }
                    }
                }
        }
        return null;
    }

    public static String formatTime(Duration duration) {
        StringBuilder builder = new StringBuilder();
        long seconds = duration.getSeconds();
        long minutes = seconds / 60L;
        long hours = minutes / 60L;
        long days = hours / 24L;
        long weeks = days / 7L;
        seconds %= 60L;
        minutes %= 60L;
        hours %= 24L;
        days %= 7L;
        if (seconds > 0L) {
            builder.insert(0, seconds + "s");
        }

        if (minutes > 0L) {
            if (builder.length() > 0) {
                builder.insert(0, ' ');
            }

            builder.insert(0, minutes + "m");
        }

        if (hours > 0L) {
            if (builder.length() > 0) {
                builder.insert(0, ' ');
            }

            builder.insert(0, hours + "h");
        }

        if (days > 0L) {
            if (builder.length() > 0) {
                builder.insert(0, ' ');
            }

            builder.insert(0, days + "d");
        }

        if (weeks > 0L) {
            if (builder.length() > 0) {
                builder.insert(0, ' ');
            }

            builder.insert(0, weeks + "w");
        }

        return builder.toString();
    }

}