package net.nel.il.dao;

import net.nel.il.account.FullAccount;
import net.nel.il.account.InputAccount;
import net.nel.il.account.OutputAccount;
import net.nel.il.entity.Client;
import net.nel.il.entity.ClientData;
import net.nel.il.entity.Message;
import net.nel.il.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class DataReceivingRepository {

    private final String lat = "lat";

    private final String lng = "lng";

    private final String radius = "radius";

    private final String DEFAULT_VALUE = "null";

    private final String current_t = "current_t";

    public static final int ENGAGED_TO = -1;

    public static final int ENGAGED_FROM = -2;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public void getData(InputAccount inputAccount, OutputAccount outputAccount){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Client client = entityManager.find(Client.class, inputAccount.getIdentifier());
        if(client.getCompanionId() == Handler.WAS_DONE_BREAK){
            doBreak(outputAccount, client, entityManager);
        }
        else {
            handle(client, entityManager,
                    outputAccount, inputAccount);
        }
        transaction.commit();
        entityManager.close();
    }

    private void handle(Client client, EntityManager entityManager,
                        OutputAccount outputAccount, InputAccount inputAccount){
        Client companion = null;
        if(client.getCompanionId() > 0){
            companion = entityManager.find(Client.class, client.getCompanionId());
        }
        if(isAcceptTo(client, companion)){
            acceptTo(client, entityManager, outputAccount);
        }
        else if(isRequestTo(client)){
            requestTo(outputAccount, client, companion);
        }
        else{
            switch (client.getCompanionId()){
                case Handler.NO_REQUEST:
                    handleData(inputAccount, entityManager, outputAccount);
                    refreshCommonData(inputAccount, entityManager);
                    handleMessages(inputAccount, outputAccount, entityManager);
                    break;
                case Handler.WAS_DONE_REJECTION:
                    doRejection(outputAccount, client, entityManager);
                    break;
                case Handler.WAS_DONE_NO_CONNECTION:
                    doNoConnection(outputAccount, client, entityManager);
                    break;
            }
            if(isNoRequest(client)){
                handleData(inputAccount, entityManager, outputAccount);
                refreshCommonData(inputAccount, entityManager);
                handleMessages(inputAccount, outputAccount, entityManager);
            }
        }
    }

    private boolean isAcceptTo(Client client, Client companion){
        return client.getCompanionId() > 0 && companion != null
                && companion.getCompanionId() == client.getId()
                && client.getStatus() != ENGAGED_TO
                && client.getStatus() != ENGAGED_FROM;
    }

    private boolean isRequestTo(Client client){
        return client.getCompanionId() > 0
                && client.getStatus() != ENGAGED_TO
                && client.getStatus() != ENGAGED_FROM;
    }

    private boolean isNoRequest(Client client){
        return (client.getStatus() == ENGAGED_TO
                && client.getCompanionId() > 0)
                || (client.getStatus() == ENGAGED_FROM
                && client.getCompanionId() > 0);
    }

    private void acceptTo(Client client, EntityManager entityManager,
                          OutputAccount outputAccount){
        client.setStatus(ENGAGED_TO);
        entityManager.merge(client);
        outputAccount.setId(Handler.ACCEPT_TO);
    }

    private void requestTo(OutputAccount outputAccount, Client client,
                           Client companion){
        outputAccount.setId(Handler.REQUEST_TO);
        if(companion != null) {
            outputAccount.setCompanionId(client.getCompanionId());
            outputAccount.setType(companion.getType());
        }
    }


    private void handleData(InputAccount inputAccount,
                                     EntityManager entityManager, OutputAccount outputAccount) {
        List<Integer> peopleIdentifiers = inputAccount.getPeopleIdentifiers();
        outputAccount.setId(Handler.GET_DATA_TO);
        FullAccount fullAccount = new FullAccount();
        List<Client> clients = findPeopleByRadius(entityManager, inputAccount);
        for (Client client : clients) {
            if(inputAccount.getCompanion_id() != client.getId()){
                if ((isSender(client, inputAccount) || isUnavailable(client))) {
                    continue;
                }
            }
            if (peopleIdentifiers != null && peopleIdentifiers.contains(client.getId())) {
                fillDefaultAccountFields(fullAccount);
                fillCommonFields(fullAccount, client);
            } else {
                if (client.getClientData().size() != 0) {
                    for (ClientData clientData : client.getClientData()){
                        fillAccountFields(fullAccount, clientData);
                        fillCommonFields(fullAccount, client);
                    }
                } else {
                    fillDefaultAccountFields(fullAccount);
                    fillCommonFields(fullAccount, client);
                }
            }
        }
        outputAccount.setLists(fullAccount);
    }

    private boolean isSender(Client client, InputAccount inputAccount){
        return client.getId() == inputAccount.getIdentifier();
    }

    private boolean isUnavailable(Client client){
        return client.getStatus() <= Handler.STATUS_UNAVAILABLE;
    }

    @SuppressWarnings("all")
    private List<Client> findPeopleByRadius(EntityManager entityManager,
                                    InputAccount inputAccount){
        Query query = entityManager.createQuery("SELECT c FROM Client c" +
                        " WHERE (c.lat-:lat)*(c.lat-:lat) + " +
                        "(c.lng-:lng)*(c.lng-:lng) < :radius*:radius and " +
                        "c.time + 120000 > :current_t", Client.class);
        query.setParameter(lat, inputAccount.getLat());
        query.setParameter(lng, inputAccount.getLng());
        query.setParameter(radius,  inputAccount.getRadius());
        query.setParameter(current_t, System.currentTimeMillis());
        List<Client> clients = (List<Client>) query.getResultList();
        addOutsideCompanion(clients, entityManager, inputAccount);
        return clients;
    }

    private void addOutsideCompanion(List<Client> clients, EntityManager entityManager,
                                    InputAccount inputAccount){
        boolean isCompanion = false;
        if(inputAccount.getCompanion_id() > 0) {
            for (Client client : clients) {
                if (client.getId() == inputAccount.getCompanion_id()) {
                    isCompanion = true;
                    break;
                }
            }
            if (!isCompanion) {
                Client companionClient = entityManager.find(Client.class, inputAccount.getCompanion_id());
                if(companionClient.getTime() + 120000 > System.currentTimeMillis()) {
                    clients.add(companionClient);
                }
            }
        }
    }

    private void fillCommonFields(FullAccount fullAccount, Client innerClient){
        fullAccount.lats.add(innerClient.getLat());
        fullAccount.lngs.add(innerClient.getLng());
        fullAccount.statuses.add(innerClient.getStatus());
        fullAccount.identifiers.add(innerClient.getId());
    }

    private void refreshCommonData(InputAccount inputAccount, EntityManager entityManager){
        Client client = entityManager.find(Client.class, inputAccount.getIdentifier());
        client.setLat(inputAccount.getLat());
        client.setLng(inputAccount.getLng());
        client.setRadius(inputAccount.getRadius());
        client.setTime(System.currentTimeMillis());
        entityManager.merge(client);
    }

    private void fillAccountFields(FullAccount fullAccount, ClientData clientData){
        fullAccount.names.add(clientData.getName());
        fullAccount.ages.add(clientData.getAge());
        fullAccount.hobbies.add(clientData.getHobby());
        fullAccount.photos.add(clientData.getPhoto());
    }

    private void fillDefaultAccountFields(FullAccount fullAccount){
        fullAccount.names.add(DEFAULT_VALUE);
        fullAccount.ages.add(DEFAULT_VALUE);
        fullAccount.hobbies.add(DEFAULT_VALUE);
        fullAccount.photos.add(DEFAULT_VALUE);
    }

    private void doRejection(OutputAccount outputAccount, Client client,
                             EntityManager entityManager){
        outputAccount.setId(Handler.REJECT_TO);
        client.setStatus(Handler.STATUS_AVAILABLE);
        client.setCompanionId(Handler.NO_REQUEST);
        entityManager.merge(client);
    }

    private void doNoConnection(OutputAccount outputAccount, Client client,
                                EntityManager entityManager){
        outputAccount.setId(Handler.NO_CONNECTION);
        client.setStatus(Handler.STATUS_AVAILABLE);
        client.setCompanionId(Handler.NO_REQUEST);
        entityManager.merge(client);
    }

    private void doBreak(OutputAccount outputAccount, Client client,
                         EntityManager entityManager){
        outputAccount.setId(Handler.BREAK_TO);
        client.setStatus(Handler.STATUS_AVAILABLE);
        client.setCompanionId(Handler.NO_REQUEST);
        entityManager.merge(client);
    }

    @SuppressWarnings("all")
    private void handleMessages(InputAccount inputAccount, OutputAccount outputAccount,
                                EntityManager entityManager){
        if(inputAccount.getMessagesAmount() != null) {
            Query query = entityManager.createQuery("select m from Message m " +
                    "where m.askingId = :id_client and m.companionId = :id_companion");
            query.setParameter("id_client", inputAccount.getCompanion_id());
            query.setParameter("id_companion", inputAccount.getIdentifier());
            List<Message> messages = (List<Message>) query.getResultList();
            if(messages.size() > inputAccount.getMessagesAmount()){
                fillMessages(outputAccount, messages, inputAccount);
            }
        }
    }

    private void fillMessages(OutputAccount outputAccount, List<Message> messages,
                             InputAccount inputAccount){
        List<String> outputMessages = new ArrayList<>();
        messages.sort(new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                int result;
                if(o1.getId() > o2.getId()){
                    result = 1;
                }
                else if(o1.getId() < o2.getId()){
                    result = -1;
                }
                else {
                    result = 0;
                }
                return result;
            }
        });
        for(int element = inputAccount.getMessagesAmount(); element < messages.size(); element++){
            outputMessages.add(messages.get(element).getMessage());
        }
        outputAccount.setMessages(outputMessages);
    }

}
