package com.example.api;

import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class Data {
    String login;
    String avatar;
    public Data(JSONObject jsonObject) {
        try {
            if (jsonObject.has("login")) {
                login = jsonObject.getString("login");
            }if (jsonObject.has("avatar_url")) {
                avatar = jsonObject.getString("avatar_url");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
