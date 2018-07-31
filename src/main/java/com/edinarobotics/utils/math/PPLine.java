package com.edinarobotics.utils.math;

public class PPLine {
    PPPoint pt1;
    PPPoint pt2;
    private double angle;
    private double lookAheadDist;

    public PPLine(PPPoint pt1, PPPoint pt2, double lookAheadDist) {
        this.pt1 = pt1;
        this.pt2 = pt2;
        this.angle = Math.atan2(pt2.y - pt1.y, pt2.x - pt1.x);
        this.lookAheadDist = lookAheadDist;
    }

    public double getAngleRad() { return angle; }
    public double getAngleDeg() { return Math.toDegrees(angle); }

    public double getDesiredHeading(double botX, double botY) {
        double theta = Math.atan2(botY - pt1.y, botX - pt1.x) - getAngleRad();
        double dist = Math.sqrt( ( Math.pow(botX - pt1.x, 2) ) + ( Math.pow(botY - pt1.y, 2) ) );

        double yOffset = Math.sin(theta) * dist;

        if(yOffset < lookAheadDist) {
            return Math.toDegrees(Math.asin(yOffset / lookAheadDist)) + getAngleDeg();
        } else {
            //todo: check case validity
            return 90 + getAngleDeg(); //travel in dir of y-offset when bot cannot 'find' line
        }

    }

    public boolean continueRun(double botX, double botY) {
        double dist = ( Math.pow(botX - pt2.x, 2) ) + ( Math.pow(botY - pt2.y, 2) );

        if(dist < Math.pow(lookAheadDist, 2)) {
            return true;
        } else {
            return false;
        }
    }
}