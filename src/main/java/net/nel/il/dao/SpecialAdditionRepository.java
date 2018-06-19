package net.nel.il.dao;


import net.nel.il.account.InputAccount;
import net.nel.il.account.OutputAccount;
import net.nel.il.entity.Client;
import net.nel.il.entity.ClientEvent;
import net.nel.il.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class SpecialAdditionRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public OutputAccount handle(InputAccount inputAccount){
        OutputAccount outputAccount = new OutputAccount();
        outputAccount.setId(Handler.UPDATE_DATA_TO);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Client client = entityManager.find(Client.class, inputAccount.getIdentifier());
        mergeClientEvent(client, inputAccount);
        entityManager.merge(client);
        transaction.commit();
        entityManager.close();
        return outputAccount;
    }

    @SuppressWarnings("all")
    private void mergeClientEvent(Client client, InputAccount inputAccount){
        String []timeFrom = inputAccount.getFrom().split(":");
        String []timeTo = inputAccount.getTo().split(":");
        Integer hourFrom = Integer.parseInt(timeFrom[0]);
        Integer minuteFrom = Integer.parseInt(timeFrom[1]);
        Integer hourTo = Integer.parseInt(timeTo[0]);
        Integer minuteTo = Integer.parseInt(timeTo[1]);
        Date date = new Date(System.currentTimeMillis());
        List<ClientEvent> clientEventList = new ArrayList<>();
        ClientEvent clientEvent = new ClientEvent();
        clientEvent.setClientEv(client);
        clientEvent.setYear(date.getYear());
        clientEvent.setMonth(date.getMonth());
        clientEvent.setDay(date.getDay());
        clientEvent.setMinuteFrom(minuteFrom);
        clientEvent.setMinuteTo(minuteTo);
        clientEvent.setHourFrom(hourFrom);
        clientEvent.setHourTo(hourTo);
        clientEvent.setPointOneLatMax(getMax(inputAccount.getPointOneLat(),
                inputAccount.getPointTwoLat()));
        clientEvent.setPointOneLngMax(getMax(inputAccount.getPointOneLng(),
                inputAccount.getPointTwoLng()));
        clientEvent.setPointTwoLatMin(getMin(inputAccount.getPointOneLat(),
                inputAccount.getPointTwoLat()));
        clientEvent.setPointTwoLngMin(getMin(inputAccount.getPointOneLng(),
                inputAccount.getPointTwoLng()));
        clientEventList.addAll(client.getClientEvents());
        clientEventList.add(clientEvent);
        client.setClientEvents(clientEventList);
    }

    private Double getMax(Double a, Double b){
        if(a > b){
            return a;
        }
        else {
            return b;
        }
    }

    private Double getMin(Double a, Double b){
        if(a < b){
            return a;
        }
        else{
            return b;
        }
    }
}
