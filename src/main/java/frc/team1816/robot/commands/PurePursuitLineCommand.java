package frc.team1816.robot.commands;

import com.edinarobotics.purepursuit.*;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;

//todo: write PP-endpoint command class

public class PurePursuitLineCommand extends Command {
    private Drivetrain drivetrain;

    private PurePursuitCalc calc;

    private double currXPos, currYPos, currHeading;

    public PurePursuitLineCommand(PPPoint pt1, PPPoint pt2, double lookAheadDist, double targetVelocity) {
        super("purepursuitlinecommand");
        drivetrain = Components.getInstance().drivetrain;

        calc = new PurePursuitCalc(pt1, pt2, lookAheadDist, targetVelocity);

        requires(drivetrain);
    }

    @Override
    protected void initialize() {
        System.out.println("Pure Pursuit Drive Init");

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
    protected boolean isFinished() {
        return calc.endPath();
    }

    @Override
    protected void end() {
        System.out.println("PP Command End");
        drivetrain.setDrivetrain(0,0);
    }

    @Override
    protected void interrupted() {
        super.interrupted();
    }

}
