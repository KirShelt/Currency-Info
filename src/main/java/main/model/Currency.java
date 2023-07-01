package main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.time.LocalDate;

@Getter
@Setter
public class Currency {

    private String name;
    private double maxValue;
    private LocalDate maxDate;
    private double minValue;
    private LocalDate minDate;
    private double averageSum;
    private int daysCount;
    private final String accuracyPattern;

    public Currency(String name, double maxValue, LocalDate maxDate, double minValue, LocalDate minDate,
                    double averageSum, int daysCount, int accuracy) {
        this.name = name;
        this.maxValue = maxValue;
        this.maxDate = maxDate;
        this.minValue = minValue;
        this.minDate = minDate;
        this.averageSum = averageSum;
        this.daysCount = daysCount;
        String pattern = "#.";
        for (int i = 0; i < accuracy; i++) pattern += "#";
        this.accuracyPattern = pattern;
    }


    public String getAverageValue() {
        return DoubleAccuracy(averageSum / daysCount);
    }

    public String getMinValueString() {
        return DoubleAccuracy(minValue);
    }

    public String getMaxValueString() {
        return DoubleAccuracy(maxValue);
    }

    private String DoubleAccuracy(double value) {
        return new DecimalFormat(accuracyPattern).format(value);
    }
}