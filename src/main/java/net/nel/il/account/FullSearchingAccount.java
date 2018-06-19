package net.nel.il.account;

import java.util.ArrayList;
import java.util.List;

public class FullSearchingAccount {

    public List<Integer> identifiers;

    public List<String> names;

    public List<String> ages;

    public List<String> hobbies;

    public List<String> photos;

    public List<String> to;

    public List<String> from;

    public List<Double> pointOneLat;

    public List<Double> pointOneLng;

    public List<Double> pointTwoLat;

    public List<Double> pointTwoLng;

    public FullSearchingAccount(){
        identifiers = new ArrayList<>();
        names = new ArrayList<>();
        ages = new ArrayList<>();
        hobbies = new ArrayList<>();
        photos = new ArrayList<>();
        to = new ArrayList<>();
        from = new ArrayList<>();
        pointOneLat = new ArrayList<>();
        pointOneLng = new ArrayList<>();
        pointTwoLat = new ArrayList<>();
        pointTwoLng = new ArrayList<>();
    }
}
