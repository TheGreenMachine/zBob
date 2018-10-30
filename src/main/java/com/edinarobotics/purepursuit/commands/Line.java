package com.edinarobotics.purepursuit.commands;

import com.edinarobotics.utils.math.Math1816;

public class Line {
    private Point pt1, pt2;
    private double lookAheadDist;
    private double slope;
    private double angle;
    private double yIntercept;

    public Line(Point p1, Point p2, double lookAheadDist){
        this.lookAheadDist = lookAheadDist;
        this.pt1 = pt1;
        this.pt2 = pt2;
        this.slope = (p2.getY() - p1.getY())/(p2.getX() - p1.getX());
        this.yIntercept = p1.getY() - this.slope * p1.getX();
        this.angle = Math.atan2((p2.getY() - p1.getY()), (p2.getX() - p1.getX()));
    }

    public double getDesiredHeading(double robotX, double robotY){
        //Equation for the path
        // y = a * x + b
        //Equation for the circle
        // (x - h)^2 + (y - k)^2 = radius ^2
        //Quadratic Formula; Finds the points of intersection of the line of vision (circle) with the line connecting two adjacent targets
        double quadraticA = (1 + slope * slope);
        double quadraticB = (2 * slope * (yIntercept - robotY) - 2 * robotX);
        double quadraticC = Math.pow(robotX, 2) + Math.pow(yIntercept - robotY, 2) - Math.pow(lookAheadDist, 2);
        double delta = quadraticB * quadraticB - 4 * quadraticA * quadraticC;
        double x1 = 0;
        double y1 = 0;
        if (delta >= 0) {
            //Assumption: Robot is only moving forward, so only display the larger solution
            x1 = (-quadraticB + Math.sqrt(delta)) / (2 * quadraticA);
            y1 = slope * x1 + yIntercept;
        }

        double desiredHeading = Math1816.atanApprox((y1 - robotY), (x1 - robotX));
        return desiredHeading;
    }


    public boolean continueRun(double robotX, double robotY) {
        double dist = ( Math.pow(robotX - pt2.x, 2) ) + ( Math.pow(robotY - pt2.y, 2) );

        if(dist < Math.pow(lookAheadDist, 2)) {
            return false;
        } else {
            return true;
        }
    }

    public double getAngle() {return this.angle; }

    public double getAngleDeg() {return Math.toDegrees(this.angle); }

    public double getSlope(){ return this.slope; }

    public double getYIntercept() { return this.yIntercept; }

}