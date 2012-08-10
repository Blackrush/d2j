package org.d2j.common;

/**
 * User: Blackrush
 * Date: 22/12/11
 * Time: 20:47
 * IDE : IntelliJ IDEA
 */
public class NetworkStringBuilder {
    public static char DELIMITER = (char)0;

    public static String combine(String... packets){
        StringBuilder sb = new StringBuilder(packets.length);
        for (String packet : packets){
            sb.append(packet).append(DELIMITER);
        }
        return sb.toString();
    }

    private StringBuilder sb;

    public NetworkStringBuilder() {
        sb = new StringBuilder();
    }

    public void append(String packet){
        sb.append(packet).append(DELIMITER);
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
