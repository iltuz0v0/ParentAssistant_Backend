package net.nel.il.dao;

import net.nel.il.entity.Client;
import net.nel.il.entity.ClientData;
import net.nel.il.entity.ClientEvent;
import net.nel.il.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TestRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public String test(){
        StringBuilder builder = new StringBuilder();
        builder.append("___________________________________");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        Query query = entityManager.createQuery("SELECT c FROM Client c" +
                " WHERE c.id > 0");
        List list = query.getResultList();
        for(int i = 0; i < list.size(); i++){
            Client client = (Client) list.get(i);
            builder.append("id:" + client.getId() + " ");
            builder.append("time:" + client.getTime() + " ");
            builder.append("lat:" + client.getLat() + " ");
            builder.append("lng:" + client.getLng() + " ");
            builder.append("radius:" + client.getRadius() + " ");
            builder.append("status:" + client.getStatus() + " ");
            builder.append("companion_id:" + client.getCompanionId() + " ");
            builder.append("google_identifier: " + client.getGoogleIdentifier() + " ");
            builder.append("<br>");
            List<ClientData> clientDatas = (List<ClientData>)
                    entityManager.createQuery("select c from ClientData c " +
                            "where outer_id = :id")
                            .setParameter("id", client.getId())
                            .getResultList();
            if(clientDatas != null) {
                for (ClientData clientData : clientDatas) {
                    builder.append("id: " + clientData.getId() + " ");
                    builder.append("name: " + clientData.getName() + " ");
                    builder.append("age: " + clientData.getAge() + " ");
                    builder.append("hobby: " + clientData.getHobby() + " ");
                    builder.append("photo: " + clientData.getPhoto() + " ");
                    builder.append("<br>");
                }
            }
            builder.append("<br>");
            builder.append("<br>");
        }
        Query query1 = entityManager.createQuery("select m from Message m where " +
                "m.id > 0");
        List<Message> messages = query1.getResultList();
        for(Message message: messages){
            builder.append("id " + message.getId());
            builder.append("message " + message.getMessage() + " ");
            builder.append("client_id " + message.getAskingId() + " ");
            builder.append("companion_id " + message.getCompanionId());
            builder.append("<br>");
        }
        builder.append("<br>");
        builder.append("<br>");
        Query query2 = entityManager.createQuery("select c from ClientEvent c where " +
                "c.id > 0");
        List<ClientEvent> clientEvents = query2.getResultList();
        for(ClientEvent clientEvent : clientEvents){
            builder.append("id " + clientEvent.getId() + " ");
            builder.append("year " + clientEvent.getYear() + " ");
            builder.append("month " + clientEvent.getMonth() + " ");
            builder.append("day " + clientEvent.getDay() + " ");
            builder.append("hourFrom " + clientEvent.getHourFrom() + " ");
            builder.append("minuteFrom " + clientEvent.getMinuteFrom() + " ");
            builder.append("hourTo " + clientEvent.getHourTo() + " ");
            builder.append("minuteTo " + clientEvent.getMinuteTo() + " ");
            builder.append("pointOneLatMax " + clientEvent.getPointOneLatMax() + " ");
            builder.append("pointOneLngMax " + clientEvent.getPointOneLngMax() + " ");
            builder.append("pointTwoLatMin " + clientEvent.getPointTwoLatMin() + " ");
            builder.append("pointTwoLngMin " + clientEvent.getPointTwoLngMin() + " ");
            builder.append("<br>");
        }
        entityTransaction.commit();
        entityManager.close();
        return builder.toString();
    }

    public String drop(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Query query1 = entityManager.createQuery("delete from ClientData");
        query1.executeUpdate();
        Query query = entityManager.createQuery("delete from Client");
        query.executeUpdate();
        transaction.commit();
        return "D_rop";
    }
}
