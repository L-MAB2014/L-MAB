package com.Master;

import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.logging.Logger;

public class BT {

	/**
	 * logger
	 */
	private static Logger logger = Logger.getAnonymousLogger();
    /**
     * Dateneingangsstream
     */
    public DataInputStream dis;
    /**
     * Verbindungsobjekt
     */
    private NXTConnector connection;
    /**
     * Dateneingangsstream
     */
    private DataOutputStream dos;
    /**
     * Name des zu Verbinden Bots
     */
    private String bt_Name;
    /**
     * Controller für Informationen
     */
    private IController controller;

    /**
     * Manager zum Nachrichteneingang
     */
    private InputChannel input;

    /**
     * Verbindungsstauts zum Bot
     */
    private boolean connect;

    /**
     * Bot zu welchem eine Verbindung aufgebaut wird
     */
    private IBot bot;


    BT(String bt_Name, IController controller, IBot bot) {
        this.bot = bot;
        this.bt_Name = bt_Name;
        this.controller = controller;
        this.input = new InputChannel();
        this.connect = false;
    }

    /**
     * Legt das Verbindungs-Objekt an und baut die Verbindung auf
     *
     * @param address Adresse des zu verbindenen Bots
     * @return Verbindungs-Objekt
     */
    private  NXTConnector CreateAndConnect(String address) {
        NXTConnector connection = new NXTConnector();

        connection.addLogListener(new NXTCommLogListener() {

            public void logEvent(String message) {
            	logger.info("Bot "+bt_Name+" BTSend Log.listener: " + message);
            }

            public void logEvent(Throwable throwable) {
            	logger.info("Bot "+bt_Name+" BTSend Log.listener - stack trace: ");
                throwable.printStackTrace();
            }
        });

        boolean connected = connection.connectTo(address);

        if (!connected) {
        	logger.info("Bot "+bt_Name+" Failed to connect to any NXT");
            return null;
        }
        return connection;
    }

    /**
     * überprüft und leitet den Verbindungsaufbau
     *
     * @return
     */
    public boolean ConnectAgent() {
        try {
            this.connection = CreateAndConnect("btspp://" + this.bt_Name);

            if (this.connection != null) {
                this.dos = new DataOutputStream(connection.getOutputStream());
                this.dis = new DataInputStream(connection.getInputStream());
                this.connect = true;
                input.start();
                return true;
            } else {
                this.connect = false;
                return false;
            }

        } catch (Exception e) {
        	logger.info("Bot "+bt_Name+"-(MessageFromBot()):"+e);
            this.connect = false;
            return false;
        }
    }

    /**
     * Bereitet das Objekt auf eine erneute Verbindung vor
     */
    private void ResetForNewConnect() {
        this.connection = null;
        this.dos = null;
        this.dis = null;
        this.connect = false;
        this.input = new InputChannel();
    }

    /**
     * Schlieüt die Verbindung zum Bot
     */
    public void CloseAgent() {
        try {
            this.connect = false;
            this.connection.close();
            this.dis.close();
            this.dos.close();
            this.input.stop();


        } catch (Exception e) {

        	logger.info("Bot "+bt_Name+"-(MessageFromBot()):"+e);
        } finally {
            this.ResetForNewConnect();
        }

    }

    /**
     * Sendet NAchrichten zum Bot
     *
     * @param message Zu sendene Nachricht
     * @return Ob die Sendung erfolgreich verschickt wurde oder nicht
     */
    public boolean SendMessage(String message) {
        try {
        	logger.info("Bot "+bt_Name+"  bekommt folgende Nachrcht gesendet: "+message);
            dos.write(message.getBytes());
            dos.flush();

            return true;

        } catch (Exception e) {

        	logger.info("Bot "+bt_Name+"-(MessageFromBot()):"+e);
            return false;
        }
    }

    /**
     * Holt die Nachrichten aus dem Eingangs-Stream
     *
     * @return Empfangende Nachricht
     */
    public String MessageFromBot() {
        try {
            String message = "";
            char c = (char) this.dis.readByte();
            while (c != '#') {
                message += c;
                c = (char) this.dis.readByte();
            }

            return message;

        } catch (Exception e) {
        	
        	logger.info("Bot "+bt_Name+"-(MessageFromBot()):"+e);
            return null;
        }
    }

    /**
     * Gibt an ob eine eine Bluetooth-Verbindung besteht oder nicht
     *
     * @return Status der Verbindung
     */
    public boolean isConnect() {
        return this.connect;
    }


    /**
     * Klasse welche den Nachrichteneingang der Bluetooth-Verbindung hündelt
     */
    private class InputChannel extends Thread {

        /* (non-Javadoc)
         * @see java.lang.Thread#run()
         */
        public void run() {

            try {
            	logger.info("Der Input Channel f�r Bot "+bt_Name+" wurde gestartet");
                while (connect) {
                    String message = MessageFromBot();
                    bot.HandleMessageInput(message);
                }

            } catch (Exception e) {

            	logger.info("Bot "+bt_Name+"-(MessageFromBot()):"+e);
            }
            logger.info("Der Input Channel f�r Bot "+bt_Name+" wurde beendet");
            controller.InputConsole(bt_Name + ": Input-Cahnnel beendet");

        }

    }

}
