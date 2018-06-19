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
public class FirstRequest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public void doFirstRequest(InputAccount inputAccount, OutputAccount outputAccount){
        outputAccount.setId(Handler.UPDATE_DATA_TO);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Client client = entityManager.find(Client.class, inputAccount.getIdentifier());
        client.setStatus(Handler.STATUS_AVAILABLE);
        client.setCompanionId(Handler.NO_REQUEST);
        transaction.commit();
    }

}
