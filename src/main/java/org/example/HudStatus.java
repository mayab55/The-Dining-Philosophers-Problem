package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HudStatus extends JPanel {
    private final List<JLabel> labels;
    private final List<Philosof> philosofList;
    private boolean isRunning; // משתנה לשליטה על מחזור החיים של ה-Thread

    public HudStatus(List<Philosof> philosofList) {
        this.philosofList = philosofList;
        this.labels = new ArrayList<>();
        this.isRunning = true;

        // מספר שורות דינמי, ועמודה אחת (1) קבועה כדי שהם יהיו אחד מתחת לשני!
        this.setLayout(new GridLayout(philosofList.size(), 1));
        this.setOpaque(false);

        for (Philosof p : philosofList) {
            JLabel label = new JLabel();
            // הגדרת פונט בגודל 14 (יותר קטן מה-18 הקודם)
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setForeground(Color.BLACK);
            this.add(label);
            labels.add(label);
        }

        // הפעלת תרד העדכון
        new Thread(() -> {
            while (this.isRunning) {
                // לולאה מוגנת שמוודאת שלא חורגים מאף אחת מהרשימות
                int size = Math.min(philosofList.size(), labels.size());

                for (int i = 0; i < size; i++) {
                    Philosof p = philosofList.get(i);
                    String status = switch (p.getStatus()) {
                        case Philosof.THINKING -> "Thinking";
                        case Philosof.WAITING_FOR_FORK_1 -> "Waiting fork 1";
                        case Philosof.WAITING_FOR_FORK_2 -> "Waiting fork 2";
                        case Philosof.EATING -> "Eating";
                        case Philosof.DEAD -> "Murdered";
                        default -> "Unknown";
                    };

                    // עדכון טקסט בטוח
                    labels.get(i).setText(p.getName() + ": " + status + " | Count: " + p.getEatingCount());
                }
                Utils.sleep(400);
            }
        }).start();
    }

    /**
     * פונקציה שתקראי לה מ-MainPanel לפני שאת מסירה את ה-HUD הישן
     * כדי לעצור את ה-Thread שלו בצורה נקייה מהזיכרון.
     */
    public void stopHUD() {
        this.isRunning = false;
    }
}