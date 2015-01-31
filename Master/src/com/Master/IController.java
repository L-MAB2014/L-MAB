package com.Master;

/**
 * @author Marcel Reich
 * Intface mit den Methoden welche beim Controller aufgrufen werden k�nnen von seiten des Bots
 */
public interface IController {

    /**
     * F�gt Nachrichten in die Console der Benutzeroberfl�che ein
     *
     * @param text Einzuf�gende Nachricht
     */
    abstract void InputConsole(String text);

    /**
     * Updatet eine Tabellenzeile in der Benutzeroberfläche
     *
     * @param row  Zeile in der Tabelle
     * @param text Einzuf�gende Informationen
     */
    abstract void UpdateTable(int row, String[] text);

    /**
     * Updatet die Positionsanzeige für einen Bot in der Map auf der Benutzeroberfläche
     *
     * @param checkpoint      Neuer Checkpoint des Bots
     * @param last_checkpoint Letzter Checkpoint des Bots
     * @param bot_name        Name des Bots
     */
    abstract void UpdateMap(String checkpoint, String last_checkpoint, String bot_name);

    /**
     * �berproft einen Checkpoint, ob dieser angefahren werden kann oder nciht
     *
     * @param checkpoint Zu Ueberpruefender Checkpunkt
     * @return Ergebnis der Ueberpruefung
     */
    abstract boolean CheckForContinue(String checkpoint, String next_checkpoint, Bot bot);

    /**
     * �berpr�ft ob der Bot in den Ein- oder Ausgang fahren dars
     * @param entrance Ein- oder Ausgang
     * @param bot zu �berpr�fender Bot
     * @return Resultat ob der Bot in den Ein- oder Ausgang fahren darf
     */
    abstract boolean TestEntranceForPuffer(String entrance, Bot bot);
    
    /**
     * �bgeribt den n�chst freien Puffer
     * @return
     * N�chster freier Puffer
     */
    abstract Checkpoint NextFreePuffer();
    
    /**
     * Reserviert f�r einen Bot einen Ausgang
     * @param exit Ausgang
     * @param bot  Bot
     * @return
     * Ob die Reservierung f�r den Ausgang erfolgreich war oder nicht
     */
    abstract boolean ExitReserved(String exit, Bot bot);
    
    /**
     * Reserviert f�r einen Bot einen Eingang
     * @param entrance Eingang
     * @param bot Bot
     * @return
     * Ob die Reservierung f�r den Eingang erfolgreich war oder nicht
     */
    abstract boolean StoreReserved(String entrance, Bot bot);
    
    /**
     * Reserviert f�r einen Bot einen Checkpunkt
     * @param check Checkpunkt
     * @param bot Bot
     * @return Ob die Reservierung f�r den Checkpunkt erfolgreich war oder nicht
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
     * F�gt die Order dem Augnag hinzu soland sie abgeladen wurde
     * @param order Abzuladnene Order
     * @param bot Bot welcher die Order abl�dt
     */
    abstract public  void OrderUnload(Order order, Bot bot);
    
    /**
     * �berpr�ft welche Order als n�chstes abgeholt werden muss
     * @return
     * Abzuholende Order
     */
    abstract public Order NextOrderForBot();
    
    
}
