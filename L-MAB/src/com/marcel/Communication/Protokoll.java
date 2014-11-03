package com.marcel.Communication;

import java.util.ArrayList;
import java.util.List;

public class Protokoll {

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

    public static String MessageToString(Message message) {
        return (message.getKey() + ":" + message.getValue() + "#");
    }

    public static List<Message> StringToMessage(String message) {
        List<Message> l = new ArrayList<Message>();
//		String[] splitMessage = message.split(";");
//		
//		for (int i=0; i< splitMessage.length; ++i)
//		{
//			String[] sm = splitMessage[i].split(":");
//			l.add( new Message(sm[0],sm[1]));
//		}

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
