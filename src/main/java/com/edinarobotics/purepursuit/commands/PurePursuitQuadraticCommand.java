package com.edinarobotics.purepursuit.commands;

import com.edinarobotics.utils.tracking.Line;
import com.edinarobotics.utils.tracking.Point;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.subsystems.Drivetrain;

public class PurePursuitQuadraticCommand extends Command {
    private Drivetrain drivetrain;

    private double lookAheadDist;
    private Point startPoint;
    private Point endPoint;
    private Line pathSegment;

    public PurePursuitQuadraticCommand(Point pt1, Point pt2, double lookAheadDist) {
        requires(drivetrain);

        this.lookAheadDist = lookAheadDist;
        this.startPoint = pt1;
        this.endPoint = pt2;
        this.pathSegment = new Line(pt1, pt2);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}