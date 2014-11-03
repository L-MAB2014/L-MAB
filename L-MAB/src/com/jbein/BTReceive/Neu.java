package com.jbein.BTReceive;

import com.marcel.Communication.BTConnect;
import com.marcel.Communication.OrderManagement;
import lejos.nxt.*;
import lejos.robotics.Color;
import lejos.util.Delay;

public class Neu {


    private ColorSensor colorSensor;

    private UltrasonicSensor ultra;

    private NXTRegulatedMotor rightMotor;
    private NXTRegulatedMotor leftMotor;

    private BTConnect bt;

    //private ThreadUltraSensor thread_ultra;

    private OrderManagement manager;

    private boolean waite;


    private int tp = 250;
    private int light;
    private int high;
    private int low;
    private int offset;
    private int turn;
    private int kp;
    private int error;

    private int park_Position;

    private long last_checkpoint;

    private boolean IsEnding;

    private boolean IsParking;


    private int counter = 0;


    Neu() {

        this.IsEnding = false;
        this.waite = false;

        this.park_Position = 2;

        this.IsParking = true;

        this.last_checkpoint = 0;

        this.manager = new OrderManagement();


        this.colorSensor = new ColorSensor(SensorPort.S1);
        this.ultra = new UltrasonicSensor(SensorPort.S2);

        this.rightMotor = new NXTRegulatedMotor(MotorPort.C);
        this.leftMotor = new NXTRegulatedMotor(MotorPort.A);

        this.rightMotor.setAcceleration(3000);
        this.leftMotor.setAcceleration(3000);

        this.rightMotor.setSpeed(tp);
        this.leftMotor.setSpeed(tp);

        //	this.thread_ultra = new ThreadUltraSensor(this.ultra);

        Button.ESCAPE.addButtonListener(new ButtonListener() {
            public void buttonPressed(Button b) {
                IsEnding = true;
                System.exit(0);
            }

            public void buttonReleased(Button b) {
            }
        });

        this.drive();
    }

    public static void main(String[] args) {

        Neu n = new Neu();

    }

    private void drive() {
        try {
            //		this.thread_ultra.StartThreadSensor();

            init();
            fahrzu();

        } catch (Exception e) {

        } finally {
//			this.thread_ultra.StopThreadSensor();
        }

    }

    void init() {

        LCD.drawString("HELL: ", 0, 0);

        Button.waitForAnyPress();

        high = colorSensor.getLightValue();

        LCD.drawString("Hell: " + high, 0, 0);

        Button.waitForAnyPress();
        LCD.clear();

        LCD.drawString("Dunkel:", 0, 0);

        Button.waitForAnyPress();

        low = colorSensor.getLightValue();

        LCD.drawString("Dunkel: " + low, 0, 0);

        Button.waitForAnyPress();

        LCD.clear();

        offset = (high + low) / 2;

        LCD.drawString("Mitte: " + offset, 0, 0);

        kp = tp / ((high - offset) / 2);
        if (kp < 1)
            kp = 1;

        LCD.drawString("KP: " + kp, 0, 1);
        LCD.drawString("Init Fertig", 0, 2);
        Button.waitForAnyPress();
        LCD.clear();
    }

    void links90() {

        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

        rightMotor.setSpeed(200);
        leftMotor.setSpeed(200);
        rightMotor.rotate(450);
    }

    void rechts90() {

        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

        rightMotor.setSpeed(200);
        leftMotor.setSpeed(200);
        leftMotor.rotate(400);
    }

    void links45() {

        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

        rightMotor.setSpeed(200);
        leftMotor.setSpeed(200);
        rightMotor.rotate(220);
    }

    void turn180() {

        this.leftMotor.stop(true);
        this.rightMotor.stop(true);

        rightMotor.setSpeed(200);
        leftMotor.setSpeed(200);
        leftMotor.backward();
        rightMotor.forward();

        Delay.msDelay(2000);


    }

    private void fahrzu() {

        leftMotor.forward();
        rightMotor.forward();

        this.last_checkpoint = System.currentTimeMillis();

        while (true) {
            light = colorSensor.getLightValue();
            int f = this.colorSensor.getColorID();

            if ((System.currentTimeMillis() - this.last_checkpoint) > 1000) {
                //	if(this.colorSensor.getColorID() == farbe)
                if (f == Color.RED || f == Color.YELLOW) {

                    try {

                        this.leftMotor.stop(true);
                        this.rightMotor.stop(true);


                        Thread.sleep(500);

                        leftMotor.forward();
                        rightMotor.forward();

                        this.last_checkpoint = System.currentTimeMillis();

                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }


            if (light > high)
                light = high;
            if (light < low)
                light = low;

            error = light - offset;

            turn = kp * error;

            if (turn > 100)
                turn = 100;
            if (turn < -100)
                turn = -100;

            LCD.drawString("Licht: " + light, 0, 1);
            LCD.drawString("Links: " + (tp + turn), 0, 2);
            LCD.drawString("Rechts: " + (tp - turn), 0, 3);
            LCD.drawString("Counter: " + counter++, 0, 4);

            leftMotor.setSpeed(tp + turn);
            rightMotor.setSpeed(tp - turn);


        }


    }

}
