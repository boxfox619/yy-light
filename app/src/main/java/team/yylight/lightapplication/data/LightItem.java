package team.yylight.lightapplication.data;

import org.json.JSONArray;

/**
 * Created by boxfox on 2017-08-11.
 */

public class LightItem {
    private int number, amount;
    private String title, subscribe, temperature, writer;
    private String imageUrl;
    private double score;
    private JSONArray tags;

    public LightItem(int number, String writer, String title, String subscribe, String imageUrl, double score, int amount, String temperature, JSONArray tags) {
        this.number = number;
        this.title = title;
        this.subscribe = subscribe;
        this.imageUrl = imageUrl;
        this.score = score;
        this.amount = amount;
        this.temperature = temperature;
        this.tags = tags;
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public JSONArray getTags() {
        return tags;
    }

    public void setTags(JSONArray tags) {
        this.tags = tags;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
}
