package com.edinarobotics.purepursuit;

/**
 * The PPPoint class generates a point to be used in the PPLine class. It
 * stores an X and Y position as well as a toString() method 
 *
 * @author    Andrew Hou
 * @version   1.0
 * @since     2018-11-14
 */
public class PPPoint {
    double x, y;

    public PPPoint(double x, double y) {
        this.x = x; this.y = y;
    }

    public String toString() { return String.format("[%.2f, %.2f]", x, y); }
}