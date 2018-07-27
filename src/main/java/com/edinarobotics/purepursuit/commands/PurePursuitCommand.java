package com.edinarobotics.purepursuit.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.subsystems.Drivetrain;

//todo: add PPDrive, commandGroup. Rework structure

public class PurePursuitCommand extends Command {
    private static final double MIN_TURN_SPEED = 0.1;
    private static final double kP_TURN = -0.02;

    private Drivetrain drivetrain;

    private double lookAheadDist;
    private Point startPoint;
    private Point endPoint;
    private Line line;

    private double currXPos, currYPos;
    private double velocity;

    static class Point {
        double x, y;

        Point(double x, double y) { this.x = x; this.y = y; }

        public String toString() { return String.format("[%.2f, %.2f]", x, y); }
    }

    static class Line {
        Point pt1;
        Point pt2;
        private double angle;
        private double lookAheadDist;

        Line(Point pt1, Point pt2, double lookAheadDist) {
            this.pt1 = pt1;
            this.pt2 = pt2;
            this.angle = Math.atan2(pt2.y - pt1.y, pt2.x - pt1.x);
            this.lookAheadDist = lookAheadDist;
        }

        double getAngleRad() { return angle; }
        double getAngleDeg() { return Math.toDegrees(angle); }

        double getDesiredHeading(double botX, double botY) {
            double theta = Math.atan2(botY - pt1.y, botX - pt1.x) - getAngleRad(); //i dont understand how atan2 works, clearly
            double dist = Math.sqrt( ( Math.pow(botX - pt1.x, 2) ) + ( Math.pow(botY - pt1.y, 2) ) );

            double yOffset = Math.sin(theta) * dist;

            return Math.toDegrees(Math.asin(yOffset / lookAheadDist)) + getAngleDeg();

            //todo: add case for when look-ahead cannot 'find' line -- travel in dir of y-offset line
        }

        boolean continueRun(double botX, double botY) {
            double dist = ( Math.pow(botX - pt2.x, 2) ) + ( Math.pow(botY - pt2.y, 2) );

            if(dist < Math.pow(lookAheadDist, 2)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public PurePursuitCommand(Point pt1, Point pt2, double lookAheadDist, double velocity) {
        super("purepursuitcommand");
        requires(drivetrain);

        this.lookAheadDist = lookAheadDist;
        startPoint = pt1;
        endPoint = pt2;
        line = new Line(startPoint, endPoint, lookAheadDist);

        this.velocity = velocity;
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

        double desiredHeading = line.getDesiredHeading(currXPos, currYPos);
        double angleError = desiredHeading - drivetrain.getGyroAngle(); //positive - ccw; negative - cw;
        double powerDeduction;
        if( kP_TURN * angleError > 45) {
            powerDeduction = -0.9;
        } else {
            powerDeduction = kP_TURN * angleError;
        }

        if(velocity - powerDeduction < 0.1) {
            powerDeduction = velocity - 0.1;
        }

        if(angleError < 0) {
            drivetrain.setDrivetrain(velocity, velocity - powerDeduction);
        } else if (angleError > 0) {
            drivetrain.setDrivetrain(velocity - powerDeduction, velocity);
        } else {
            drivetrain.setDrivetrain(velocity, velocity);
        }
    }

    @Override
    protected boolean isFinished() {
        //todo: check continueRun() method form Line class
        return false;
    }

    @Override
    protected void end() {
        //todo: shutoff motors
    }

    @Override
    protected void interrupted() {
        super.interrupted();
    }
}
