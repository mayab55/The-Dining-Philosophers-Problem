package org.example;

import javax.swing.*;
import java.awt.*;

public class Fork extends JPanel {
    private int number;
    private Philosof heldBy;
    private final int originalX;
    private final int originalY;

    public Fork(int number, int x, int y){
        this.number = number;
        this.originalX = x;
        this.originalY = y;

        this.setBounds(x, y, 16, 16);

        this.setOpaque(false);
        this.heldBy = null;
    }

    public String toString(){
        if (this.heldBy == null){
            return "This fork is not held by anyone!";
        }
        return "Fork " + this.number + " is currently held by " + this.heldBy.getName();
    }

    public void setHeldBy(Philosof philosof){
        this.heldBy = philosof;

        if (philosof != null) {
            int x = philosof.getX() + (philosof.getWidth() / 2) - (this.getWidth() / 2);
            int y = philosof.getY() + (philosof.getHeight() / 2) - (this.getHeight() / 2);
            this.setLocation(x, y);
        } else {
            this.setLocation(originalX, originalY);
        }

        repaint();
    }

    public int getNumber(){
        return this.number;
    }

    public Philosof getHeldBy(){
        return this.heldBy;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(heldBy == null){
            g.setColor(Color.DARK_GRAY);
        }
        else{
            g.setColor(Color.ORANGE);
        }
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}