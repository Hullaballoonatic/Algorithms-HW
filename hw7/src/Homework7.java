import java.io.File;
import java.io.PrintStream;
import java.util.*;

public class Homework7 {
    public static void main(String... args) {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        try {
            in = new Scanner(new File(args[0]));
        } catch (Exception ignored) {}
        try {
            out = new PrintStream(new File(args[1]));
        } catch (Exception ignored) {}
        out.println(new ActivitySelector(in));
    }
}

class ActivitySelector {
    private Activity[] activities;
    private int n;

    @Override
    public String toString() {
        List<Activity> schedule = selectForValue();
        StringBuilder sb = new StringBuilder();
        int value = 0;
        for (Activity activity : schedule) {
            value += activity.value;
            sb.append(activity.id).append("\n");
        }
        return String.format("%d\n%s", value, sb.toString());
    }

    private List<Activity> selectForTime() {
        return new ArrayList<Activity>() {{
            add(activities[0]);
            for (int m = 1, k = 0; m < n; m++)
                if (activities[m].start >= activities[k].end)
                    add(activities[k = m]);
        }};
    }

    private List<Activity> selectForValue() {
            List<Activity> result = selectForTime();
        Arrays.sort(activities, Comparator.comparingDouble(o -> o.valuePerTime));
        int maxValue = 0;
        for (Activity activity : activities) {
            int value = activity.value;

            List<Activity> potentialSchedule = new ArrayList<Activity>() {{
                add(activity);
            }};

            for (int m = 0, k = 0; m < n; m++)
                if (activities[m].id != activity.id && activities[m].start >= activities[k].end && (activities[m].end <= activity.start || activities[m].start >= activity.end)) {
                    value += activities[m].value;
                    potentialSchedule.add(activities[k = m]);
                }

            if(value >= maxValue) {
                maxValue = value;
                result   = potentialSchedule;
            }
        }
        return result;
    }

    ActivitySelector(Scanner in) {
        n = in.nextInt();
        activities = new Activity[n];
        for (int i = 0; i < n; i++)
            activities[i] = new Activity(in);
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
