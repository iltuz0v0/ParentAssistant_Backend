package net.nel.il.service;

import net.nel.il.account.InputAccount;
import net.nel.il.account.OutputAccount;
import net.nel.il.dao.*;
import net.nel.il.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {

    @Autowired
    private Handler handler;

    public MainService(){
        handler = new Handler();
    }

    @Autowired
    AccountCreationRepository accountCreationRepository;

    @Autowired
    DataReceivingRepository dataReceivingRepository;

    @Autowired
    DataUpdatingRepository dataUpdatingRepository;

    @Autowired
    InteractionRequestRepository interactionRequestRepository;

    @Autowired
    AcceptionRequestRepository acceptionRequestRepository;

    @Autowired
    RejectionRequestRepository rejectionRequestRepository;

    @Autowired
    BreakingRequestRepository breakingRequestRepository;

    @Autowired
    TestRepository testRepository;

    @Autowired
    TimeExceedRepository timeExceedRepository;

    @Autowired
    MessagingRepository messagingRepository;

    @Autowired
    FirstRequest firstRequest;

    @Autowired
    NoConnectionRepository noConnectionRepository;

    public OutputAccount handle(InputAccount inputAccount){
        return doRequest(inputAccount);
    }

    public String show(){
        return testRepository.test();
    }

    public String drop(){
        return testRepository.drop();
    }

    private OutputAccount doRequest(InputAccount inputAccount){
        OutputAccount outputAccount = new OutputAccount();
        createAccount(inputAccount.getId(), inputAccount, outputAccount);
        getData(inputAccount.getId(), inputAccount, outputAccount);
        updateData(inputAccount.getId(), inputAccount, outputAccount);
        doRequest(inputAccount.getId(), inputAccount, outputAccount);
        doAcception(inputAccount.getId(), inputAccount, outputAccount);
        doRejection(inputAccount.getId(), inputAccount, outputAccount);
        doBreak(inputAccount.getId(), inputAccount, outputAccount);
        doTimeOutside(inputAccount.getId(), inputAccount, outputAccount);
        doFirstRequest(inputAccount.getId(), inputAccount, outputAccount);
        doNoConnection(inputAccount.getId(), inputAccount, outputAccount);
        doMessaging(inputAccount.getId(), inputAccount, outputAccount);
        return outputAccount;
    }

    private void doMessaging(int id, InputAccount inputAccount,
                             OutputAccount outputAccount) {
        if (id == Handler.SENDING_MESSAGE) {
            messagingRepository.sendMessage(inputAccount, outputAccount);
        }
    }

    private void createAccount(int id, InputAccount inputAccount,
                                        OutputAccount outputAccount){
        if(id == Handler.CREATING_ACCOUNT_FROM
                || id == Handler.NEGATIVE_TRYING_TO_RETURN_OLD_ACCOUNT) {
            accountCreationRepository.createAccount(inputAccount,
                    System.currentTimeMillis(), outputAccount);
            System.out.println("create account 1");
        }
        if(id == Handler.POSITIVE_TRYING_TO_RETURN_OLD_ACCOUNT){
            System.out.println("create account 2");
            accountCreationRepository.returnAccount(inputAccount, outputAccount);
        }
    }

    private void getData(int id, InputAccount inputAccount,
                                  OutputAccount outputAccount){
        if(id == Handler.GETTING_DATA_FROM) {
            dataReceivingRepository.getData(inputAccount, outputAccount);
        }
    }

    private void updateData(int id, InputAccount inputAccount,
                                     OutputAccount outputAccount){
        if(id == Handler.UPDATE_DATA_FROM) {
            dataUpdatingRepository.updateData(inputAccount,
                    System.currentTimeMillis(), outputAccount);
        }
    }

    private void doRequest(int id, InputAccount inputAccount,
                                    OutputAccount outputAccount){
        if(id == Handler.REQUEST_FROM) {
            interactionRequestRepository.request(inputAccount, outputAccount);
        }
    }

    private void doAcception(int id, InputAccount inputAccount,
                             OutputAccount outputAccount){
        if(id == Handler.ACCEPT_FROM) {
            acceptionRequestRepository.accept(inputAccount, outputAccount);
        }
    }

    private void doRejection(int id, InputAccount inputAccount,
                             OutputAccount outputAccount){
        if(id == Handler.REJECT_FROM) {
            rejectionRequestRepository.reject(inputAccount, outputAccount);
        }
    }

    private void doBreak(int id, InputAccount inputAccount,
                         OutputAccount outputAccount){
        if(id == Handler.BREAK_FROM) {
            breakingRequestRepository.breakF(inputAccount, outputAccount);
        }
    }

    private void doTimeOutside(int id, InputAccount inputAccount,
                               OutputAccount outputAccount){
        if(id == Handler.TIME_OUTSIDE_TO) {
            timeExceedRepository.timeExceed(inputAccount, outputAccount);
        }
    }

    private void doFirstRequest(int id, InputAccount inputAccount,
                                OutputAccount outputAccount){
        if(id == Handler.FIRST_REQUEST_TO) {
            firstRequest.doFirstRequest(inputAccount, outputAccount);
        }
    }

    private void doNoConnection(int id, InputAccount inputAccount,
                                OutputAccount outputAccount){
        if(id == Handler.NO_CONNECTION) {
            noConnectionRepository.handleNoConnection(inputAccount, outputAccount);
        }
    }

}
