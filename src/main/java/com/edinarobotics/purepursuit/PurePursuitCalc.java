package com.edinarobotics.purepursuit;

public class PurePursuitCalc {
    private static final double MIN_TURN_SPEED = 0.1;
    private static final double kP_TURN = -0.03; //todo: tune value

    private PPPoint startPoint;
    private PPPoint endPoint;
    private PPLine path;

    private double currX, currY, currHeading, desiredHeading;
    private double leftSetV, rightSetV;
    private double maxVel;

    public PurePursuitCalc(PPPoint pt1, PPPoint pt2, double lookAheadDist, double targetVelocity) {
        startPoint = pt1;
        endPoint = pt2;
        path = new PPLine(startPoint, endPoint, lookAheadDist);
        this.maxVel = targetVelocity;
    }

    public double[] calcVelocities(double x, double y, double heading) {
        double[] velocities = new double[2];
        currX = x;
        currY = y;
        currHeading = heading;

        desiredHeading = path.getDesiredHeading(currX, currY) - 90;
        double angleError = desiredHeading - currHeading;
        double powerDeduction;

        powerDeduction = Math.abs(kP_TURN * angleError);

        //cap powerDeduction so robot speed never drops below MIN_TURN_SPEED
        powerDeduction = Math.max(powerDeduction, maxVel - MIN_TURN_SPEED);

        System.out.println("Angle Error: " + angleError);

        if(angleError > 0) {
            leftSetV = maxVel;
            rightSetV = maxVel - powerDeduction;
        } else if (angleError < 0) {
            leftSetV = maxVel - powerDeduction;;
            rightSetV = maxVel;
        } else {
            leftSetV = maxVel;
            rightSetV = maxVel;
        }

        velocities[0] = leftSetV;
        velocities[1] = rightSetV;
        return velocities;
    }

    public boolean endPath() {
        return !path.continueRun(currX, currY);
    }

    public double[] getData() {
        double [] data = new double[] {currX, currY, currHeading, desiredHeading, leftSetV, rightSetV};
        return data;
    }
}