package com.marcel.Communication;

/**
 * @author Marcel Reich
 * Interface zum weiterreichen der eingehenden Nachrichten
 */
public interface IRoboter {

    /**
     * �bergibt dem Robtoer die Empfangene Nachricht
     * @param text Empfangener text
     */
    abstract void InputMessage(String text);
}
