package org.d2j.game.game;

import org.d2j.common.CollectionUtils;
import org.d2j.common.StringUtils;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.game.events.SystemMessageEvent;
import org.d2j.game.service.IWorld;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Blackrush
 * Date: 24/12/11
 * Time: 19:13
 * IDE : IntelliJ IDEA
 */
public class PubSystem implements ActionListener {
    private static final String PUB_MESSAGE_FORMAT = "<font color=\"#{0}\"><b>(Pub)</b> {1}</font>";

    private final IGameConfiguration configuration;
    private final IWorld world;
    private Timer timer;
    private List<String> messages;
    private String messageColor;
    private int currentMessage;

    public PubSystem(IGameConfiguration configuration, IWorld world) {
        this.configuration = configuration;
        this.world = world;
        this.timer = new Timer(configuration.getPubInterval() * 60 * 1000, this);
        this.messages = CollectionUtils.toList(configuration.getPubMessages());
        this.messageColor = this.configuration.getPubColor();
        this.currentMessage = 0;
    }

    public void start(){
        timer.start();
    }

    public void stop(){
        timer.stop();
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessage(int index, String message){
        if (index < 0) {
        }
        else if (index >= messages.size()){
            messages.add(message);
        }
        else{
            messages.set(index, message);
        }
    }

    public void setMessageColor(String messageColor){
        this.messageColor = messageColor;
    }

    public void setInterval(int minutes){
        timer.stop();
        timer.setDelay(minutes * 60000);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = StringUtils.format(
                PUB_MESSAGE_FORMAT,
                messageColor,
                messages.get(currentMessage)
        );

        world.systemMessage(new SystemMessageEvent(message));

        ++currentMessage;
        if (currentMessage >= configuration.getPubMessages().length){
            currentMessage = 0;
        }
    }

    public void force(int id){
        if (id < 0 || id >= messages.size()) return;

        String message = StringUtils.format(
                PUB_MESSAGE_FORMAT,
                messageColor,
                messages.get(id)
        );

        world.systemMessage(new SystemMessageEvent(message));
    }
}
