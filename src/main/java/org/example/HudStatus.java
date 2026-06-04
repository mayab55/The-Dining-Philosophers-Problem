package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HudStatus extends JPanel {
    private final List<JLabel> labels;
    private final List<Philosof> philosofList;
    private boolean isRunning;

    public HudStatus(List<Philosof> philosofList) {
        this.philosofList = philosofList;
        this.labels = new ArrayList<>();
        this.isRunning = true;

        this.setLayout(new GridLayout(philosofList.size(), 1));
        this.setOpaque(false);

        for (Philosof p : philosofList) {
            JLabel label = new JLabel();
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setForeground(Color.BLACK);
            this.add(label);
            labels.add(label);
        }

        new Thread(() -> {
            while (this.isRunning) {
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

                    labels.get(i).setText(p.getName() + ": " + status + " | Count: " + p.getEatingCount());
                }
                Utils.sleep(400);
            }
        }).start();
    }

    public void stopHUD() {
        this.isRunning = false;
    }
}