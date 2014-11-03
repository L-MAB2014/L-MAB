package com.Master;

import com.View.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Controller implements IController {

    /**
     * Benutzeroberfläche
     */
    private View view;

    /**
     * Liste der enthaltenen Bots
     */
    private List<Bot> bots;

    /**
     * Speichert sich selbst als Objekt ab
     */
    private Controller controller;

    /**
     * Enthüllt alle Checkpoints des Lagers
     */
    private HashMap<String, Checkpoint> checkpoints;

    //TODO Kann später Weg
    private int order_ID;

    //TODO Kann später Weg
    private int counter;

    Controller() {
        this.controller = this;
        this.order_ID = 0;
        this.counter = 0;
        this.view = new View();

        this.view.addControlBotListener(new ControlBotListener());
        this.view.addNewBotListener(new NewBotListener());
        this.view.addConrtolOrderListener(new ControlOrderListener());
        this.view.addNewOrderListener(new NewOrderListener());
        this.view.addStoppListener(new StoppListener());

        this.view.addSimulationStartListener(new SimulationStartListener());
        this.view.addSimulationOeffnenListener(new SimulationOeffnenListener());
        this.view.addSimulationSpeichernListener(new SimulationSpeichernListener());
        this.view.addSimulationEinstellungenListener(new SimulationEinstellungenListener());

        this.checkpoints = CreatCheckpoints.InitializeCheckpoints();
        this.bots = new ArrayList<Bot>();
    }

    public static void main(String[] args) {
        new Controller();
    }

    /* (non-Javadoc)
     * @see com.Master.IController#InputConsole(java.lang.String)
     */
    public void InputConsole(String text) {
        view.InputDialog(text);
    }

    /* (non-Javadoc)
     * @see com.Master.IController#UpdateTable(int, java.lang.String[])
     */
    public void UpdateTable(int row, String[] text) {
        view.UpdateTable(row, text);
    }

    /* (non-Javadoc)
     * @see com.Master.IController#UpdateMap(java.lang.String, java.lang.String, java.lang.String)
     */
    public void UpdateMap(String checkpoint, String last_checkpoint, String bot_name) {
        view.UpdateCheckpoint(checkpoint, last_checkpoint, bot_name);
    }

    /**
     * ÜberprÜft ob ein Bots schon mit dem übergebenen-Namen existiert
     *
     * @param name Name des Bots
     * @return Ergebnis der überprüfung
     */
    public boolean ExitsBot(String name) {
        for (int i = 0; i < this.bots.size(); ++i) {
            if (name.equals(this.bots.get(i).getBt_Name()))
                return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.Master.IController#CheckForContinue(java.lang.String)
     */
    public boolean CheckForContinue(String checkpoint, String next_checkpoint, Bot bot) {
        Checkpoint check = checkpoints.get(checkpoint);

        if (check != null) {
            Checkpoint checknext = check.getNext_WayCheckpoint();
            Checkpoint checknext2 = check.getNext_OtherCheckpoint();

            if (checknext != null && checknext.getName().equals(next_checkpoint)) {
                return this.EditCheckpointsForContinue(check, checknext, bot);
            } else if (checknext2 != null && checknext2.getName().equals(next_checkpoint)) {
                return this.EditCheckpointsForContinue(check, checknext2, bot);
            } else {
                InputConsole(bot.getBt_Name() + ": nächster Checkpunkt" + next_checkpoint + " nicht vorhanden");
            }

        } else {
            InputConsole(bot.getBt_Name() + ": aktueller Checkpunkt" + checkpoint + " nicht vorhanden");
        }
        return false;
    }

    public void ToPufferAndSetOnWaitList(String check, String entrance) {
        //TODO noch proggen
    }

    private boolean EditCheckpointsForContinue(Checkpoint checkpoint, Checkpoint next_checkpoint, Bot bot) {
        if (!next_checkpoint.isClosed()) {
            next_checkpoint.setClosed(true);
            if (checkpoint.isBotInWaitList()) {
                if (checkpoint.isReserved()) {
                    checkpoint.setClosed(false);
                    Bot waitBot = checkpoint.getFirstOnWaitList();
                    waitBot.ContinueAfterWaitList();
                } else {
                    WorkOffWaitList w = new WorkOffWaitList(checkpoint);
                    w.start();
                }
            } else {
                checkpoint.setClosed(false);
                checkpoint.setReserved(false);
            }
            return true;
        } else {
            if (!next_checkpoint.setBotOnWaitList(bot)) {
                if (!next_checkpoint.isBotInWaitList() && !next_checkpoint.isClosed()) {
                    checkpoint.setClosed(false);
                    next_checkpoint.setClosed(true);
                    return true;
                }
            } else {
                InputConsole(bot.getBt_Name() + ": in der Warteliste für  Checkpunkt" + next_checkpoint);
            }
        }

        return false;
    }

    public boolean TestEntranceForPuffer(String entrance) {
        Checkpoint check = checkpoints.get(entrance);
        if (check.isClosed() || check.isReserved())
            return false;
        return true;
    }

    public boolean EntranceReserved(String entrance) {
        Checkpoint check = checkpoints.get(entrance);

        if (!check.isReserved()) {
            check.setReserved(true);
            return false;
        }
        return false;
    }

    public boolean IsNextPufferFree(String check) {
        if (check.equals("C1")) {
            Checkpoint puffer1 = checkpoints.get("PF2");
            if (!puffer1.isClosed() && !puffer1.isReserved())
                return true;
            Checkpoint puffer2 = checkpoints.get("PF3");
            if (!puffer2.isClosed() && !puffer2.isReserved())
                return true;
        }
        if (check.equals("C2")) {
            Checkpoint puffer = checkpoints.get("PF3");
            if (!puffer.isClosed() && !puffer.isReserved())
                return true;
        }
        return false;
    }

    /**
     * ActionListener zum Betätigen des Bots Verwalten-Buttons
     */
    class ControlBotListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            InputConsole("Der Bots-Verwalten-Button wurde betätigt");
        }
    }

    /**
     * ActionListener zum Betätigen des Neuen Bots -Buttons
     */
    class NewBotListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            String bot_name = view.GetBotName();

            if (bot_name.trim().length() != 0) {
                if (!ExitsBot(bot_name)) {
                    Bot bot = new Bot(controller, bot_name, bots.size(), ("P" + (bots.size() + 1)));
                    view.InputTable(new String[]{"" + bots.size(), bot_name, "", "", "", ""});

                    if (bot.Connect()) {
                        bot.InfoUpdate();
                        bot.SendParkPosition();
                        bots.add(bot);

                    } else {
                        view.DeleteFromTable(bots.size());
                        InputConsole("Fehlermeldung konnte keine Verbindung hergestellt werden");
                        //Fehlermeldung konnte keine Verbindung hergestellt werden
                    }
                } else {
                    InputConsole("Bot mit dem Namen existiert bereits!");
                }
            } else {
                InputConsole("Fehlermeldung : Keine Eingabe");
                //Fehlermeldung: Keine Eingabe
            }
        }
    }

    /**
     * ActionListener zum Betätigen des Auftrags Verwaltungs-Buttons
     */
    class ControlOrderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //TODO Betätigen des Auftrags Verwaltungs-Buttons
        }
    }

    /**
     * ActionListener zum Betaetigen des Neuer Auftrag-Buttons
     */
    class NewOrderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            int store = view.GetStoreSelection();
            int exit = view.GetExitSelection();

            if (store != 0 && exit != 0) {
                int id = order_ID++;

                Order new_order = new Order(id, store, exit);

                if (bots.size() > 0) {
                    if (counter >= bots.size()) {
                        counter = 0;
                    }
                    bots.get(counter).NewOrder(new_order);
                    counter++;
                } else {
                    view.InputDialog("Keine Bots vorhanden");
                }
            } else {
                view.InputDialog("Lager und Ausgang wühlen!");
            }
        }
    }

    /**
     * ActionListener zum Betätigen des Stopp/Buttons
     */
    class StoppListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            InputConsole("Der Stopp-Button wurde betätigt");
        }
    }

    /**
     * ActionListener zum Betätigen des Menüteintrags "Starten"
     */
    class SimulationStartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            InputConsole("Start");
        }
    }

    /**
     * ActionListener zum Betätigen des Menüteintrags "Öffnen"
     */
    class SimulationOeffnenListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            InputConsole("Öffnen");
        }
    }

    /**
     * ActionListener zum Betätigen des Menüteintrags "Speichern"
     */
    class SimulationSpeichernListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            InputConsole("Speichern");
        }
    }

    /**
     * ActionListener zum Betätigen des Menüteintrags "Einstellungen"
     */
    class SimulationEinstellungenListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            InputConsole("Einstellungen");
        }
    }

    /**
     * Klasse welche den Nachrichteneingang der Bluetooth-Verbindung händelt
     */
    private class WorkOffWaitList extends Thread {
        Checkpoint check;
        WorkOffWaitList(Checkpoint cp) {
            this.check = cp;
        }

        /* (non-Javadoc)
         * @see java.lang.Thread#run()
         */
        public void run() {
            boolean b = true;
            while (check.isBotInWaitList() && b) {
                Bot waitBot = check.getFirstOnWaitList();
                b = waitBot.ContinueAfterWaitList();
                check = checkpoints.get(waitBot.getCheckpoint());
            }
            check.setClosed(false);
        }
    }
}
