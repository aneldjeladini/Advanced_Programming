import java.io.*;
import java.sql.Time;
import java.util.*;

class UnsupportedFormatException extends Exception {
    String message;

    public UnsupportedFormatException(String message) {
        super(message);
    }

}

class InvalidTimeException extends Exception {
    String message;

    public InvalidTimeException(String message) {
        super(message);
    }

}

class TimeStamp {
    int hours;
    int minutes;

    public TimeStamp(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public String convertedTime() {
        if (hours == 0) {
            hours = 12;
            return this + " AM";
        }
        if (hours >= 1 && hours <= 11) {
            return this + " AM";
        }
        if (hours == 12) {
            return this + " PM";
        }
        if (hours >= 13 && hours <= 23) {
            hours -= 12;
            return this + " PM";
        } else return "INVALID HOURS";
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%2d:", hours));
        if (minutes < 10) {
            sb.append("0").append(minutes);
        } else {
            sb.append(minutes);
        }
        return sb.toString();
    }
}

class TimeTable {
    private ArrayList<TimeStamp> times;

    public TimeTable() {
        times = new ArrayList<>();
    }

    public void readTimes(InputStream inputStream) throws InvalidTimeException, UnsupportedFormatException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                String[] parts = line.trim().split("\\s+");
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].contains(":")) {
                        String[] timeParts = parts[i].split(":");
                        int hours = Integer.parseInt(timeParts[0]);
                        int minutes = Integer.parseInt(timeParts[1]);
                        if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
                            throw new InvalidTimeException(parts[i]);
                        }
                        TimeStamp time = new TimeStamp(hours, minutes);
                        times.add(time);
                    } else if (parts[i].contains(".")) {
                        String[] timeParts = parts[i].split("\\.");
                        int hours = Integer.parseInt(timeParts[0]);
                        int minutes = Integer.parseInt(timeParts[1]);
                        if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
                            throw new InvalidTimeException(parts[i]);
                        }
                        TimeStamp time = new TimeStamp(hours, minutes);
                        times.add(time);
                    } else {
                        throw new UnsupportedFormatException(parts[i]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeTimes(OutputStream outputStream, TimeFormat format) {
        PrintWriter pw = new PrintWriter(outputStream);
        times.sort(Comparator.comparingInt(TimeStamp::getHours).thenComparingInt(TimeStamp::getMinutes));
        times.stream()
                .forEach(t -> {
                    if (format == TimeFormat.FORMAT_24) {
                        System.out.println(t);
                    } else {
                        System.out.println(t.convertedTime());
                    }
                });
    }
}

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}