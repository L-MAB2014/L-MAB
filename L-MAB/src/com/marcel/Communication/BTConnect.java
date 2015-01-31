package com.marcel.Communication;

import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Marcel Reich
 * 
 * Bluetooth-Verbindung
 *
 */
public class BTConnect {

    /**
     * Zeit bis eine Verbindung getrennt wird
     */
    private final int disconnect_time = 5000;


    /**
     * Verbindungs-Objekt
     */
    private BTConnection btc;
    
    /**
     * Stream für den Nachrichteneingang
     */
    private DataInputStream dis;
    
    /**
     * Stream für den Nachrichtenausgang
     */
    private DataOutputStream dos;

    /**
     * Thread zum verwalten der Nachrichteneingänge
     */
    private InputChannel input;

    /**
     * Status der Verbindung
     */
    private boolean isConnect;

    /**
     * Schnittstelle um die Nachirchteneingänge weiterzugegben
     */
    private IRoboter roboter;

    BTConnect(IRoboter robo) {
        this.isConnect = false;
        this.roboter = robo;
    }


    /**
     * Startet die Kommunikationsverwaltung
     * @return
     */
    public boolean Connection() {
        try {
            LCD.drawString("WAIT..", 0, 1);
            this.input = new InputChannel();
            this.btc = Bluetooth.waitForConnection();
            this.dis = btc.openDataInputStream();
            this.dos = btc.openDataOutputStream();
            LCD.drawString("VERBUNDEN", 0, 1);
            this.input.start();

            this.isConnect = true;

            return true;

        } catch (Exception e) {
            this.Close();
            return false;
        }


    }

    /**
     * Schließt die verbinung zum Leitstand
     */
    public void Close() {
        try {
            this.input.CloseInput();
            this.dos.close();
            this.dis.close();
            this.btc.close();

            this.ResetForNewStart();
        } catch (IOException e1) {

            //Fehlermeldung
            //"Fehler mein schlieï¿½en der Verbindung";
        }
    }

    /**
     * Resetet die Informationen für eine Verbindung
     */
    private void ResetForNewStart() {
        this.input = null;
        this.btc = null;
        this.dis = null;
        this.dos = null;
    }


    /**
     * Sendet ine Nachricht zum Leitstand
     * @param m Zu sendene Nachricht
     */
    public synchronized void SendPosition(String m) {
        try {
            m += "#";
            dos.write(m.getBytes());
            dos.flush();

        } catch (Exception e) {

        }
    }


    /**
     * @author Marcel Reich
     * thread zum Verwaltuen des Nachrichteneingangs
     *
     */
    private class InputChannel extends Thread {

        private String message = "";

        private boolean disconnect;

        InputChannel() {
            disconnect = false;
        }

        /* (non-Javadoc)
         * @see java.lang.Thread#run()
         */
        public void run() {

            try {
                long time_last_input = System.currentTimeMillis();


                LCD.drawString("INPUT START", 0, 4);
                //		while(((System.currentTimeMillis()-time_last_input) < disconnect_time) && !this.disconnect )
                while (!this.disconnect) {
                    while (dis.available() != 0 && !this.disconnect) {

                        char c = (char) dis.readByte();

                        if (c != '+') {
                            if (c == '#') {
                                roboter.InputMessage(message);
                                message = "";
                            } else {
                                message += c;
                            }
                        }
                    }
                }
                LCD.drawString("INPUT ENDE", 0, 4);


            } catch (Exception e) {


            }


        }

        public void CloseInput() {
            this.disconnect = true;
        }
    }


}
