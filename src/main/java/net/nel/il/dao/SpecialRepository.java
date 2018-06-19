package net.nel.il.dao;

import net.nel.il.account.FullSearchingAccount;
import net.nel.il.account.InputAccount;
import net.nel.il.account.OutputAccount;
import net.nel.il.entity.Client;
import net.nel.il.entity.ClientData;
import net.nel.il.entity.ClientEvent;
import net.nel.il.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.*;

@Repository
public class SpecialRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private static final String DEFAULT_VALUE = "null";

    private static final String YEAR = "year";

    private static final String MONTH = "month";

    private static final String DAY = "day";

    private static final String HOUR_FROM = "hourFrom";

    private static final String HOUR_TO = "hourTo";

    private static final String MIN_LAT = "minLat";

    private static final String MAX_LAT = "maxLat";

    private static final String MIN_LNG = "minLng";

    private static final String MAX_LNG = "maxLng";

    public OutputAccount handle(InputAccount inputAccount){
        OutputAccount outputAccount = new OutputAccount();
        outputAccount.setId(Handler.EVENT_REQUEST);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        List<ClientEvent> clientEvents = query(entityManager, inputAccount);
        FullSearchingAccount fullSearchingAccount = new FullSearchingAccount();
        for(ClientEvent clientEvent : clientEvents){
            Client client = clientEvent.getClientEv();
            if(client.getId() == inputAccount.getIdentifier()){
                continue;
            }
            if(client.getClientData().size() != 0) {
                for (ClientData clientData : client.getClientData()) {
                    fillAccountFields(fullSearchingAccount, clientData);
                    fillOtherFields(fullSearchingAccount, clientEvent, client);
                }
            }
            else{
                fillDefaultAccountFields(fullSearchingAccount);
                fillOtherFields(fullSearchingAccount, clientEvent, client);
            }
        }
        outputAccount.setSearchingLists(fullSearchingAccount);
        transaction.commit();
        entityManager.close();
        return outputAccount;
    }

    @SuppressWarnings("all")
    private List<ClientEvent> query(EntityManager entityManager, InputAccount inputAccount){
        double maxLat = getMax(inputAccount.getPointOneLat(), inputAccount.getPointTwoLat());
        double minLat = getMin(inputAccount.getPointOneLat(), inputAccount.getPointTwoLat());
        double maxLng = getMax(inputAccount.getPointOneLng(), inputAccount.getPointTwoLng());
        double minLng = getMin(inputAccount.getPointOneLng(), inputAccount.getPointTwoLng());
        String []timeFrom = inputAccount.getFrom().split(":");
        String []timeTo = inputAccount.getTo().split(":");
        Date date = new Date(System.currentTimeMillis());
        Integer hourFrom = Integer.parseInt(timeFrom[0]);
        Integer minuteFrom = Integer.parseInt(timeFrom[1]);
        Integer hourTo = Integer.parseInt(timeTo[0]);
        Integer minuteTo = Integer.parseInt(timeTo[1]);
        Query query = entityManager.createQuery("SELECT c FROM ClientEvent c " +
                " WHERE c.year = :year and c.month = :month and c.day = :day " +
                "and c.hourFrom <= :hourFrom " +
                "and c.hourTo >= :hourTo and not (:minLat >= c.pointOneLatMax or " +
                ":maxLat <= c.pointTwoLatMin or :minLng >= c.pointOneLngMax or " +
                ":maxLng <= c.pointTwoLngMin)", ClientEvent.class);
        query.setParameter(YEAR, date.getYear());
        query.setParameter(MONTH, date.getMonth());
        query.setParameter(DAY, date.getDay());
        query.setParameter(HOUR_FROM, hourFrom);
        query.setParameter(HOUR_TO, hourTo);
        query.setParameter(MIN_LAT, minLat);
        query.setParameter(MAX_LAT, maxLat);
        query.setParameter(MIN_LNG, minLng);
        query.setParameter(MAX_LNG, maxLng);
        return (List<ClientEvent>) query.getResultList();
    }

    private void fillDefaultAccountFields(FullSearchingAccount fullSearchingAccount){
        fullSearchingAccount.names.add(DEFAULT_VALUE);
        fullSearchingAccount.ages.add(DEFAULT_VALUE);
        fullSearchingAccount.hobbies.add(DEFAULT_VALUE);
        fullSearchingAccount.photos.add(DEFAULT_VALUE);
    }

    private void fillAccountFields(FullSearchingAccount fullSearchingAccount,
                                   ClientData clientData){
        fullSearchingAccount.names.add(clientData.getName());
        fullSearchingAccount.ages.add(clientData.getAge());
        fullSearchingAccount.hobbies.add(clientData.getHobby());
        fullSearchingAccount.photos.add(clientData.getPhoto());
    }

    private void fillOtherFields(FullSearchingAccount fullSearchingAccount,
                                 ClientEvent clientEvent, Client client){
        fullSearchingAccount.identifiers.add(client.getId());
        fullSearchingAccount.from.add(Integer.toString(clientEvent.getHourFrom()) +
                ":" + Integer.toString(clientEvent.getMinuteFrom()));
        fullSearchingAccount.to.add(Integer.toString(clientEvent.getHourTo()) +
                ":" + Integer.toString(clientEvent.getMinuteTo()));
        fullSearchingAccount.pointOneLat.add(clientEvent.getPointOneLatMax());
        fullSearchingAccount.pointOneLng.add(clientEvent.getPointOneLngMax());
        fullSearchingAccount.pointTwoLat.add(clientEvent.getPointTwoLatMin());
        fullSearchingAccount.pointTwoLng.add(clientEvent.getPointTwoLngMin());
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
