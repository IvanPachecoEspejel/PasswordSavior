package com.ivanpacheco.passwordsavior.greenDAO.Entities;

import android.content.Intent;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Date;

/**
 * Created by ivanpacheco on 21/02/18.
 * Entity for create table Credential in database
 */

@Entity
public class Credential {
    @Id(autoincrement = true)
    private Long id;

    @Property
    @NotNull
    @Index(name = "UNIQUE_CREDENTIAL", unique = true)
    private String appName;

    @Property
    @NotNull
    private String userName;

    @Property
    @NotNull
    private String passWord;

    @Property
    @NotNull
    private Long creationDate;

    @Property
    @NotNull
    private Long lastUpdate;

    @Transient
    private Integer type;

    @Transient
    public final static int TYPE_NORMAL = 0;
    @Transient
    public static final int TYPE_DETAIL = 1;
    @Transient
    public static final int TYPE_INPUT = 2;

    @Generated(hash = 1261823263)
    public Credential(Long id, @NotNull String appName, @NotNull String userName,
            @NotNull String passWord, @NotNull Long creationDate,
            @NotNull Long lastUpdate) {
        this.id = id;
        this.appName = appName;
        this.userName = userName;
        this.passWord = passWord;
        this.creationDate = creationDate;
        this.lastUpdate = lastUpdate;
    }

    @Generated(hash = 943805485)
    public Credential() {
    }

    public Credential(Credential c, Integer type){
        this.userName = c.getUserName();
        this.appName = c.getAppName();
        this.passWord = c.getPassWord();
        this.type = type;
    }

    public Credential(@NotNull String appName, @NotNull String userName,
                      @NotNull String passWord) {
        this.appName = appName;
        this.userName = userName;
        this.passWord = passWord;

        this.creationDate = new Date().getTime();
        this.lastUpdate = this.creationDate;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return this.passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Long getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public Long getLastUpdate() {
        return this.lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        if(type == null)
            type = 0;
        return type;
    }

}

