package org.d2j.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 02/03/12
 * Time: 11:08
 */
public class Future0 {
    public static interface Listener {
        void listen();
    }

    private Semaphore mutex;
    private List<Listener> listeners;
    private final Object listeners_lock = new Object();

    public Future0() {
        this.mutex = new Semaphore(1);
        this.listeners = new ArrayList<>();

        try {
            this.mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addListener(Listener listener){
        synchronized (listeners_lock) {
            listeners.add(listener);
        }
    }

    public void await() {
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mutex.release();
    }

    public void notifyListeners() {
        synchronized (listeners_lock){
            for (Listener listener : listeners){
                listener.listen();
            }
            listeners.clear();
        }
        mutex.release();
    }
}
