package com.marcel.Communication;

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
     * N�chster Checkpunkt auf dem normalen Weg
     */
    private Checkpoint next_WayCheckpoint;

    /**
     * N�chstes m�gliches Lager, Parkplatz oder Puffer
     */
    private Checkpoint next_OtherCheckpoint;

    /**
     * Vorheriger Checkpunkt auf dem normalen Weg
     */
    private Checkpoint previous_WayCheckpoint;

    /**
     * M�gliches vorheriges Lager, Parkplatz oder Puffer
     */
    private Checkpoint previous_OtherCheckpoint;

    /**
     * Enth�lkt die FDarbe des Checkpoints
     */
    private int color;


    Checkpoint(String name, int color) {
        this.name = name;
        this.color = color;

        this.next_WayCheckpoint = null;
        this.next_OtherCheckpoint = null;
        this.previous_WayCheckpoint = null;
        this.previous_OtherCheckpoint = null;
    }


    /**
     * Setzt die n�chsten Checkpunkte des Checkpunktes
     *
     * @param next_WayCheckpoint   N�chster Checkpunkt der sich auf dem Weg befindet
     * @param next_OtherCheckpoint N�chster Checkpunkt der als Lager,Puffer oder Parkplatz dient
     */
    public void setNextCheckpoints(Checkpoint next_WayCheckpoint, Checkpoint next_OtherCheckpoint) {
        this.next_WayCheckpoint = next_WayCheckpoint;
        this.next_OtherCheckpoint = next_OtherCheckpoint;
    }

    /**
     * Setzt die vorherigen Checkpunkte des Checkpunktes
     *
     * @param previous_WayCheckpoint   Vorheriger Checkpunkt der sich auf dem Weg befindet
     * @param previous_OtherCheckpoint Vorheriger Checkpunkt der als Lager,Puffer oder Parkplatz diente
     */
    public void setPeviousCheckpoints(Checkpoint previous_WayCheckpoint, Checkpoint previous_OtherCheckpoint) {
        this.previous_WayCheckpoint = previous_WayCheckpoint;
        this.previous_OtherCheckpoint = previous_OtherCheckpoint;
    }


    public int getColor() {
        return color;
    }


    /**
     * Gibt den Namen des Checkunktes an
     *
     * @return
     */
    public String getName() {
        return name;
    }


    /**
     * Gibt den naechsten Checkpunkt auf dem Weg zurueck
     *
     * @return naechster Checkpunkt(Weg)
     */
    public Checkpoint getNext_WayCheckpoint() {
        return next_WayCheckpoint;
    }


    /**
     * Gibt den naechsten Checkpunkt Parkplatz & Puffer usw. zurueck
     *
     * @return naechster Checkpunkt(Anderer)
     */
    public Checkpoint getNext_OtherCheckpoint() {
        return next_OtherCheckpoint;
    }


    /**
     * Gibt den vorheriger Checkpunkt auf dem Weg zurueck
     *
     * @return vorheriger Checkpunkt(Weg)
     */
    public Checkpoint getPrevious_WayCheckpoint() {
        return previous_WayCheckpoint;
    }


    /**
     * Gibt den vorheriger Checkpunkt Parkplatz & Puffer usw. zurueck
     *
     * @return vorheriger Checkpunkt(Anderer)
     */
    public Checkpoint getPrevious_OtherCheckpoint() {
        return previous_OtherCheckpoint;
    }

}
