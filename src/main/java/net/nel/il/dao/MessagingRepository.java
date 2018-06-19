package net.nel.il.dao;

import net.nel.il.account.InputAccount;
import net.nel.il.account.OutputAccount;
import net.nel.il.entity.Message;
import net.nel.il.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@Repository
public class MessagingRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public void sendMessage(InputAccount inputAccount, OutputAccount outputAccount){
        outputAccount.setId(Handler.UPDATE_DATA_TO);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Message message = new Message(inputAccount.getMessage(), inputAccount.getIdentifier(),
                inputAccount.getCompanion_id());
        entityManager.persist(message);
        transaction.commit();
        entityManager.close();
    }

}
