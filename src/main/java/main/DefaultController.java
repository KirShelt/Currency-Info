package main;

import main.model.Currency;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;


@Controller
public class DefaultController {

    @RequestMapping("/")
    public String index(Model model){
        ArrayList<Currency> currenciesList = StatisticStorage.collectStatistics();
        model.addAttribute("currencies", currenciesList);
        int days = StatisticStorage.getDays();
        model.addAttribute("daysCount", days);
        return "index";
    }
}