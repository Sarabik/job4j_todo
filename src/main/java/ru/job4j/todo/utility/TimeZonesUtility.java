package ru.job4j.todo.utility;

import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class TimeZonesUtility {

    public static final Map<String, String> TIMEZONES = getTimeZones();

    private static Map<String, String> getTimeZones() {
        Map<String, String> zonesMap = new TreeMap<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        for (String zoneId : ZoneId.getAvailableZoneIds()) {
            ZoneId id = ZoneId.of(zoneId);
            ZoneOffset zoneOffset = localDateTime.atZone(id).getOffset();
            String offset = zoneOffset.getId().replaceAll("Z", "+00:00");
            zonesMap.put((String.format("(UTC%s) - %s", offset, id)), zoneId);
        }
        return zonesMap;
    }

    public static Collection<Task> tasksToCurrentTimeZone(Collection<Task> tasks, User user) {
       if (tasks.size() > 0) {
           tasks.forEach(
                   task -> {
                       LocalDateTime current = task.getCreated();
                       LocalDateTime newTime = dateToCurrentTimeZone(current, user);
                       task.setCreated(newTime);
                   });
       }
        return tasks;
    }

    public static LocalDateTime dateToCurrentTimeZone(LocalDateTime current, User user) {
        return current
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of(user.getTimezone()))
                .toLocalDateTime();
    }
}
