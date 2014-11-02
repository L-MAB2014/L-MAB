package com.Master;

import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * 
 *
 */
public class Checkpoint {
	
	
	/**
	 * Name des Checkpunktes
	 */
	private String name;
	
	/**
	 * Nächster Checkpunkt auf dem normalen Weg
	 */
	private Checkpoint next_WayCheckpoint;
	
	/**
	 * Nächstes mögliches Lager, Parkplatz oder Puffer
	 */
	private Checkpoint next_OtherCheckpoint;
	
	/**
	 * Vorheriger Checkpunkt auf dem normalen Weg
	 */
	private Checkpoint previous_WayCheckpoint;

	/**
	 * Mögliches vorheriges Lager, Parkplatz oder Puffer
	 */
	private Checkpoint previous_OtherCheckpoint;
		
	/**
	 * Gibt an ob der Checkpoint reserviert ist
	 */
	private boolean isReserved;
	
	/**
	 * Gibt an ob der Checkpoint belegt/gesperrt ist
	 */
	private boolean isClosed;
	
	/**
	 * Warteliste für den Checkpoint
	 */
	private List<Bot> waiting_List;
	
	Checkpoint(String name)
	{
		this.name = name;
		
		this.waiting_List = new ArrayList<Bot>();
		
		this.isClosed = false;
		this.isReserved = false;
		
		this.next_WayCheckpoint = null;
		this.next_OtherCheckpoint = null;
		this.previous_WayCheckpoint = null;
		this.previous_OtherCheckpoint = null;
	}
	
	
	/**
	 * Setzt die nächsten Checkpunkte des Checkpunktes
	 * @param next_WayCheckpoint
	 * Nächster Checkpunkt der sich auf dem Weg befindet
	 * @param next_OtherCheckpoint
	 * Nächster Checkpunkt der als Lager,Puffer oder Parkplatz dient
	 * 
	 */
	public void setNextCheckpoints(Checkpoint next_WayCheckpoint, Checkpoint next_OtherCheckpoint)
	{
		this.next_WayCheckpoint = next_WayCheckpoint;
		this.next_OtherCheckpoint = next_OtherCheckpoint;
	}
	
	/**
	 * Setzt die vorherigen Checkpunkte des Checkpunktes
	 * @param previous_WayCheckpoint
	 * Vorheriger Checkpunkt der sich auf dem Weg befindet
	 * @param previous_OtherCheckpoint
	 * Vorheriger Checkpunkt der als Lager,Puffer oder Parkplatz diente
	 */
	public void setPeviousCheckpoints(Checkpoint previous_WayCheckpoint, Checkpoint previous_OtherCheckpoint)
	{
		this.previous_WayCheckpoint = previous_WayCheckpoint;
		this.previous_OtherCheckpoint = previous_OtherCheckpoint;
	}
	
	/**
	 * Gibt an ob der Checkpoint reserviert ist oder nicht!
	 * @return
	 */
	public boolean isReserved() {
		return isReserved;
	}

	/**
	 * Reserviert oder Dereserviert den Checkpunkt
	 * @param isReserved
	 */
	public void setReserved(boolean isReserved) {
		System.out.println(name+" (Reserviert)  : "+ isReserved);
		this.isReserved = isReserved;
	}

	/**
	 * Gibt an ob der Checkpoint gesperrt ist oder nicht!
	 * @return
	 */
	public boolean isClosed() {
		return isClosed;
	}

	/**
	 * Sperrt oder entsperrt den Checkpunkt
	 * @param isClosed
	 */
	public void setClosed(boolean isClosed) {
		System.out.println(name+"  : "+ isClosed);
		this.isClosed = isClosed;
	}

	/**
	 * Gibt den Namen des Checkunktes an
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * Gibt den naechsten Checkpunkt auf dem Weg zurueck
	 * @return naechster Checkpunkt(Weg)
	 */
	public Checkpoint getNext_WayCheckpoint() {
		return next_WayCheckpoint;
	}


	/**
	 * Gibt den naechsten Checkpunkt Parkplatz & Puffer usw. zurueck
	 * @return naechster Checkpunkt(Anderer)
	 */
	public Checkpoint getNext_OtherCheckpoint() {
		return next_OtherCheckpoint;
	}


	/**
	 * Gibt den vorheriger Checkpunkt auf dem Weg zurueck
	 * @return vorheriger Checkpunkt(Weg)
	 */
	public Checkpoint getPrevious_WayCheckpoint() {
		return previous_WayCheckpoint;
	}


	/**
	 * Gibt den vorheriger Checkpunkt Parkplatz & Puffer usw. zurueck
	 * @return vorheriger Checkpunkt(Anderer)
	 */
	public Checkpoint getPrevious_OtherCheckpoint() {
		return previous_OtherCheckpoint;
	}

	/**
	 * Gibt den ersten Bot aus der Warteliste zurück
	 * @return Erster Bot der Warteliste
	 */
	public Bot getFirstOnWaitList()
	{
		return this.waiting_List.remove(0);
	}
	
	
	/**
	 * Setzt einen Bot in die Warteliste
	 * @param bot Zu setzender Bot
	 * @return Ob der Bot in die Warteliste gesetzt wurde oder nciht
	 */
	public boolean setBotOnWaitList(Bot bot)
	{
		if(this.isBotInWaitList() || this.isClosed || this.isReserved())
		{
			this.waiting_List.add(bot);
			return true;
		}
		
		return false;
	}
	

	/**
	 * Überprüft ob sich ein Bot in der Warteliste befindet
	 * @return
	 */
	public boolean isBotInWaitList()
	{
		return this.waiting_List.size() == 0 ? false : true; 
	}
	
}
