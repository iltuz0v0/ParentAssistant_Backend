package net.nel.il.entity;


import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "message", columnDefinition = "varchar(255) character set utf8")
    private String message;

    @Column(name = "asking_id")
    private Integer askingId;

    @Column(name = "companion_id")
    private Integer companionId;

    public Message() {
    }

    public Message(String message, Integer askingId, Integer companionId) {
        this.message = message;
        this.askingId = askingId;
        this.companionId = companionId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getAskingId() {
        return askingId;
    }

    public void setAskingId(Integer askingId) {
        this.askingId = askingId;
    }

    public Integer getCompanionId() {
        return companionId;
    }

    public void setCompanionId(Integer companionId) {
        this.companionId = companionId;
    }
}
