package com.example.sleeperstudent;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DbUserInfo extends RealmObject {
    @PrimaryKey
    private String name;

    private int age;
    private int height;
    private int weight;

    private RealmList<Long> startDate;
    private RealmList<Long> endDate;
    private RealmList<Integer> sleepTime;
    private RealmList<Integer> mood;
    private RealmList<Integer> quality;
    private RealmList<Integer> stress;

    public DbUserInfo(String name) {
        this.name = name;
        /*this.age = age;
        this.height = height;
        this.weight = weight;*/
    }
    public DbUserInfo() {}
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }

    public RealmList<Integer> getMood() {
        return mood;
    }

    public void setStress(RealmList<Integer> stress) {
        this.stress = stress;
    }

    public void setStartDate(RealmList<Long> startDate) {
        this.startDate = startDate;
    }

    public void setSleepTime(RealmList<Integer> sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void setQuality(RealmList<Integer> quality) {
        this.quality = quality;
    }

    public void setMood(RealmList<Integer> mood) {
        this.mood = mood;
    }

    public void setEndDate(RealmList<Long> endDate) {
        this.endDate = endDate;
    }

    public RealmList<Integer> getQuality() {
        return quality;
    }

    public RealmList<Integer> getSleepTime() {
        return sleepTime;
    }

    public RealmList<Integer> getStress() {
        return stress;
    }

    public RealmList<Long> getEndDate() {
        return endDate;
    }

    public RealmList<Long> getStartDate() {
        return startDate;
    }
}
