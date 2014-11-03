package com.jbein.BTReceive;

import lejos.nxt.*;

public class Lichtest {

    public static void Test() {
        ColorSensor colorSensor = new ColorSensor(SensorPort.S1);

        Button.ESCAPE.addButtonListener(new ButtonListener() {
            public void buttonPressed(Button b) {
                System.exit(0);
            }

            public void buttonReleased(Button b) {
            }
        });


        while (true) {
            LCD.drawString("Licht: " + colorSensor.getLightValue() + "     ", 0, 1);
        }


    }


    public static void main(String[] args) {
        Test();
    }

}
