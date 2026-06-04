package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainPanel extends JPanel {
    private final int width;
    private final int height;
    private final Waiter waiter;

    private final List<Philosof> philosofList = new ArrayList<>();
    private final List<Fork> forkList = new ArrayList<>();

    private HudStatus hud;

    private final int MIN_PHILOSOPHERS = 2;
    private final int MAX_PHILOSOPHERS = 7;

    public MainPanel(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;

        setBounds(x, y, width, height);
        this.setLayout(null);
        this.setBackground(new Color(155, 157, 159));

        this.waiter = new Waiter();

        int initialPhilosophers = 5;

        rebuildTable(initialPhilosophers);

        new Thread(() -> {
            Utils.sleep(100);
            repaint();
        }).start();

        JButton deletePhilo = new JButton("Remove one philo");
        deletePhilo.setBounds(this.width - 220, this.height - 110, 200, 80);
        deletePhilo.addActionListener(e -> {
            if (philosofList.size() > MIN_PHILOSOPHERS) {
                int currentSize = philosofList.size();
                rebuildTable(currentSize - 1);
            } else {
                JOptionPane.showMessageDialog(this, "Cannot have less than 2 philosophers!");
            }
        });
        this.add(deletePhilo);

        JButton addPhilo = new JButton("Add one philo");
        addPhilo.setBounds(this.width - 440, this.height - 110, 200, 80);
        addPhilo.addActionListener(e -> {
            if (philosofList.size() < MAX_PHILOSOPHERS) {
                int currentSize = philosofList.size();
                rebuildTable(currentSize + 1);
            } else {
                JOptionPane.showMessageDialog(this, "Maximum 7 philosophers allowed!");
            }
        });
        this.add(addPhilo);
    }

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

        int radiusTable = 125;
        int radiusPhilo = 195;

        String[] names = {"Boris", "Shai", "Alba", "Binyamin", "Dvora", "Emanuel", "Galit"};

        for (int i = 0; i < count; i++) {
            double angle = i * (2 * Math.PI / count);
            int fx = (int) (centerX + radiusTable * Math.cos(angle)) - 8;
            int fy = (int) (centerY + radiusTable * Math.sin(angle)) - 8;

            Fork fork = new Fork(i + 1, fx, fy);
            forkList.add(fork);
            this.add(fork);
        }

        for (int i = 0; i < count; i++) {
            double angle = i * (2 * Math.PI / count) + (Math.PI / count);
            int px = (int) (centerX + radiusPhilo * Math.cos(angle)) - 30;
            int py = (int) (centerY + radiusPhilo * Math.sin(angle)) - 30;

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