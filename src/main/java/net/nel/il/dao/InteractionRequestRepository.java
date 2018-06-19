package net.nel.il.dao;

import net.nel.il.account.InputAccount;
import net.nel.il.account.OutputAccount;
import net.nel.il.entity.Client;
import net.nel.il.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Repository
public class InteractionRequestRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public void request(InputAccount inputAccount, OutputAccount outputAccount){
        outputAccount.setId(Handler.CLEAR_MESSAGES);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Client client = entityManager.find(Client.class, inputAccount.getIdentifier());
        Client companion = entityManager.find(Client.class, inputAccount.getCompanion_id());
        if(inputAccount.getCompanion_id() == client.getCompanionId()){
            clearMessages(entityManager, inputAccount);
            outputAccount.setId(Handler.ACCEPT_TO);
            companion.setCompanionId(inputAccount.getIdentifier());
        }
        else if(fullAvailable(client, companion)) {
            companionRequest(client, companion, entityManager, inputAccount);
        }
        else if(companionAvailable(client, companion)){
            client.setCompanionId(Handler.NO_REQUEST);
            companionRequest(client, companion, entityManager, inputAccount);
        }
        else{
            outputAccount.setId(Handler.BUSY);
            client.setStatus(Handler.STATUS_AVAILABLE);
            client.setCompanionId(Handler.NO_REQUEST);
        }
        entityManager.merge(client);
        entityManager.merge(companion);
        transaction.commit();
        entityManager.close();
    }

    private boolean fullAvailable(Client client, Client companion){
        return client.getStatus() == Handler.STATUS_AVAILABLE
                && companion.getStatus() == Handler.STATUS_AVAILABLE
                && companion.getCompanionId() == Handler.NO_REQUEST
                && client.getCompanionId() == Handler.NO_REQUEST;
    }

    private boolean companionAvailable(Client client, Client companion){
        return (client.getStatus() == Handler.STATUS_UNAVAILABLE ||
                client.getCompanionId() > 0)
                && companion.getCompanionId() == Handler.NO_REQUEST
                && companion.getStatus() == Handler.STATUS_AVAILABLE;
    }

    private void companionRequest(Client client, Client companion,
                                  EntityManager entityManager, InputAccount inputAccount){
        clearMessages(entityManager, inputAccount);
        client.setStatus(Handler.STATUS_UNAVAILABLE);
        client.setType(inputAccount.getType());
        companion.setStatus(Handler.STATUS_UNAVAILABLE);
        companion.setCompanionId(client.getId());
    }

    private void clearMessages(EntityManager entityManager, InputAccount inputAccount){
        Query queryClientData = entityManager.createQuery("delete from Message m where " +
                "m.askingId = :id_client and m.companionId = :id_companion");
        queryClientData.setParameter("id_client", inputAccount.getIdentifier());
        queryClientData.setParameter("id_companion", inputAccount.getCompanion_id());
        queryClientData.executeUpdate();
        Query queryCompanionData = entityManager.createQuery("delete from Message m where " +
                "m.askingId = :id_client and m.companionId = :id_companion");
        queryCompanionData.setParameter("id_client", inputAccount.getCompanion_id());
        queryCompanionData.setParameter("id_companion", inputAccount.getIdentifier());
        queryCompanionData.executeUpdate();
    }

}
