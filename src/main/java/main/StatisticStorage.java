package main;

import main.model.Currency;

import java.time.LocalDate;
import java.util.ArrayList;

public class StatisticStorage {

    private static int days = 0;
    private static final int THREADS_COUNT = Runtime.getRuntime().availableProcessors()-1;

    public static void setDays(int daysC) {
        days = daysC;
    }

    public static int getDays() {
        return days;
    }

    public static ArrayList<Currency> collectStatistics() {
        DataCollector[] collectors = new DataCollector[THREADS_COUNT];
        int currentPosition = days;
        DataCollector.currencies.clear();

        for (int i = THREADS_COUNT; i > 0; i--) {
            int currentThreadDatesCount = currentPosition / i;

            LocalDate[] dateSet = new LocalDate[currentThreadDatesCount];
            for (int j = 0; j < currentThreadDatesCount; j++) {
                dateSet[j] = LocalDate.now().minusDays(currentPosition - j - 1);
            }
            currentPosition -= currentThreadDatesCount;
            collectors[i - 1] = new DataCollector(dateSet);
            collectors[i - 1].start();
        }

        for (int i = THREADS_COUNT; i > 0; i--) {
            try {
                collectors[i - 1].join();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return new ArrayList<>(DataCollector.currencies.values());
    }
}