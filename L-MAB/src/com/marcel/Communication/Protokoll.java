package com.marcel.Communication;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse Protokoll erstellt oder entziffert Nachrichten
 * zum Nachrichtenaustausch mit dem Bot(Roboter)
 * 
 * Das Protokoll sieht wie folgt aus:
 * 
 * Schlüssel : Wert; Schlüssel : Wert; ..... (#
 * 
 * Eine Nachricht wird aufgeteilt in einen Schlüssel und einen Wert, welche
 * durch einen Doppelpunkt getrennt werden, soll eine weitere Nachricht angefangen,
 *  geschiet dies  durch ein Semikolon. Am Ende einer kompletten Nachricht
 * wird eine Raute (#) positionert, die das Ende wiederspiegelt.
 */
public class Protokoll {

    /**
   * Erstellt anhand einer Liste von Message-Objekten eine nachdem Protokoll vorgeschriebene Nachricht
     *
     * @param message Message-Objekten
     * @return Protokoll gerechte Nachricht
     */
    public static String MessageToString(List<Message> message) {
        String m = "";

        for (int i = 0; i < message.size(); ++i) {
            m += message.get(i).getKey() + ":" + message.get(i).getValue();

            if ((i + 1) < message.size())
                m += ";";
        }
        m += "#";
        return m;
    }

    /**
     * * Erstellt anhand eines Message-Objektes eine nachdem Protokoll vorgeschriebene Nachricht
     *
     * @param message Message-Objekt
     * @return Protokoll gerechte Nachricht
     */
    public static String MessageToString(Message message) {
        return (message.getKey() + ":" + message.getValue() + "#");
    }

    /**
     * Wandelt einen eingegangene Message in einzelne Message-Objekte um und speichert diese in eine Liste
     *
     * @param message Eingehende Nachricht (nachdem Protokolformat  )
     * @return Aufgeschlüsselte Nachricht in Message Objekten
     */
    public static List<Message> StringToMessage(String message) {
        List<Message> l = new ArrayList<Message>();

        char[] split = message.toCharArray();

        int i = 0;
        while (i < split.length) {
            String value = "";
            String key = "";

            while ((i < split.length) && (split[i] != ':')) {
                key += split[i];
                ++i;
            }
            ++i;
            while ((i < split.length) && (split[i] != ';')) {
                value += split[i];
                ++i;
            }
            ++i;

            if (value != "" && key != "")
                l.add(new Message(key, value));
        }
        return l;
    }


}
