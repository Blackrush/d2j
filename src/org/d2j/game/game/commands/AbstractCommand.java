package org.d2j.game.game.commands;

import org.d2j.common.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 09/01/12
 * Time: 20:07
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCommand implements ICommand {
    public abstract String helpMessage();
    public abstract String[] usageParameters();

    public final String usage(){
        StringBuilder sb = new StringBuilder();
        sb.append("<font color=\"#FF0000\"><b>").append(name()).append("</b></font><font color=\"#0015FF\">");
        for (String arg : usageParameters()){
            sb.append(" ${<i>").append(arg).append("</i>}");
        }
        sb.append("</font>");
        return sb.toString();
    }

    @Override
    public final String help() {
        return StringUtils.format(
                "{1} Usage: {2}",
                name(),
                helpMessage(),
                usage()
        );
    }
}
