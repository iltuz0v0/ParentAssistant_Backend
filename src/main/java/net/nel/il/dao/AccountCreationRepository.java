package net.nel.il.dao;

import net.nel.il.account.InputAccount;
import net.nel.il.account.OutputAccount;
import net.nel.il.entity.Client;
import net.nel.il.entity.ClientData;
import net.nel.il.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountCreationRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public void createAccount(InputAccount inputAccount, Long time,
                                       OutputAccount outputAccount){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        if(isOldAccount(entityManager, inputAccount)
                && inputAccount.getId() != Handler.NEGATIVE_TRYING_TO_RETURN_OLD_ACCOUNT){
            outputAccount.setId(Handler.TRYING_TO_RETURN_OLD_ACCOUNT);
            return;
        }
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Client client = new Client(inputAccount.getLat(), inputAccount.getLng(),
                inputAccount.getRadius(), inputAccount.getGoogleIdentifier());
        client.setTime(time);
        entityManager.persist(client);
        persistClientData(inputAccount, client);
        entityManager.merge(client);
        transaction.commit();
        outputAccount.setIdentifier(client.getId());
        outputAccount.setId(Handler.CREATE_ACCOUNT_TO);
        entityManager.close();
    }

    public void returnAccount(InputAccount inputAccount, OutputAccount outputAccount){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        List<String> names = new ArrayList<>();
        List<String> ages = new ArrayList<>();
        List<String> hobbies = new ArrayList<>();
        List<String> photos = new ArrayList<>();

        Client oldClient = getOldAccount(entityManager, inputAccount);
        for(ClientData clientData : oldClient.getClientData()){
            names.add(clientData.getName());
            ages.add(clientData.getAge());
            hobbies.add(clientData.getHobby());
            photos.add(clientData.getPhoto());
        }
        outputAccount.setIdentifier(oldClient.getId());
        outputAccount.setNames(names);
        outputAccount.setAges(ages);
        outputAccount.setHobbies(hobbies);
        outputAccount.setPhotos(photos);

        outputAccount.setId(Handler.CREATE_ACCOUNT_TO);
        transaction.commit();
        entityManager.close();
    }

    private void persistClientData(InputAccount inputAccount,
                               Client client){
        if(inputAccount.getName() != null) {
            List<ClientData> clientDataList = new ArrayList<>();
            for (int element = 0; element < inputAccount.getName().size(); element++) {
                ClientData clientData = new ClientData();
                clientData.setName(inputAccount.getName().get(element));
                clientData.setAge(inputAccount.getAge().get(element));
                clientData.setHobby(inputAccount.getHobbies().get(element));
                clientData.setPhoto(inputAccount.getPhotos().get(element));
                clientData.setClient(client);
                clientDataList.add(clientData);
            }
            client.setClientData(clientDataList);
        }
    }

    @SuppressWarnings("all")
    private boolean isOldAccount(EntityManager entityManager, InputAccount inputAccount){
        Query query = entityManager.createQuery("SELECT c FROM Client c " +
                "where c.googleIdentifier = :googleIdentifier");
        query.setParameter("googleIdentifier", inputAccount.getGoogleIdentifier());
        List<Client> list = query.getResultList();
        return list.size() != 0;

    }

    private Client getOldAccount(EntityManager entityManager, InputAccount inputAccount){
        Query query = entityManager.createQuery("SELECT c FROM Client c " +
                "where c.googleIdentifier = :googleIdentifier");
        query.setParameter("googleIdentifier", inputAccount.getGoogleIdentifier());
        return (Client) query.getResultList().get(0);
    }
}
