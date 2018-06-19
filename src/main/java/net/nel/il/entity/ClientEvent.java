package net.nel.il.entity;


import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@Table(name = "events")
public class ClientEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "point_one_lat_max", columnDefinition = "double")
    private Double pointOneLatMax;

    @Column(name = "point_one_lng_max", columnDefinition = "double")
    private Double pointOneLngMax;

    @Column(name = "point_two_lat_min", columnDefinition = "double")
    private Double pointTwoLatMin;

    @Column(name = "point_two_lng_min", columnDefinition = "double")
    private Double pointTwoLngMin;

    @Column(name = "year")
    private Integer year;

    @Column(name = "month")
    private Integer month;

    @Column(name = "day")
    private Integer day;

    @Column(name = "hour_from")
    private Integer hourFrom;

    @Column(name = "minute_from")
    private Integer minuteFrom;

    @Column(name = "hour_to")
    private Integer hourTo;

    @Column(name = "minute_to")
    private Integer minuteTo;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "outer_id")
    private Client clientEv;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPointOneLatMax() {
        return pointOneLatMax;
    }

    public void setPointOneLatMax(Double pointOneLatMax) {
        this.pointOneLatMax = pointOneLatMax;
    }

    public Double getPointOneLngMax() {
        return pointOneLngMax;
    }

    public void setPointOneLngMax(Double pointOneLngMax) {
        this.pointOneLngMax = pointOneLngMax;
    }

    public Double getPointTwoLatMin() {
        return pointTwoLatMin;
    }

    public void setPointTwoLatMin(Double pointTwoLatMin) {
        this.pointTwoLatMin = pointTwoLatMin;
    }

    public Double getPointTwoLngMin() {
        return pointTwoLngMin;
    }

    public void setPointTwoLngMin(Double pointTwoLngMin) {
        this.pointTwoLngMin = pointTwoLngMin;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHourFrom() {
        return hourFrom;
    }

    public void setHourFrom(Integer hourFrom) {
        this.hourFrom = hourFrom;
    }

    public Integer getMinuteFrom() {
        return minuteFrom;
    }

    public void setMinuteFrom(Integer minuteFrom) {
        this.minuteFrom = minuteFrom;
    }

    public Integer getHourTo() {
        return hourTo;
    }

    public void setHourTo(Integer hourTo) {
        this.hourTo = hourTo;
    }

    public Integer getMinuteTo() {
        return minuteTo;
    }

    public void setMinuteTo(Integer minuteTo) {
        this.minuteTo = minuteTo;
    }

    public Client getClientEv() {
        return clientEv;
    }

    public void setClientEv(Client clientEv) {
        this.clientEv = clientEv;
    }
}
