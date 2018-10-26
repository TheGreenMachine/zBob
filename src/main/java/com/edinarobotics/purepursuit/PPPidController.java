package com.edinarobotics.purepursuit;

public class PPPidController {
    private double kP_TURN;
    private double kI_TURN;
    private double kD_TURN;

    private double kOut, iOut, dOut;
    private double setHeading, currHeading, prevHeading;

    public PPPidController(double kP, double kI, double kD) {
        kP_TURN = kP;
        kI_TURN = kI;
        kD_TURN = kD;
    }
}