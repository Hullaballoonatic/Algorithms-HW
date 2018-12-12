import java.io.File;
import java.io.PrintStream;
import java.util.*;

public class Homework7 {
    private static Scanner in = new Scanner(System.in);
    private static PrintStream out = System.out;

    public static void main(String... args) {
        try {
            in = new Scanner(new File(args[0]));
        } catch (Exception ignored) {
        }
        try {
            out = new PrintStream(new File(args[1]));
        } catch (Exception ignored) {
        }
        out.println(new ActivitySelector(in).selectForValue());
    }
}

class ActivitySelector {
    private Activity[] activities;
    private int n;

    private List<Activity> selectForTime() {
        return new ArrayList<Activity>() {
            {
                add(activities[0]);
                for (int m = 1, k = 0; m < n; m++)
                    if (activities[m].start >= activities[k].end)
                        add(activities[k = m]);
            }
        };
    }

    ValueSchedule selectForValue() {
        List<Activity> schedule = selectForTime();
        Arrays.sort(activities, Comparator.comparingDouble(o -> o.valuePerTime));
        int maxValue = 0;
        for (Activity activity : activities) {
            int value = activity.value;

            List<Activity> potentialSchedule = new ArrayList<Activity>() {{
                add(activity);
            }};



            for (int m = 0, k = 0; m < n; m++)
                if (activities[m].id != activity.id && activities[m].start >= activities[k].end
                        && (activities[m].end <= activity.start || activities[m].start >= activity.end)) {
                    value += activities[m].value;
                    potentialSchedule.add(activities[k = m]);
                }

            if (value >= maxValue) {
                maxValue = value;
                schedule = potentialSchedule;
            }
        }
        return new ValueSchedule(schedule, maxValue);
    }

    ActivitySelector(Scanner in) {
        n = in.nextInt();
        activities = new Activity[n];
        for (int i = 0; i < n; i++)
            activities[i] = new Activity(in);
    }

    class ValueSchedule {
        List<Activity> schedule;
        int totalValue = 0;

        ValueSchedule(List<Activity> schedule, int totalValue) {
            this.schedule = schedule;
            this.totalValue = totalValue;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(totalValue).append("\n");
            for (Activity activity : schedule)
                sb.append(activity.id).append("\n");
            return sb.toString();
        }
    }

    class Activity {
        int id, start, end, value;
        float valuePerTime;

        Activity(Scanner in) {
            id = in.nextInt();
            start = in.nextInt();
            end = in.nextInt();
            value = in.nextInt();
            valuePerTime = (end - start) * 1000f / value;
        }

        @Override
        public String toString() {
            return String.format("%2d %2d %2d %4d %6f", id, start, end, value, valuePerTime);
        }
    }
}
