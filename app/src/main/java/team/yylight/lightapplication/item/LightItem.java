package team.yylight.lightapplication.item;

/**
 * Created by boxfox on 2017-08-11.
 */

public class LightItem {
    private int number;
    private String title, subscribe;
    private String imageUrl;
    private double score;

    public LightItem(int number, String title, String subscribe, String imageUrl, double score) {
        this.number = number;
        this.title = title;
        this.subscribe = subscribe;
        this.imageUrl = imageUrl;
        this.score = score;
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
}
