package com.Master;

public interface IController {

    /**
     * Fügt Nachrichten in die Console der Benutzeroberfläche ein
     *
     * @param text Einzufügende Nachricht
     */
    abstract void InputConsole(String text);

    /**
     * Updatet eine Tabellenzeile in der Benutzeroberfläche
     *
     * @param row  Zeile in der Tabelle
     * @param text Einzufügende Informationen
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
     * Überprüft einen Checkpoint, ob dieser angefahren werden kann oder nciht
     *
     * @param checkpoint Zu Ueberpruefender Checkpunkt
     * @return Ergebnis der Ueberpruefung
     */
    abstract boolean CheckForContinue(String checkpoint, String next_checkpoint, Bot bot);

    abstract boolean TestEntranceForPuffer(String entrance, Bot bot);
    abstract Checkpoint NextFreePuffer();
    abstract boolean ExitReserved(String exit, Bot bot);
    abstract boolean StoreReserved(String entrance, Bot bot);
    abstract boolean CheckpointReserved(String check, Bot bot);
    
    abstract void SetOnWaitList(String check, Bot bot);
    
    abstract public void OrderLoad(Order order);
    abstract public  void OrderUnload(Order order, Bot bot);
    
    abstract public Order NextOrderForBot();
    
    
}
