package CoreSheild.assignment.MapData.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/results")
    public String results() {
        return "forward:/results.html";
    }
}