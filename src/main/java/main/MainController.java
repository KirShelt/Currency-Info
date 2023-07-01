package main;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/{days}")
      public void get(@PathVariable int days) {
        StatisticStorage.setDays(days);
    }
}
