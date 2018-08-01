package com.edinarobotics.purepursuit.commands;

import com.edinarobotics.utils.math.PPLine;
import com.edinarobotics.utils.math.PPPoint;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;

//todo: this class goes in a commandGroup that strings together Lines

public class PurePursuitCommand extends Command {
    private static final double MIN_TURN_SPEED = 0.1;
    private static final double kP_TURN = -0.02; //todo: tune value

    private Drivetrain drivetrain;

    private PPPoint startPoint;
    private PPPoint endPoint;
    private PPLine path;

    private double currXPos, currYPos;
    private double targetVelocity;

    public PurePursuitCommand(PPPoint pt1, PPPoint pt2, double lookAheadDist, double targetVelocity) {
        super("purepursuitcommand");
        drivetrain = Components.getInstance().drivetrain;

        startPoint = pt1;
        endPoint = pt2;
        path = new PPLine(startPoint, endPoint, lookAheadDist);

        this.targetVelocity = targetVelocity;

        requires(drivetrain);
    }

    @Override
    protected void initialize() {
        System.out.println("Pure Pursuit Drive Init");
        currXPos = drivetrain.getXPos();
        currYPos = drivetrain.getYPos();
    }

    @Override
    protected void execute() {
        currXPos = drivetrain.getXPos();
        currYPos = drivetrain.getYPos();

        double desiredHeading = path.getDesiredHeading(currXPos, currYPos);
        double angleError = desiredHeading - drivetrain.getGyroAngle(); //positive - ccw; negative - cw
        double powerDeduction;
        if( kP_TURN * angleError > 45) {
            powerDeduction = -0.9;
        } else {
            powerDeduction = kP_TURN * angleError;
        }

        powerDeduction = Math.max(powerDeduction, targetVelocity - MIN_TURN_SPEED); //cap powerDeduction so that robot never drops below MIN_TURN_SPEED

        if(angleError < 0) {
            drivetrain.setDrivetrain(targetVelocity, targetVelocity - powerDeduction);
        } else if (angleError > 0) {
            drivetrain.setDrivetrain(targetVelocity - powerDeduction, targetVelocity);
        } else {
            drivetrain.setDrivetrain(targetVelocity, targetVelocity);
        }
    }

    @Override
    protected boolean isFinished() {
        return !(path.continueRun(currXPos, currYPos));
    }

    @Override
    protected void end() {
        drivetrain.setDrivetrain(0,0);
    }

    @Override
    protected void interrupted() {
        super.interrupted();
    }

    //-------UNIT TEST CODE-------//
    public static void main(String [] args) {
        //todo: code for unit testing
    }
}