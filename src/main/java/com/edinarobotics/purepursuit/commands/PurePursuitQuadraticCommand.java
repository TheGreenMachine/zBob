package com.edinarobotics.purepursuit.commands;

import com.edinarobotics.utils.math.Math1816;
import com.edinarobotics.utils.tracking.Line;
import com.edinarobotics.utils.tracking.Point;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;

public class PurePursuitQuadraticCommand extends Command {
    private Drivetrain drivetrain;

    private double lookAheadDist;
    private Point startPoint;
    private Point endPoint;
    private Line pathSegment;

    private double robotX, robotY, prevX, prevY;

    public PurePursuitQuadraticCommand(Point pt1, Point pt2, double lookAheadDist) {
        drivetrain = Components.getInstance().drivetrain;
        requires(drivetrain);

        this.lookAheadDist = lookAheadDist;
        this.startPoint = pt1;
        this.endPoint = pt2;
        this.pathSegment = new Line(pt1, pt2);
    }

    @Override
    protected void initialize() {
        //this.robotX = drivetrain.getxPos();
        //this.robotY = drivetrain.getyPos();
        this.robotX = 0;
        this.robotY = 0;
        this.prevX = this.robotX;
        this.prevY = this.robotY;
    }

    @Override
    protected void execute() {
        //Equation for the path
        // y = a * x + b
        double a = pathSegment.getSlope();
        double b = pathSegment.getYIntercept();
        // (x - h)^2 + (y - k)^2 = radius ^2
        // y = a * x + b
        //Quadratic Formula; Finds the points of intersection of the line of vision (circle) with the line connecting two adjacent targets
        double A = (1 + a * a);
        double B = (2 * a * ( b - robotY) - 2 * robotX);
        double C = Math.pow(robotX, 2) + Math.pow(b - robotY, 2) - Math.pow(lookAheadDist, 2);
        double delta = B * B - 4 * A * C;
        double x1 = 0;
        double y1 = 0;
        if (delta >= 0) {
            //Assumption: Robot is only moving forward, so only display the larger solution
            x1 = (-B + Math.sqrt(delta)) / (2 * A);
            y1 = a * x1 + b;
        }

        double heading = Math1816.atanApprox((y1 - robotY)/(x1 - robotX));        
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}