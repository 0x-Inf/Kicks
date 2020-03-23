package com.diablo.jayson.kicksv1.Models;

import java.io.Serializable;

public class Host implements Serializable {

    private String userName;
    private String uid;
    private String hostPic;

    public Host(String userName, String uid, String hostPic) {
        this.userName = userName;
        this.uid = uid;
        this.hostPic = hostPic;
    }

    public Host() {
    }

    public String getHostPic() {
        return hostPic;
    }

    public void setHostPic(String hostPic) {
        this.hostPic = hostPic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
