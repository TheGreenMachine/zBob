package com.edinarobotics.purepursuit;

public class PurePursuitCalc {
    private static final double MIN_TURN_SPEED = 0.1;

    //todo: tune PID values
    private static final double kP_TURN = 0.002;
    private static final double kI_TURN = 0.001; //needs heavier I term
    private static final double kD_TURN = 0;
    private static final double DELTA_T = 0.02;

    private double pOut, iOut, dOut;
    private double angleErr, prevErr;
    private double control;

    private PPPoint startPoint;
    private PPPoint endPoint;
    private PPLine path;

    private double currX, currY, desiredHeading, currHeading;
    private double leftSetV, rightSetV;
    private double maxVel;

    public PurePursuitCalc(PPPoint pt1, PPPoint pt2, double lookAheadDist, double targetVelocity) {
        startPoint = pt1;
        endPoint = pt2;
        path = new PPLine(startPoint, endPoint, lookAheadDist);
        this.maxVel = targetVelocity;

        prevErr = 0;
        iOut = 0;
    }

    public double[] calcVelocities(double x, double y, double heading) {
        double[] velocities = new double[2];
        currX = x;
        currY = y;
        currHeading = heading;

        desiredHeading = path.getDesiredHeading(currX, currY) - 90;

        angleErr = desiredHeading - currHeading;

        pOut = kP_TURN * angleErr;
        iOut += kI_TURN * (((angleErr + prevErr) * DELTA_T ) / 2); //trapezoidal integral/area approximation
        dOut = kD_TURN * ((prevErr - angleErr) / DELTA_T);

        control = pOut + iOut + dOut;

        control = Math.min(Math.abs(control), maxVel - MIN_TURN_SPEED); //max control (deduction) is MIN_TURN_SPEED

        System.out.println("Angle Error: " + angleErr + "\tControl: " + control);

        if(angleErr > 0) {
            leftSetV = maxVel;
            rightSetV = maxVel - control;
        } else if (angleErr < 0) {
            leftSetV = maxVel - control;;
            rightSetV = maxVel;
        } else {
            leftSetV = maxVel;
            rightSetV = maxVel;
        }

        velocities[0] = leftSetV;
        velocities[1] = rightSetV;
        return velocities;
    }

    public double[] calcVelocitiesEndpoint(double x, double y, double heading) {
        // TODO: test this method/logic
        
        double[] velocities = new double[2];
        currX = x;
        currY = y;
        currHeading = heading;

        desiredHeading = path.getDesiredHeadingEndpoint(currX, currY) - 90;

        angleErr = desiredHeading - currHeading;

        pOut = kP_TURN * angleErr;
        iOut += kI_TURN * (((angleErr + prevErr) * DELTA_T ) / 2); //trapezoidal integral/area approximation
        dOut = kD_TURN * ((prevErr - angleErr) / DELTA_T);

        control = pOut + iOut + dOut;

        control = Math.min(Math.abs(control), maxVel - MIN_TURN_SPEED); //max control (deduction) is MIN_TURN_SPEED

        System.out.println("Angle Error: " + angleErr + "\tControl: " + control);

        if(angleErr > 0) {
            leftSetV = maxVel;
            rightSetV = maxVel - control;
        } else if (angleErr < 0) {
            leftSetV = maxVel - control;;
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

    public String getDataHeader() {
        return "currX,currY,currHeading,desiredHeading,angleErr,control,leftSetV,rightSetV";
    }
    public double[] getData() {
        double [] data = new double[] {currX, currY, currHeading, desiredHeading, angleErr, control, leftSetV, rightSetV};
        return data;
    }

    public double[] getPIDData() {
        double [] piddata = new double[] {pOut, iOut, dOut};
        return piddata;
    }
}
