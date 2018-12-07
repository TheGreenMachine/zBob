package com.edinarobotics.purepursuit;

import java.io.BufferedReader;
import java.io.FileReader;

public class PPTest {
    /**
     * Run this by executing:  java com.edinarobotics.purepursuit.PPTest xRobot yRobot numPts maxAngle.
     *
     * For example this command will show 21 points going from -20 degrees to 20 degrees for a robot at 0, 0:
     *
     *     java com.edinarobotics.purepursuit.PPTest 0.0 0.0 21 20
     */
    public static void main(String [] args) {
    	if (args.length > 1 && args[0].equals("-f")) {
    		readFile(args[1]);
    	} else if (args.length > 1 && args[0].equals("-l")) {
    		testLine();
    	} else {
    		testCalc(args);
    	}
    }
    
    public static void testLine() {
    	PPPoint pt2 = new PPPoint(0, 24);
		PPPoint pt3 = new PPPoint(36, 36);
        double lookAheadDist = 4.0;
        double targetVelocity = 0.25;

		PPLine line = new PPLine(pt2, pt3, lookAheadDist);

		double x = 0.0;
		for (double y = 20.0; y < 30.0; y += 1.0) {
			double desiredHdg = line.getDesiredHeading(x, y);
			System.out.printf("%6.2f, %6.2f, %7.2f, %7.2f\n", x, y, desiredHdg, desiredHdg -90);
		}
    }
    
    public static void testCalc(String args[]) {
        PPPoint pt1 = new PPPoint(0.0, 24.0);
        PPPoint pt2 = new PPPoint(36.0, 36.0);
        double lookAheadDist = 4.0;
        double targetVelocity = 0.25;

        PurePursuitCalc calc = new PurePursuitCalc(pt1, pt2, lookAheadDist, targetVelocity);
        double heading;
        double x = (args.length > 0) ? Double.parseDouble(args[0]) : 10.0;
        double y = (args.length > 1) ? Double.parseDouble(args[1]) : 10.0;
        final int numPoints = (args.length > 2) ? Integer.parseInt(args[2]) : 11;
        double maxAngle = (args.length > 3) ? Double.parseDouble(args[3]) : 20;
        double stepSize = maxAngle * 2.0 / ((double)numPoints - 1.0);

        for (int i = 0; i < numPoints; i++) {
            heading = -maxAngle + ((double)i * stepSize);
            for (int j = 0; j < 10; j++) {
	            double[] data = calc.calcVelocities(x, y, heading);
	            double[] alldata = calc.getData();
	            String msg = String.format("currX=%6.2f, currY=%6.2f, currHeading=%6.2f, desiredHeading=%6.2f, leftSetV=%6.2f, rightSetV=%6.2f",
	                    alldata[0], alldata[1], alldata[2], alldata[3], alldata[4], alldata[5]);
	            System.out.println(msg);
            }
        }
    }
    
    public static void readFile(String fname) {
		PPPoint pt1 = new PPPoint(0,0);
		PPPoint pt2 = new PPPoint(0, 24);
		PPPoint pt3 = new PPPoint(36, 36);
		PPPoint pt4 = new PPPoint(60, 60);

    	PPPoint pts[] = {pt1, pt2, pt3, pt4 };
        double lookAheadDist = 4.0;
        double targetVelocity = 0.25;

        int idx = 0, lineNum = 0;
        PurePursuitCalc calc = new PurePursuitCalc(pts[idx], pts[idx+1], lookAheadDist, targetVelocity);

        try (BufferedReader reader = new BufferedReader(new FileReader(fname))) {
    		String ln;
    		double x, y, currHdg, data[], alldata[];
    		
    		while ( (ln = reader.readLine()) != null) {
	    		if (ln.length() < 10) continue;
	    		String flds[] = ln.split(",");
	    		if (flds.length < 3) continue;
	    		if (flds[0].startsWith("curr")) {
	    			if (lineNum > 0) {
	    				idx++;
	    				System.out.println("--------------------------------------------------------------------");
	    				if (idx > pts.length - 2) {
	    					break;
	    				}
	    		        calc = new PurePursuitCalc(pts[idx], pts[idx+1], lookAheadDist, targetVelocity);

	    			};
	    			continue;
	    		}
	    		x = Double.parseDouble(flds[0]);
	    		y = Double.parseDouble(flds[1]);
	    		currHdg = Double.parseDouble(flds[2]);
	    		
	    		data = calc.calcVelocities(x, y, currHdg);
	            alldata = calc.getData();
	            // currX, currY, currHeading, desiredHeading, angleErr, control, leftSetV, rightSetV

	            String msg = String.format("currX=%6.2f, currY=%6.2f, currHdg=%6.2f, desHdg=%7.2f, angErr=%6.2f, ctrl=%5.2f, leftSetV=%6.2f, rightSetV=%6.2f",
	                    alldata[0], alldata[1], alldata[2], alldata[3], alldata[4], alldata[5], alldata[6], alldata[7]);
	            System.out.print(msg);
	            if (flds.length > 3) {
	            	System.out.printf(",  fl-desHdg=%7.2f, fl-angErr=%6.2f", Double.parseDouble(flds[3]), Double.parseDouble(flds[4]));
	            }
	            System.out.println();
	            	
	            lineNum++;
	            if (lineNum > 60) break;
    		}
    	} catch (Exception e) {
    		System.out.println("ERROR: " + e.getMessage());
    		e.printStackTrace();
    	}
    }
}
