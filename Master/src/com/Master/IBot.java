package com.Master;

/**
 * @author Marcel Reich
 * Interface um die Nachrichten aus dem Inpput Stream dem Bot zu übermitteln
 *
 */
public interface IBot {

    /**
     * Bearbeitet die eingehende Nachricht
     *
     * @param message Eingehende Nachricht
     */
    abstract void HandleMessageInput(String message);
}
