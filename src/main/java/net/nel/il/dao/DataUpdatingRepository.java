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
import java.util.ArrayList;
import java.util.List;

@Repository
public class DataUpdatingRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public void updateData(InputAccount inputAccount, Long time,
                                    OutputAccount outputAccount){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Client oldClient = entityManager.find(Client.class, inputAccount.getIdentifier());
        mergeClientData(inputAccount, oldClient);
        oldClient.setTime(time);
        oldClient.setGoogleIdentifier(inputAccount.getGoogleIdentifier());
        entityManager.merge(oldClient);
        outputAccount.setId(Handler.UPDATE_DATA_TO);
        transaction.commit();
        entityManager.close();
    }

    private void mergeClientData(InputAccount inputAccount, Client oldClient){
        oldClient.setLat(inputAccount.getLat());
        oldClient.setLng(inputAccount.getLng());
        oldClient.setRadius(inputAccount.getRadius());
        List<ClientData> clientDataList = new ArrayList<>();
        for(int element = 0; element < inputAccount.getName().size(); element++){
            ClientData clientData = new ClientData();
            if(oldClient.getClientData().size() > element) {
                clientData.setId(oldClient.getClientData().get(element).getId());
            }
            clientData.setName(inputAccount.getName().get(element));
            clientData.setAge(inputAccount.getAge().get(element));
            clientData.setHobby(inputAccount.getHobbies().get(element));
            clientData.setPhoto(inputAccount.getPhotos().get(element));
            clientData.setClient(oldClient);
            clientDataList.add(clientData);
        }
        oldClient.setClientData(clientDataList);
    }

}
