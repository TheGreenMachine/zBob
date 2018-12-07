package frc.team1816.robot.commands;

import com.edinarobotics.purepursuit.*;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.Robot;
import frc.team1816.robot.subsystems.Drivetrain;

public class PurePursuitLineCommand extends Command {
    private Drivetrain drivetrain;
    private PurePursuitCalc calc;

    private double currXPos, currYPos, currHeading, initHeading;

    public PurePursuitLineCommand(PPPoint pt1, PPPoint pt2, double lookAheadDist, double targetVelocity, double initHeading) {
        super("purepursuitlinecommand");
        drivetrain = Components.getInstance().drivetrain;

        calc = new PurePursuitCalc(pt1, pt2, lookAheadDist, targetVelocity);

        this.initHeading = initHeading;

        requires(drivetrain);
    }

    @Override
    protected void initialize() {
        System.out.println("Pure Pursuit Drive Init");

        currXPos = drivetrain.getXPos();
        currYPos = drivetrain.getYPos();
        currHeading = drivetrain.getGyroAngle() - initHeading;
        Robot.PPLog.log(calc.getDataHeader());
    }

    @Override
    protected void execute() {
        currXPos = drivetrain.getXPos();
        currYPos = drivetrain.getYPos();
        currHeading = drivetrain.getGyroAngle() - initHeading;

        double[] velocities = calc.calcVelocities(currXPos, currYPos, currHeading);

        logData();
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
        end();
    }

    public void logData() {
        double [] data = calc.getData();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < data.length; i++) {
            builder.append(data[i]);
            builder.append(",");
        }

        Robot.PPLog.log(builder.toString());
    }

}
