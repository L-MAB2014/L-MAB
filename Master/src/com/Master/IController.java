package com.Master;

public interface IController {

	/**
	 * Fuegt Nachrichten in die Console der Benutzeroberflaeche ein
	 * @param text Einzufuegende Nachricht
	 */
	abstract void InputConsole( String text);
	
	/**
	 * Updatet eine Tabellenzeile in der Benutzeroberflaeche
	 * @param row Zeile in der Tabelle
	 * @param text Einzufuegende Informationen
	 */
	abstract void UpdateTable( int row, String [] text);
	
	/**
	 * Updatet die Positionsanyeige fuer einen Bot in der Map auf der Benutzeroberflaeche
	 * @param checkpoint Neuer Checkpoint des Bots
	 * @param last_checkpoint Letzter Checkpoint des Bots
	 * @param bot_name Name des Bots
	 */
	abstract void UpdateMap(String checkpoint, String last_checkpoint, String bot_name);
	
	/**
	 * Ueberprueft einen Checkpoint, ob dieser angefahren werden kann oder nciht
	 * @param checkpoint Zu Ueberpruefender Checkpunkt
	 * @return Ergebnis der Ueberpruefung
	 */
	abstract boolean CheckForConintue(String checkpoint, String next_checkpoint, Bot bot);
	
	
	
	abstract boolean IsNextPufferFree(String puffer);
	
	abstract boolean TestEntranceForPuffer(String entrance);
	
	abstract boolean EntranceReserved(String entrance);
	
	abstract void  ToPufferAndSetOnWaitList(String check, String entrance);
	
}
