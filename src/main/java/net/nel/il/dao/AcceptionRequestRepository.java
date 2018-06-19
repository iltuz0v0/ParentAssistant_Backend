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
public class AcceptionRequestRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public void accept(InputAccount inputAccount, OutputAccount outputAccount){
        outputAccount.setId(Handler.UPDATE_DATA_TO);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Client client = entityManager.find(Client.class, inputAccount.getIdentifier());
        Client companion = entityManager.find(Client.class, inputAccount.getCompanion_id());
        if(client.getCompanionId() == Handler.NO_REQUEST){
            client.setCompanionId(Handler.WAS_DONE_BREAK);
            //client.setStatus(Handler.STATUS_AVAILABLE);
        }else{
            companion.setCompanionId(client.getId());
            client.setStatus(DataReceivingRepository.ENGAGED_FROM);
        }
        entityManager.merge(client);
        entityManager.merge(companion);
        transaction.commit();
        entityManager.close();
    }
}
