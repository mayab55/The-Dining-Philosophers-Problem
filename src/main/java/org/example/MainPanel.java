package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainPanel extends JPanel {
    private final int width;
    private final int height;
    private final Waiter waiter;

    // רשימות דינמיות שיכולות לגדול ולקטון
    private final List<Philosof> philosofList = new ArrayList<>();
    private final List<Fork> forkList = new ArrayList<>();

    // שמירת רפרנס ל-HUD כדי שנוכל להחליף אותו דינמית
    private HudStatus hud;

    // הגדרת גבולות לפילוסופים (מינימום 2, מקסימום 7)
    private final int MIN_PHILOSOPHERS = 2;
    private final int MAX_PHILOSOPHERS = 7;

    public MainPanel(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;

        setBounds(x, y, width, height);
        this.setLayout(null);
        this.setBackground(new Color(155, 157, 159));

        this.waiter = new Waiter();

        // נתחיל את המשחק עם 5 פילוסופים כברירת מחדל
        int initialPhilosophers = 5;

        // יצירת המזלגות, הפילוסופים וה-HUD הראשוניים
        rebuildTable(initialPhilosophers);

        // תרד לריענון גרפי ראשוני
        new Thread(() -> {
            Utils.sleep(100);
            repaint();
        }).start();

        // ==========================================
        // 1. כפתור הסרת פילוסוף
        // ==========================================
        JButton deletePhilo = new JButton("Remove one philo");
        deletePhilo.setBounds(this.width - 220, this.height - 110, 200, 80);
        deletePhilo.addActionListener(e -> {
            if (philosofList.size() > MIN_PHILOSOPHERS) {
                int currentSize = philosofList.size();

                // בנייה מחדש של השולחן עם פילוסוף אחד פחות
                rebuildTable(currentSize - 1);
            } else {
                JOptionPane.showMessageDialog(this, "Cannot have less than 2 philosophers!");
            }
        });
        this.add(deletePhilo);

        // ==========================================
        // 2. כפתור הוספת פילוסוף (עובד עכשיו עד 7!)
        // ==========================================
        JButton addPhilo = new JButton("Add one philo");
        addPhilo.setBounds(this.width - 440, this.height - 110, 200, 80);
        addPhilo.addActionListener(e -> {
            if (philosofList.size() < MAX_PHILOSOPHERS) {
                int currentSize = philosofList.size();

                // בנייה מחדש של השולחן עם פילוסוף אחד נוסף
                rebuildTable(currentSize + 1);
            } else {
                JOptionPane.showMessageDialog(this, "Maximum 7 philosophers allowed!");
            }
        });
        this.add(addPhilo);
    }

    /**
     * מתודה שמחשבת מחדש את המיקומים המעגליים של המזלגות והפילוסופים
     * ומסדרת אותם סביב השולחן בצורה דינמית לפי הכמות המבוקשת.
     */
    private void rebuildTable(int count) {
        for (Philosof p : philosofList) {
            p.stopRun();
            this.remove(p);
        }
        for (Fork f : forkList) {
            this.remove(f);
        }
        if (this.hud != null) {
            this.hud.stopHUD();
            this.remove(this.hud);
        }

        philosofList.clear();
        forkList.clear();

        waiter.setMaxEating(count - 1);

        int centerX = width / 2;
        int centerY = height / 2;

        // מרחקים מאוזנים וסימטריים סביב השולחן
        int radiusTable = 125;
        int radiusPhilo = 195;

        String[] names = {"Boris", "Shai", "Alba", "Binyamin", "Dvora", "Emanuel", "Galit"};

        // 2. יצירת המזלגות
        for (int i = 0; i < count; i++) {
            double angle = i * (2 * Math.PI / count);
            int fx = (int) (centerX + radiusTable * Math.cos(angle)) - 8;
            int fy = (int) (centerY + radiusTable * Math.sin(angle)) - 8;

            Fork fork = new Fork(i + 1, fx, fy);
            forkList.add(fork);
            this.add(fork);
        }

        // 3. יצירת הפילוסופים (עם היסט של חצי צעד כדי שיהיו בדיוק בין המזלגות)
        for (int i = 0; i < count; i++) {
            double angle = i * (2 * Math.PI / count) + (Math.PI / count);
            int px = (int) (centerX + radiusPhilo * Math.cos(angle)) - 30;
            int py = (int) (centerY + radiusPhilo * Math.sin(angle)) - 30;

            // הקישור הסימטרי למניעת דדלוק והצגה נכונה
            Fork rightFork = forkList.get(i);
            Fork leftFork = forkList.get((i + 1) % count);

            Philosof philo = new Philosof(names[i], rightFork, leftFork, waiter, px, py);
            philosofList.add(philo);
            this.add(philo);
        }

        this.hud = new HudStatus(philosofList);
        this.hud.setBounds(10, 460, 400, 200);
        this.add(this.hud);

        this.revalidate();
        this.repaint();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0xB88149));
        g.fillOval(width / 2 - 225, height / 2 - 225, 450, 450);
    }
}