package net.nel.il.service;


import net.nel.il.account.InputAccount;
import net.nel.il.account.OutputAccount;
import net.nel.il.dao.SpecialAdditionRepository;
import net.nel.il.dao.SpecialRepository;
import net.nel.il.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecialService {

    @Autowired
    SpecialRepository specialRepository;

    @Autowired
    SpecialAdditionRepository specialAdditionRepository;

    public OutputAccount handle(InputAccount inputAccount){
        if(inputAccount.getId() == Handler.EVENT_REQUEST){
            return specialRepository.handle(inputAccount);
        }
        if(inputAccount.getId() == Handler.EVENT_CREATION_REQUEST){
            return specialAdditionRepository.handle(inputAccount);
        }
        else{
            return null;
        }
    }

}
