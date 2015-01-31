package com.marcel.Communication;

/**
 * @author Marcel Reich
 * 
 * keys f�r die Kommuniaktion mit dem Leitstand
 *
 */
public class RoboterData {

    /**
     * Code f�r einen Checkpunkt
     */
    public static final String code_Checkpoint = "CP";

    /**
     * Code f�r einen n�chsten Checkpunkt
     */
    public static final String code_NextCheckpoint = "NC";

    /**
     * Code zum Updaten einer Order f�r einen Bot
     */
    public static final String code_UpdateOrder = "UO";

    /**
     * Code f�r das Signal, das ein Bot bereit zum Aufladen ist
     */
    public static final String code_ReadyLoad = "RL";

    /**
     * Code für das Signal, das ein Bot bereit zum Abladen ist
     */
    public static final String code_ReadyUnload = "RU";

    /**
     * Code f�r ein Stopp/Signal
     */
    public static final String code_STOP = "ST";
    
    /**
     * Code f�r die Identifierzierung eines STOP-Signal
     */
    public static final String STOP_CODE = "X123x";

    /**
     * Code f�r einen Store(Lager)
     */
    public static final String code_Store = "S";

    /**
     * Code f�r ein Exit(Ausgang)
     */
    public static final String code_Exit = "E";

    /**
     * Code f�r eine OrderID
     */
    public static final String code_OrderID = "ID";

    /**
     * Code f�r die Position zum laden
     */
    public static final String code_PostionLoad = "PL";
    
    /**
     * Code f�r die Position zum abladen
     */
    public static final String code_PostionUnload = "PU";
    
    /**
     *  Code f�r einen Puffer
     */
    public static final String code_Puffer = "PF";
    
    /**
     * Code f�r die Meldung das aufgeladen wurde
     */
    public static final String code_FinishLoad = "FL";
    
    /**
     * Code f�r die Meldung das abgeladen wurde
     */
    public static final String code_FinishUnload = "FU";
    
    /**
     * Code f�r ein OK-Signal
     */
    public static final String code_Continue = "OK";
    
    /**
     * Code f�rs Laden eines Pakets
     */
    public static final String code_Load = "L";
    
    /**
     * Code f�r die Zuweisung eines Parkplatzes
     */
    public static final String code_ParkPosition = "PP";
    
    /**
     * Code f�r die Meldung das man Parken muss
     */
    public static final String code_ToPark = "P";
    
    /**
     * Code f�r die Zuweisung eines Puffers
     */
    public static final String code_ToPuffer = "TP";
    
    /**
     * Code f�r die Meldung zur �berpr�fung ob man zu einem Ein- oder Ausgang fahren darf
     */
    public static final String code_TestTarget = "TT";
    
    /**
     * Code f�r eine Reservierung
     */
    public static final String code_Reserved = "R";
    
}
