package net.nel.il.controller;

import com.google.gson.Gson;
import net.nel.il.account.InputAccount;
import net.nel.il.account.OutputAccount;
import net.nel.il.service.MainService;
import net.nel.il.service.SpecialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {

    @Autowired
    MainService mainService;

    @Autowired
    SpecialService specialService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String mainMethod(@RequestBody String json){
        Gson gson = new Gson();
        InputAccount account = gson.fromJson(json, InputAccount.class);
        OutputAccount outputAccount = mainService.handle(account);
        return gson.toJson(outputAccount);
    }

    @RequestMapping(value = "/special/", method = RequestMethod.POST)
    public String specialMethod(@RequestBody String json){
        Gson gson = new Gson();
        InputAccount account = gson.fromJson(json, InputAccount.class);
        OutputAccount outputAccount = specialService.handle(account);
        return gson.toJson(outputAccount);
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(){
        String result = mainService.show();
        return result;
    }

    @RequestMapping(value = "/drop", method = RequestMethod.GET)
    public String drop(){
        String result = mainService.drop();
        return result;
    }
}
