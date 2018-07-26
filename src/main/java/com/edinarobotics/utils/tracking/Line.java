package com.edinarobotics.utils.tracking;

import static com.edinarobotics.utils.math.Math1816.atanApprox;

public class Line {
    private Point p1, p2;
    private double slope;
    private double angle;
    private double yIntercept;

    public Line(Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;
        this.slope = (p2.getY() - p1.getY())/(p2.getX() - p1.getX());
        this.yIntercept = p1.getY() - this.slope * p1.getX();
        this.angle = atanApprox(slope);
    }

    public double getAngle() {return this.angle; }

    public double getAngleDeg() {return Math.toDegrees(this.angle); }

    public double getSlope(){ return this.slope; }

    public double getYIntercept() { return this.yIntercept; }

}