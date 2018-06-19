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

@Repository
public class TimeExceedRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public void timeExceed(InputAccount inputAccount, OutputAccount outputAccount){
        outputAccount.setId(Handler.UPDATE_DATA_TO);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        Client client = entityManager.find(Client.class, inputAccount.getIdentifier());
        Client companion = entityManager.find(Client.class, inputAccount.getCompanion_id());
        if(client.getCompanionId() > 0){
            client.setStatus(Handler.STATUS_AVAILABLE);
            client.setCompanionId(Handler.NO_REQUEST);
            companion.setCompanionId(Handler.WAS_DONE_BREAK);
        }
        else{
            client.setStatus(Handler.STATUS_AVAILABLE);
            client.setCompanionId(Handler.NO_REQUEST);
            companion.setCompanionId(Handler.NO_REQUEST);
        }
        entityManager.merge(client);
        entityManager.merge(companion);
        entityTransaction.commit();
        entityManager.close();
    }
}
