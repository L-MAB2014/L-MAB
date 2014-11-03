package com.marcel.Communication;

import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BTConnect {

    private final int disconnect_time = 5000;


    private BTConnection btc;
    private DataInputStream dis;
    private DataOutputStream dos;

    private InputChannel input;

    private boolean isConnect;

    private IRoboter roboter;

    BTConnect(IRoboter robo) {
        this.isConnect = false;
        this.roboter = robo;
    }


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

    public void Close() {
        try {
            this.input.CloseInput();
            this.dos.close();
            this.dis.close();
            this.btc.close();

            this.ResetForNewStart();
        } catch (IOException e1) {

            //Fehlermeldung
            //"Fehler mein schlie�en der Verbindung";
        }
    }

    private void ResetForNewStart() {
        this.input = null;
        this.btc = null;
        this.dis = null;
        this.dos = null;
    }


    public synchronized void SendPosition(String m) {
        try {
            m += "#";
            dos.write(m.getBytes());
            dos.flush();

        } catch (Exception e) {

        }
    }


    private class InputChannel extends Thread {

        private String message = "";

        private boolean disconnect;

        InputChannel() {
            disconnect = false;
        }

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
