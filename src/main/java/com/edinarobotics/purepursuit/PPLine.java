package com.edinarobotics.purepursuit;

/**
 * The PPLine class generates a Line given 2 points for a Pure Pursuit
 * path system. It contains methods to calculate desired heading based on 
 * robot position to travel 'along' the line.
 *
 * @author    Andrew Hou
 * @version   2.0
 * @since     2018-11-14
 */
public class PPLine {
    PPPoint pt1;
    PPPoint pt2;
    private double angle;
    private double lookAheadDist;
    private double targetDeltaX;

    public PPLine(PPPoint pt1, PPPoint pt2, double lookAheadDist) {
        this.pt1 = pt1;
        this.pt2 = pt2;
        this.angle = Math.atan2(pt2.y - pt1.y, pt2.x - pt1.x);
        this.lookAheadDist = lookAheadDist;
        this.targetDeltaX = Math.sqrt((pt2.x - pt1.x)*(pt2.x - pt1.x) + (pt2.y - pt1.y)*(pt2.y - pt1.y));
    }

    public double getAngleRad() { return angle; }
    public double getAngleDeg() { return Math.toDegrees(angle); }

    public double getSlope() { return (pt2.y - pt1.y) / (pt2.x - pt1.x); }

    /**
     * This method is used to find the angle of the line between the robot's
     * position and the generated PPLine, with length {@code lookAheadDist}.
     * @param botX This is the robot's cartesian x coordinate
     * @param botY This is the robot's cartesian y coordinate
     * @return double This returns the desired heading of the robot
     */
    public double getDesiredHeading(double botX, double botY) {
        double theta = Math.atan2(botY - pt1.y, botX - pt1.x) - getAngleRad();
        double dist = Math.sqrt((botX - pt1.x) * (botX - pt1.x) + (botY - pt1.y) * (botY - pt1.y)); 

        double yOffset = Math.sin(theta) * dist;

        if (Math.abs(yOffset) < lookAheadDist) {
            //calculate desired heading

            double lineY = (getSlope() * (pt1.x - botX)) + botY;
            double desiredHeading;
            if(lineY < botY) { //TODO: I suspect different math for above/below
                desiredHeading = Math.toDegrees(Math.asin(yOffset / lookAheadDist)) + getAngleDeg();
            } else {
                desiredHeading = - Math.toDegrees(Math.asin(yOffset / lookAheadDist)) + getAngleDeg();
            }
            
            return desiredHeading;
        } else {
            //if out of range, attempt to 'find' line again
            System.out.println("Out of Range");
            return 90 + getAngleDeg();
        }

    }

    /**
     * This method is used to find the angle of the line produced between
     * the robot's position and the endpoint, to be used when this instance
     * of PPLine is the last in a chain of lines.
     * @param botX This is the robot's cartesian x coordinate
     * @param botY This is the robot's cartesian y coordinate
     * @return double This returns the desired heading of the robot
     */
    public double getDesiredHeadingEndpoint(double botX, double botY) {
        // TODO: no clue if this works
        // return the angle between current point and line endpoint
        // used for the final line segment of a Pure Pursuit path-chain

        double theta = Math.atan2(pt2.y - botY, pt2.x - botX);
        
        return Math.toDegrees(theta);
    }

    /**
     * This method is used to check whether or not a run is complete. A run is
     * considered complete when the robot has reached or passed the endpoint or
     * if it has a total displacement greater than the target displacement.
     * @param botX This is the robot's cartesian x coordinate
     * @param botY This is the robot's cartesian y coordinate
     * @return boolean This returns whether the 'run' should be continued
     */
    public boolean continueRun(double botX, double botY) {
        double dist = ( Math.pow(botX - pt2.x, 2) ) + ( Math.pow(botY - pt2.y, 2) );
        double deltaX = Math.sqrt((botX - pt1.x)*(botX - pt1.x) + (botX - pt1.y)*(botX - pt1.y));

        if(dist < Math.pow(lookAheadDist, 2) || deltaX > targetDeltaX) {
            return false;
        } else {
            return true;
        }
    }
}