package com.fyreblox.papi.countdown;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main extends PlaceholderExpansion implements Configurable {
    private boolean showSeconds = true;
    private boolean showMinutes = true;
    private boolean showHours = true;
    private boolean showDays = true;
    private boolean showWeeks = true;
    private int maxAmount = 5;
    public Main() {
    }

    public @NotNull String getIdentifier() {
        return "countdown";
    }

    public @NotNull String getAuthor() {
        return "MrCeasar";
    }

    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean canRegister() {
        this.showSeconds = this.getBoolean("show.seconds",true);
        this.showMinutes = this.getBoolean("show.minutes",true);
        this.showHours = this.getBoolean("show.hours",true);
        this.showDays = this.getBoolean("show.days",true);
        this.showWeeks = this.getBoolean("show.weeks",true);
        this.maxAmount = this.getInt("format-amount",5);
        return true;
    }

    public Map<String, Object> getDefaults() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("show.seconds", true);
        defaults.put("show.minutes", true);
        defaults.put("show.hours", true);
        defaults.put("show.days", true);
        defaults.put("show.weeks", true);
        defaults.put("format-amount", 5);
        return defaults;
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

    public String formatTime(Duration duration) {
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

        int amountOfNumbers = 0;
        if (seconds > 0L && showSeconds) amountOfNumbers++;
        if (minutes > 0L && showMinutes) amountOfNumbers++;
        if (hours > 0L && showHours) amountOfNumbers++;
        if (days > 0L && showDays) amountOfNumbers++;
        if (weeks > 0L && showWeeks) amountOfNumbers++;

        if (seconds > 0L && this.showSeconds && (this.maxAmount >= amountOfNumbers)) {
            builder.insert(0, seconds + "s");
        }

        if (minutes > 0L && this.showMinutes && (this.maxAmount >= amountOfNumbers-1)) {
            if (builder.length() > 0) {
                builder.insert(0, ' ');
            }

            builder.insert(0, minutes + "m");
        }

        if (hours > 0L && this.showHours && (this.maxAmount >= amountOfNumbers-2)) {
            if (builder.length() > 0) {
                builder.insert(0, ' ');
            }

            builder.insert(0, hours + "h");
        }

        if (days > 0L && this.showDays && (this.maxAmount >= amountOfNumbers-3)) {
            if (builder.length() > 0) {
                builder.insert(0, ' ');
            }

            builder.insert(0, days + "d");
        }

        if (weeks > 0L && this.showWeeks && (this.maxAmount >= amountOfNumbers-4)) {
            if (builder.length() > 0) {
                builder.insert(0, ' ');
            }

            builder.insert(0, weeks + "w");
        }

        return builder.toString();
    }

}