package com.edinarobotics.purepursuit.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.subsystems.Drivetrain;

//todo: add PPDrive method, commandGroup. Rework structure

public class PurePursuitCommand extends Command {
    private Drivetrain drivetrain;

    private double lookAheadDist;
    private Point startPoint;
    private Point endPoint;
    private Line line;

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

    public PurePursuitCommand(Point pt1, Point pt2, double lookAheadDist) {
        requires(drivetrain);

        this.lookAheadDist = lookAheadDist;
        startPoint = pt1;
        endPoint = pt2;
        line = new Line(startPoint, endPoint, lookAheadDist);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        //todo: loop PP algorithm
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
