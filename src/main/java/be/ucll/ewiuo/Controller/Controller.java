package be.ucll.ewiuo.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @RequestMapping("/")
    public String index(){
        return "This is a test index page for spring";
    }
}
