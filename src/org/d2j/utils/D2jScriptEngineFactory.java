package org.d2j.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 16/02/12
 * Time: 19:15
 * To change this template use File | Settings | File Templates.
 */
public class D2jScriptEngineFactory implements Maker<ScriptEngine> {
    private String scriptEngineName;
    private ScriptEngineManager scriptEngineManager;
    private final Object lock = new Object();

    public D2jScriptEngineFactory(String scriptEngineName) {
        this.scriptEngineName = scriptEngineName;
        this.scriptEngineManager = new ScriptEngineManager();
    }

    public String getScriptEngineName() {
        synchronized (lock){
            return scriptEngineName;
        }
    }

    public void setScriptEngineName(String scriptEngineName) {
        synchronized (lock){
            this.scriptEngineName = scriptEngineName;
        }
    }

    @Override
    public ScriptEngine make() {
        synchronized (lock){
            return scriptEngineManager.getEngineByName(scriptEngineName);
        }
    }
}
