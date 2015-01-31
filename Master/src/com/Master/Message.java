package com.Master;

/**
 * Das Message/Objekt besteht aus einem Schl�ssel und einem Wert
 */
public class Message {

    /**
     * Schl�ssel der Message
     */
    private String key;

    /**
     * Wert der Message
     */
    private String value;

    Message(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Gibt den Schl�ssel der Message zur�ck
     *
     * @return Schlüssel der Message
     */
    public String getKey() {
        return key;
    }

    /**
     * Setzt den Wert der Message
     *
     * @param key zu setzender Wert
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Gibt den Wert der Message zurück
     *
     * @return Wert der Message
     */
    public String getValue() {
        return value;
    }

    /**
     * Setzt den Wert der Message
     *
     * @param value zu setzender Wert
     */
    public void setValue(String value) {
        this.value = value;
    }

}
