package org.example;

import javax.swing.*;
import java.util.List;

public class AddPhiloButton extends JButton {

    public AddPhiloButton(MainPanel mainPanel, List<Philosof> philosofList, List<Fork> forkList, Waiter waiter) {
        super("Add one philo");

        this.setBounds(mainPanel.getWidth() - 420, mainPanel.getHeight() - 110, 200, 80);

        this.addActionListener(e -> {
            long activeCount = philosofList.stream().filter(p -> p.getStatus() != Philosof.DEAD).count();

            if (activeCount < 5) {
                for (Philosof p : philosofList) {
                    if (p.getStatus() == Philosof.DEAD) {

                        waiter.plusMaxEating();

                        p.revive();

                        mainPanel.add(p);

                        mainPanel.repaint();
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Maximum 5 philosophers allowed in this setup!");
            }
        });
    }
}