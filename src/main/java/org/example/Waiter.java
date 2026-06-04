package org.example;

import java.util.ArrayList;
import java.util.List;

public class Waiter {
    private final List<Philosof> allowedList = new ArrayList<>();
    private int maxEating;

    public Waiter(){
        this.maxEating = 4;
    }

    public synchronized boolean requestPermission(Philosof philosof){
        if (allowedList.size() < maxEating){
            allowedList.add(philosof);
            return true;
        }
        return false;
    }

    public synchronized void setMaxEating(int maxEating) {
        this.maxEating = maxEating;
        this.allowedList.clear();
    }

    public void plusMaxEating() {
        this.maxEating++;
    }

    public synchronized void doneEating(Philosof philosof){
        allowedList.remove(philosof);
    }

    public void minusMaxEating(){
        this.maxEating--;
    }

}