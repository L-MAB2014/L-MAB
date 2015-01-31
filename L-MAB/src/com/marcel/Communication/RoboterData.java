package com.marcel.Communication;

/**
 * @author Marcel Reich
 * 
 * keys für die Kommuniaktion mit dem Leitstand
 *
 */
public class RoboterData {

    /**
     * Code für einen Checkpunkt
     */
    public static final String code_Checkpoint = "CP";

    /**
     * Code für einen nächsten Checkpunkt
     */
    public static final String code_NextCheckpoint = "NC";

    /**
     * Code zum Updaten einer Order für einen Bot
     */
    public static final String code_UpdateOrder = "UO";

    /**
     * Code für das Signal, das ein Bot bereit zum Aufladen ist
     */
    public static final String code_ReadyLoad = "RL";

    /**
     * Code fÃ¼r das Signal, das ein Bot bereit zum Abladen ist
     */
    public static final String code_ReadyUnload = "RU";

    /**
     * Code für ein Stopp/Signal
     */
    public static final String code_STOP = "ST";
    
    /**
     * Code für die Identifierzierung eines STOP-Signal
     */
    public static final String STOP_CODE = "X123x";

    /**
     * Code für einen Store(Lager)
     */
    public static final String code_Store = "S";

    /**
     * Code für ein Exit(Ausgang)
     */
    public static final String code_Exit = "E";

    /**
     * Code für eine OrderID
     */
    public static final String code_OrderID = "ID";

    /**
     * Code für die Position zum laden
     */
    public static final String code_PostionLoad = "PL";
    
    /**
     * Code für die Position zum abladen
     */
    public static final String code_PostionUnload = "PU";
    
    /**
     *  Code für einen Puffer
     */
    public static final String code_Puffer = "PF";
    
    /**
     * Code für die Meldung das aufgeladen wurde
     */
    public static final String code_FinishLoad = "FL";
    
    /**
     * Code für die Meldung das abgeladen wurde
     */
    public static final String code_FinishUnload = "FU";
    
    /**
     * Code für ein OK-Signal
     */
    public static final String code_Continue = "OK";
    
    /**
     * Code fürs Laden eines Pakets
     */
    public static final String code_Load = "L";
    
    /**
     * Code für die Zuweisung eines Parkplatzes
     */
    public static final String code_ParkPosition = "PP";
    
    /**
     * Code für die Meldung das man Parken muss
     */
    public static final String code_ToPark = "P";
    
    /**
     * Code für die Zuweisung eines Puffers
     */
    public static final String code_ToPuffer = "TP";
    
    /**
     * Code für die Meldung zur Überprüfung ob man zu einem Ein- oder Ausgang fahren darf
     */
    public static final String code_TestTarget = "TT";
    
    /**
     * Code für eine Reservierung
     */
    public static final String code_Reserved = "R";
    
}
