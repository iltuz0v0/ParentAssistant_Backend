package net.nel.il.account;

import java.util.ArrayList;
import java.util.List;

public class FullAccount {

    public List<String> names;

    public List<String> ages;

    public List<String> hobbies;

    public List<Double> lats;

    public List<Double> lngs;

    public List<Integer> statuses;

    public List<String> photos;

    public List<Integer> identifiers;

    public FullAccount(){
        names = new ArrayList<>();
        ages = new ArrayList<>();
        hobbies = new ArrayList<>();
        lats = new ArrayList<>();
        lngs = new ArrayList<>();
        statuses = new ArrayList<>();
        photos = new ArrayList<>();
        identifiers = new ArrayList<>();
    }
}
