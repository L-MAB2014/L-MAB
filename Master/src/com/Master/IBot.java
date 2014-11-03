package com.Master;

public interface IBot {

    /**
     * Bearbeitet die eingehende Nachricht
     *
     * @param message Eingehende Nachricht
     */
    abstract void HandleMessageInput(String message);
}
