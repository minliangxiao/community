package club.hl.community.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
* 加了Controller后这个这个类就可以接收请求的消息
* */
@Controller
public class HelloController {
    @GetMapping("/hello")
    public String hello(@RequestParam(name="name")String name, Model model){
      model.addAttribute("name",name);
      return "hello";

    }
}
