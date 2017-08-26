package team.yylight.lightapplication.data;

import io.realm.RealmObject;

/**
 * Created by boxfox on 2017-08-26.
 */

public class LightSetting extends RealmObject {
    private int amount, tempeature;
    private int time;

    public LightSetting(){}
    public LightSetting(int amount, int tempeature, int time){
        this.amount = amount;
        this.tempeature = tempeature;
        this.time = time;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTempeature() {
        return tempeature;
    }

    public void setTempeature(int tempeature) {
        this.tempeature = tempeature;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
