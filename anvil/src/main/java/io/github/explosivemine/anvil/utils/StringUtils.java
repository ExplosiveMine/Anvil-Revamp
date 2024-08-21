package io.github.explosivemine.anvil.utils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class StringUtils {
    private StringUtils() {}

    public static String formatTime(Duration duration) {
        return formatTimeRecursive(new StringBuilder(), duration, TimeUnit.DAYS.ordinal(), false);
    }

    private static String formatTimeRecursive(StringBuilder sb, Duration duration, int ordinal, boolean addComma) {
        if (ordinal < TimeUnit.SECONDS.ordinal()) {
            return sb.toString();
        }

        TimeUnit timeUnit = TimeUnit.values()[ordinal];
        long unitAmount = switch (timeUnit) {
            case DAYS -> duration.toDays();
            case HOURS -> duration.toHours();
            case MINUTES -> duration.toMinutes();
            case SECONDS -> duration.toSeconds();
            case MILLISECONDS -> duration.toMillis();
            case MICROSECONDS -> duration.toMillis() * 1000L;
            case NANOSECONDS -> duration.toNanos();
        };

        if (unitAmount > 0) {
            if (addComma) {
                sb.append(", ");
            }

            String s = " " + timeUnit.toString().toLowerCase();
            sb.append(unitAmount).append(formatPlural(unitAmount, s.substring(0, s.length()-1)));
            duration = duration.minus(unitAmount, timeUnit.toChronoUnit());
            addComma = true;
        } else if (ordinal == TimeUnit.SECONDS.ordinal()) {
            sb.append("0 seconds");
        }

        return formatTimeRecursive(sb, duration, ordinal - 1, addComma);
    }

    public static String formatPlural(Number number, String s) {
        return number.doubleValue() == 1 ? s : s + "s";
    }

}