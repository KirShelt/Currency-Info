package main;

import lombok.AllArgsConstructor;
import main.model.Currency;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
public class DataCollector extends Thread {

    static final ConcurrentHashMap<Integer, Currency> currencies = new ConcurrentHashMap<>();
    private final LocalDate[] dateSet;

    @Override
    public void run() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (LocalDate localDate : dateSet) {
            String url = "https://www.cbr.ru/scripts/XML_daily_eng.asp?date_req=" + localDate.format(dateTimeFormatter);
            try {
                Connection.Response response = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64;" +
                                " x64; rv:25.0) Gecko/20100101 Firefox/25.0").referrer("https://www.google.com").
                        ignoreHttpErrors(true).timeout(5000).execute();
                Document doc = response.parse();
                if (response.statusCode() < 400) {
                    Elements currencyElements = doc.select("Valute");
                    for (Element element : currencyElements) {
                        int numCode = Integer.parseInt(element.select("NumCode").text());
                        String valueString = element.select("Value").text().replaceAll(",", ".");
                        String nominal = element.select("Nominal").text();
                        double value = Double.parseDouble(valueString)/Integer.parseInt(nominal);
                        synchronized (currencies) {
                            Currency currency = currencies.get(numCode);
                            if (currency == null) {
                                String name = element.select("Name").text();
                                Currency newCurrency = new Currency(name, value, localDate, value, localDate, value, 1,10);
                                currencies.put(numCode, newCurrency);
                                continue;
                            }
                            currency.setAverageSum(currency.getAverageSum() + value);
                            currency.setDaysCount(currency.getDaysCount() + 1);
                            if (value > currency.getMaxValue() ||
                                    (value == currency.getMaxValue() && currency.getMaxDate().isAfter(localDate))) {
                                currency.setMaxValue(value);
                                currency.setMaxDate(localDate);
                                continue;
                            }
                            if (value < currency.getMinValue() ||
                                    (value == currency.getMinValue() && currency.getMinDate().isAfter(localDate))) {
                                currency.setMinValue(value);
                                currency.setMinDate(localDate);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}