package org.d2j.utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Blackrush
 * Date: 24/12/11
 * Time: 11:30
 * IDE : IntelliJ IDEA
 */
public abstract class PeriodicAction {
    private Timer timer;

    protected PeriodicAction(int delay) {
        timer = new Timer(delay * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action();
            }
        });
    }

    public void start(){
        timer.start();
    }

    public void stop(){
        timer.stop();
    }

    protected abstract void action();
}
