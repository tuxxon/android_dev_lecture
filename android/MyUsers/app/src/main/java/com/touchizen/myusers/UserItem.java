package com.touchizen.myusers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class UserItem implements Serializable {

    private int userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userDesc;
    private int views;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public JSONObject toJSON() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", userId);
            jo.put("username", userName);
            jo.put("useremail", userEmail);
            jo.put("userphone", userPhone);
            jo.put("userdesc", userDesc);
            jo.put("views", views);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jo;
    }

}
