package com.Master;
import java.util.*;


/**
 * Die Klasse "Protokoll" erstellt oder entziffert  Nachrichten 
 * zum Nachrichtenaustausch mit dem Bot(Roboter) 
 * 
 * Das Protokoll sieht wie folgt aus:
 * 
 * Schüssel : Wert ; SChlüssel : Wert ; ..... #
 * 
 * Eine Nachricht wird aufgeteilt in einen Schlüssel und einen Wert, welche
 * durch einen Doppelpunkt getrennt werden, soll eine weitere ANchricht angefügt 
 * werden geschiet dies über einen Semikolon. Am Ende einer kompletten Nachricht 
 * wird eine Raute (#) positionert, die das Ende wiederspiegelt. 
 */
public class Protokoll {
	
	
	/**
	 * Erstellt anhand eines Order-Objektes eine nachdem Protokoll vorgeschriebene Nachricht 
	 * @param order Order-Objekt 
	 * @return Protokoll gerechte Nachricht 
	 */
	public static String OrderToString(Order order)
	{				
		return String.format("ID:%s;S:%s;E:%s#", order.getId(), order.getStore_place(), order.getExit_place());
	}
	

	/**
	 * Erstellt anhand einer Liste von Message-Objekten eine nachdem Protokoll vorgeschriebene Nachricht
	 * @param message Message-Objekten
	 * @return Protokoll gerechte Nachricht 
	 */
	public static String MessageToString(List <Message> message)
	{
		String m = "";
		
		for(int i =0 ; i < message.size(); ++i)
		{
			m += message.get(i).getKey()+":"+message.get(i).getValue();
			
			if((i+1) < message.size())
				m+=";";
		}
		m +="#";
		
		return m;				
	}
	
	public static String MessageToString(Message message)
	{

		return (message.getKey()+":"+message.getValue()+"#");
	}
	
	/**
	 * Wandelt einen eingegangene Message in einzelne Message-Objekte um und speichert diese in eine Liste
	 * @param message Eingehende Nachricht (nachdem Protokolformat  )
	 * @return Aufgeschlüsselte Nachricht in Message Objekten
	 */
	public static List<Message> StringToMessage(String message)
	{
		List <Message> l = new ArrayList<Message>();
		String[] splitMessage = message.split(";");
		
		for (int i=0; i< splitMessage.length; ++i)
		{
			String[] sm = splitMessage[i].split(":");
			l.add( new Message(sm[0],sm[1]));
		}
		
		return l;
	}				
}
