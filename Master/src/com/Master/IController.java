package com.Master;

/**
 * @author Marcel Reich
 * Intface mit den Methoden welche beim Controller aufgrufen werden können von seiten des Bots
 */
public interface IController {

    /**
     * Fügt Nachrichten in die Console der Benutzeroberfläche ein
     *
     * @param text Einzufügende Nachricht
     */
    abstract void InputConsole(String text);

    /**
     * Updatet eine Tabellenzeile in der BenutzeroberflÃ¤che
     *
     * @param row  Zeile in der Tabelle
     * @param text Einzufügende Informationen
     */
    abstract void UpdateTable(int row, String[] text);

    /**
     * Updatet die Positionsanzeige fÃ¼r einen Bot in der Map auf der BenutzeroberflÃ¤che
     *
     * @param checkpoint      Neuer Checkpoint des Bots
     * @param last_checkpoint Letzter Checkpoint des Bots
     * @param bot_name        Name des Bots
     */
    abstract void UpdateMap(String checkpoint, String last_checkpoint, String bot_name);

    /**
     * Überproft einen Checkpoint, ob dieser angefahren werden kann oder nciht
     *
     * @param checkpoint Zu Ueberpruefender Checkpunkt
     * @return Ergebnis der Ueberpruefung
     */
    abstract boolean CheckForContinue(String checkpoint, String next_checkpoint, Bot bot);

    /**
     * Überprüft ob der Bot in den Ein- oder Ausgang fahren dars
     * @param entrance Ein- oder Ausgang
     * @param bot zu Überprüfender Bot
     * @return Resultat ob der Bot in den Ein- oder Ausgang fahren darf
     */
    abstract boolean TestEntranceForPuffer(String entrance, Bot bot);
    
    /**
     * Übgeribt den nächst freien Puffer
     * @return
     * Nächster freier Puffer
     */
    abstract Checkpoint NextFreePuffer();
    
    /**
     * Reserviert für einen Bot einen Ausgang
     * @param exit Ausgang
     * @param bot  Bot
     * @return
     * Ob die Reservierung für den Ausgang erfolgreich war oder nicht
     */
    abstract boolean ExitReserved(String exit, Bot bot);
    
    /**
     * Reserviert für einen Bot einen Eingang
     * @param entrance Eingang
     * @param bot Bot
     * @return
     * Ob die Reservierung für den Eingang erfolgreich war oder nicht
     */
    abstract boolean StoreReserved(String entrance, Bot bot);
    
    /**
     * Reserviert für einen Bot einen Checkpunkt
     * @param check Checkpunkt
     * @param bot Bot
     * @return Ob die Reservierung für den Checkpunkt erfolgreich war oder nicht
     */
    abstract boolean CheckpointReserved(String check, Bot bot);
    
    /**
     * Setzt einen Bot in die Warteschlange eines Checkpunktes
     * @param check Checkpounkt
     * @param bot	Bot
     */
    abstract void SetOnWaitList(String check, Bot bot);
    
    /**
     * Enfternt die Order einem Eingang, sobald sie ausgeladen wurde 
     * @param order zu enfernene Order
     */
    abstract public void OrderLoad(Order order);
    
    /**
     * Fügt die Order dem Augnag hinzu soland sie abgeladen wurde
     * @param order Abzuladnene Order
     * @param bot Bot welcher die Order ablädt
     */
    abstract public  void OrderUnload(Order order, Bot bot);
    
    /**
     * Überprüft welche Order als nächstes abgeholt werden muss
     * @return
     * Abzuholende Order
     */
    abstract public Order NextOrderForBot();
    
    
}
