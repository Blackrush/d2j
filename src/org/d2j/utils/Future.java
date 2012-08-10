package org.d2j.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 09/02/12
 * Time: 07:30
 * To change this template use File | Settings | File Templates.
 */
public class Future<T> {
    public static interface Listener<T> {
        void listen(T obj) throws Exception;
    }

    private T obj;
    private Semaphore mutex;
    private List<Listener<T>> listeners;
    private final Object listeners_lock = new Object();

    public Future(T obj) {
        this.obj = obj;
        this.mutex = new Semaphore(1);
        this.listeners = new ArrayList<>();

        try {
            this.mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addListener(Listener<T> listener){
        synchronized (listeners_lock) {
            listeners.add(listener);
        }
    }

    public void addListener(int index, Listener<T> listener){
        synchronized (listeners_lock) {
            listeners.add(index, listener);
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

    public void notifyListeners() throws Exception {
        synchronized (listeners_lock){
            for (Listener<T> listener : listeners){
                listener.listen(obj);
            }
            listeners.clear();
        }
        mutex.release();
    }
}
