package org.d2j.utils;

import org.slf4j.Logger;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 02/03/12
 * Time: 11:04
 */
public class ThreadUtils {
    public static Future0 whileFailure(final Action0 action, final long retryDelay, final Logger log){
        final Future0 future = new Future0();

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = false;
                while (!success) {
                    try {
                        action.action();
                        success = true;
                    } catch (Exception ignored) {
                        log.error("Fail, trying in {} seconds.", retryDelay / 1000);
                        try {
                            Thread.sleep(retryDelay);
                        } catch (InterruptedException ignored1) {}
                    }
                }
                future.notifyListeners();
            }
        }).run();

        return future;
    }
}
