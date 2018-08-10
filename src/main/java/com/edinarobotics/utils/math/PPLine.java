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
        double dist = Math.sqrt( ( Math.pow(botX - pt1.x, 2) ) + ( Math.pow(botY - pt1.y, 2) ) ); //todo: swap Math.pow with multiplication

        double yOffset = Math.sin(theta) * dist;

        double desiredHeading;

        if (yOffset < lookAheadDist) {
            desiredHeading = Math.toDegrees(Math.asin(yOffset / lookAheadDist)) + getAngleDeg();
            return desiredHeading;
        } else {
            return 90 + getAngleDeg();
        }

    }

    public boolean continueRun(double botX, double botY) {
        double dist = ( Math.pow(botX - pt2.x, 2) ) + ( Math.pow(botY - pt2.y, 2) );

        if(dist < Math.pow(lookAheadDist, 2)) {
            return false;
        } else {
            return true;
        }
    }
}