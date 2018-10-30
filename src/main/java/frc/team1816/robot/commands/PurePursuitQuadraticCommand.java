package frc.team1816.robot.commands;

import com.edinarobotics.purepursuit.commands.Line;
import com.edinarobotics.purepursuit.commands.Point;
import com.edinarobotics.purepursuit.commands.PurePursuitCalc;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;

public class PurePursuitQuadraticCommand extends Command {
    private Drivetrain drivetrain;

    private PurePursuitCalc calc;

    private double currXPos, currYPos, currHeading;

    public PurePursuitQuadraticCommand(Point pt1, Point pt2, double lookAheadDist, double targetVelocity) {
        drivetrain = Components.getInstance().drivetrain;
        requires(drivetrain);

        calc = new PurePursuitCalc(pt1, pt2, lookAheadDist, targetVelocity);
    }

    @Override
    protected void initialize() {
        System.out.println("Pure Pursuit Initialized");

        currXPos = drivetrain.getXPos();
        currYPos = drivetrain.getYPos();
        currHeading = drivetrain.getGyroAngle();
    }

    @Override
    protected void execute() {
        currXPos = drivetrain.getXPos();
        currYPos = drivetrain.getYPos();
        currHeading = drivetrain.getGyroAngle();

        double[] velocities = calc.calcVelocities(currXPos, currYPos, currHeading);

        drivetrain.setDrivetrain(velocities[0], velocities[1]);
    }

    @Override
    protected void end() {
        System.out.println("Pure Pursuit finished");
        drivetrain.setDrivetrain(0, 0);
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected boolean isFinished() {
        return calc.endPath();
    }
}