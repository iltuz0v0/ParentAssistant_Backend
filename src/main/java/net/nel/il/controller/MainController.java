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

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String index1(){
//            mainService.b();
//        return "lul";
//    }

    private String test(){
        String json;
        Gson gson = new Gson();
        InputAccount inputAccount = new InputAccount();
        inputAccount.setId(4);
        inputAccount.setIdentifier(21);
        inputAccount.setCompanion_id(22);
        inputAccount.setLat(56.2820032);
        inputAccount.setRadius(0.009f);
        inputAccount.setLng(44.0891568);
        inputAccount.setStatus(1);
//        InputAccount inputAccount = new InputAccount();
//        inputAccount.setId(13);
//        inputAccount.setIdentifier(19);
//        inputAccount.setCompanion_id(20);
//        inputAccount.setMessage("1");
        json = gson.toJson(inputAccount);
        return json;
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
