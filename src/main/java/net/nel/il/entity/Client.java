package net.nel.il.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@Table(name = "clients")
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "lat", columnDefinition = "double")
    private Double lat;

    @Column(name = "lng", columnDefinition = "double")
    private Double lng;

    @Column(name = "radius")
    private Float radius;

    @Column(name = "status", columnDefinition = "int default 1")
    private Integer status;

    @Column(name = "time", columnDefinition = "bigint default 0")
    private Long time;

    @Column(name = "google_identifier")
    private String googleIdentifier;

    @Column(name = "type", columnDefinition = "int default 0")
    private Integer type;

    @Column(name = "companionId", columnDefinition = "int default -1")
    private Integer companionId;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ClientData> clientData;

    @OneToMany(mappedBy = "clientEv", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ClientEvent> clientEvents;

    public Client(){

    }

    public Client(Double lat, Double lng, Float radius,
                  Integer status, Integer companionId) {
        this.lat = lat;
        this.lng = lng;
        this.radius = radius;
        this.status = status;
        this.companionId = companionId;
    }

    public Client(Double lat, Double lng, Float radius, String googleIdentifier) {
        this.lat = lat;
        this.lng = lng;
        this.radius = radius;
        this.googleIdentifier = googleIdentifier;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCompanionId() {
        return companionId;
    }

    public void setCompanionId(Integer companionId) {
        this.companionId = companionId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public List<ClientData> getClientData() {
        return clientData;
    }

    public void setClientData(List<ClientData> clientData) {
        this.clientData = clientData;
    }

    public String getGoogleIdentifier() {
        return googleIdentifier;
    }

    public void setGoogleIdentifier(String googleIdentifier) {
        this.googleIdentifier = googleIdentifier;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<ClientEvent> getClientEvents() {
        return clientEvents;
    }

    public void setClientEvents(List<ClientEvent> clientEvents) {
        this.clientEvents = clientEvents;
    }
}

