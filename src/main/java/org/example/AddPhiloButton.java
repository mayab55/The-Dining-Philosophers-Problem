package org.example;

import javax.swing.*;
import java.util.List;

public class AddPhiloButton extends JButton {

    public AddPhiloButton(MainPanel mainPanel, List<Philosof> philosofList, List<Fork> forkList, Waiter waiter) {
        super("Add one philo");

        // הגדרת מיקום (למשל, ליד הכפתור של המחיקה)
        this.setBounds(mainPanel.getWidth() - 420, mainPanel.getHeight() - 110, 200, 80);

        this.addActionListener(e -> {
            // נבדוק כמה פילוסופים פעילים יש כרגע
            long activeCount = philosofList.stream().filter(p -> p.getStatus() != Philosof.DEAD).count();

            // הגבלה: מאפשרים להוסיף חזרה רק אם ירדנו מתחת ל-5 פילוסופים
            if (activeCount < 5) {
                // נמצא את הפילוסוף הראשון שכרגע "מת" (הוסר) ונחזיר אותו לחיים
                for (Philosof p : philosofList) {
                    if (p.getStatus() == Philosof.DEAD) {
                        // מעדכנים את ה-Waiter שמקסימום האוכלים בו-זמנית עולה חזרה
                        waiter.plusMaxEating();

                        // יוצרים ומפעילים תרד (Thread) חדש עבור הפילוסוף, כי התרד הקודם שלו מת
                        p.revive();

                        // מוסיפים אותו חזרה ויזואלית לפאנל
                        mainPanel.add(p);

                        // מרעננים את המסך
                        mainPanel.repaint();
                        break; // מצאנו והחזרנו אחד, יוצאים מהלולאה
                    }
                }
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Maximum 5 philosophers allowed in this setup!");
            }
        });
    }
}