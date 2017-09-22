package team.yylight.lightapplication.data;

import io.realm.RealmObject;

/**
 * Created by boxfox on 2017-08-12.
 */

public class UserInfo extends RealmObject {
    private String id, password, birthday, token, deviceId;
    private boolean sex;

    public UserInfo() {
    }

    public UserInfo(String id, String password, String birthday, boolean sex) {
        this.id = id;
        this.password = password;
        this.birthday = birthday;
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex ? "True" : "False";
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = "Token " + token;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
