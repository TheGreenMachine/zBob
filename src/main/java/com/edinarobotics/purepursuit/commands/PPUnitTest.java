package com.edinarobotics.purepursuit.commands;

public class PPUnitTest {
    /**
     * Run this by executing:  java com.edinarobotics.purpursuit.PPUnitTest xRobot yRobot numPts maxAngle.
     *
     * For example this command will show 21 points going from -20 degrees to 20 degrees for a robot at 0, 0:
     *
     *     java com.edinarobotics.purepursuit.PPUnitTest 0.0 0.0 21 20
     */
    public static void main(String [] args) {
        Point pt1 = new Point(0.0, 0.0);
        Point pt2 = new Point(0.0, 100.0);
        double lookAheadDist = 10.0;
        double targetVelocity = 0.5;

        PurePursuitCalc calc = new PurePursuitCalc(pt1, pt2, lookAheadDist, targetVelocity);
        double heading;
        double x = (args.length > 0) ? Double.parseDouble(args[0]) : 0.0;
        double y = (args.length > 1) ? Double.parseDouble(args[1]) : 0.0;
        final int numPoints = (args.length > 2) ? Integer.parseInt(args[2]) : 11;
        double maxAngle = (args.length > 3) ? Double.parseDouble(args[3]) : 20;
        double stepSize = maxAngle * 2.0 / ((double)numPoints - 1.0);

        for (int i = 0; i < numPoints; i++) {
            heading = -maxAngle + ((double)i * stepSize);
            double[] data = calc.calcVelocities(x, y, heading);
            double[] alldata = calc.getData();
            String msg = String.format("currX=%6.2f, currY=%6.2f, currHeading=%6.2f, desiredHeading=%6.2f, leftSetV=%6.2f, rightSetV=%6.2f",
                    alldata[0], alldata[1], alldata[2], alldata[3], alldata[4], alldata[5]);
            System.out.println(msg);
        }
    }
}