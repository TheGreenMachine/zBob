package com.edinarobotics.purepursuit;

/**
 * The PurePursuitCalc class is used in the control aspect of the robot when
 * utilizing a pure pursuit autonomous system. It contains methods to take in a
 * robot's position and heading, calculate desired heading, and return desired
 * motor velocities via a built-in PID controller.
 *
 * @author Andrew Hou
 * @version 1.2
 * @since 2018-11-27
 */

public class PurePursuitCalc {
    private static final double MIN_TURN_SPEED = 0.02;

    private double kP_TURN = 0.004;
    private double kI_TURN = 0.0; // needs heavier I term
    private double kD_TURN = 0.0;
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

    /**
     * This method is used to find the desired heading of the robot using an
     * instance of PPLine, then calculate desired velocities using a PID controller.
     * 
     * @param x This is the robot's cartesian x coordinate
     * @param y This is the robot's cartesian y coordinate
     * @return double[] This returns an array containing the left and right
     *         velocities of the robot
     */
    public double[] calcVelocities(double x, double y, double heading) {
        double[] velocities = new double[2];
        currX = x;
        currY = y;
        currHeading = heading;

        desiredHeading = path.getDesiredHeading(currX, currY);

        angleErr = desiredHeading - currHeading;

        pOut = kP_TURN * angleErr;
        iOut += kI_TURN * (((angleErr + prevErr) * DELTA_T) / 2); // trapezoidal integral/area approximation
        dOut = kD_TURN * ((prevErr - angleErr) / DELTA_T);

        control = pOut + iOut + dOut;

        control = Math.min(Math.abs(control), maxVel - MIN_TURN_SPEED); // max control (deduction) is MIN_TURN_SPEED

        System.out.println("Angle Error: " + angleErr + "\tControl: " + control);

        if (angleErr > 0) {
            leftSetV = maxVel;
            rightSetV = maxVel - control;
        } else if (angleErr < 0) {
            leftSetV = maxVel - control;
            rightSetV = maxVel;
        } else {
            leftSetV = maxVel;
            rightSetV = maxVel;
        }

        velocities[0] = leftSetV;
        velocities[1] = rightSetV;
        prevErr = angleErr;
        return velocities;
    }

    /**
     * This method is a special case of the calcVelocities method for the endpoint
     * of a path-chain, calling PPLine's getDesiredHeadingEndpoint method.
     * 
     * @param x This is the robot's cartesian x coordinate
     * @param y This is the robot's cartesian y coordinate
     * @return double[] This returns an array containing the left and right
     *         velocities of the robot
     */
    public double[] calcVelocitiesEndpoint(double x, double y, double heading) { 
        // TODO: test this method/logic
        // TODO: consolidate endpoint and normal method logic (much duplicate logic)

        double[] velocities = new double[2];
        currX = x;
        currY = y;
        currHeading = heading;

        desiredHeading = path.getDesiredHeadingEndpoint(currX, currY) - 90; // TODO: this value is constant, I think

        angleErr = desiredHeading - currHeading;

        pOut = kP_TURN * angleErr;
        iOut += kI_TURN * (((angleErr + prevErr) * DELTA_T) / 2); // trapezoidal integral/area approximation
        dOut = kD_TURN * ((prevErr - angleErr) / DELTA_T);

        control = pOut + iOut + dOut;

        control = Math.min(Math.abs(control), maxVel - MIN_TURN_SPEED); // max control (deduction) is MIN_TURN_SPEED

        System.out.println("Angle Error: " + angleErr + "\tControl: " + control);

        if (angleErr > 0) {
            leftSetV = maxVel;
            rightSetV = maxVel - control;
        } else if (angleErr < 0) {
            leftSetV = maxVel - control;
            rightSetV = maxVel;
        } else {
            leftSetV = maxVel;
            rightSetV = maxVel;
        }

        velocities[0] = leftSetV;
        velocities[1] = rightSetV;
        return velocities;
    }

    /**
     * Check if the robt has completed the PPLine
     * 
     * @return boolean This method returns true if the path is completed
     */
    public boolean endPath() {
        return !path.continueRun(currX, currY);
    }

    /**
     * Sets the PID feedback control constants
     * 
     * @param kP This is the PID controller's kP constant
     * @param kI This is the PID controller's kI constant
     * @param kD This is the PID controller's kD constant
     */
    public void setPIDValues(double kP, double kI, double kD) {
        kP_TURN = kP;
        kI_TURN = kI;
        kD_TURN = kD;
    }

    /**
     * Gets the PID controller's constants
     * 
     * @return double[] This returns an array containing the PID controller's constants
     */
    public double[] getPIDData() {
        double[] piddata = new double[] { pOut, iOut, dOut };
        return piddata;
    }

    /**
     * Gets the header for the data output
     * 
     * @return String This returns a comma separated list of the data headers
     */
    public String getDataHeader() {
        return "currX,currY,currHeading,desiredHeading,angleErr,control,leftSetV,rightSetV";
    }

    /**
     * Gets the debug and diagnostic data
     * 
     * @return double[] This returns an array containing various values useful
     *         debugging and diagnostics
     */
    public double[] getData() {
        double[] data = new double[] { currX, currY, currHeading, desiredHeading, angleErr, control, leftSetV,
                rightSetV };
        return data;
    }
}
