package frc.team1816.robot.commands;

import com.edinarobotics.purepursuit.*;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.Robot;
import frc.team1816.robot.subsystems.Drivetrain;

public class PurePursuitLineCommand extends Command {
    private Drivetrain drivetrain;
    private PurePursuitCalc calc;
    private PurePursuitAutoTestCommand parent;

    private double currXPos, currYPos, currHeading, initHeading;

    public PurePursuitLineCommand(PPPoint pt1, PPPoint pt2, double lookAheadDist, double targetVelocity, PurePursuitAutoTestCommand parent) {
        super("purepursuitlinecommand");
        drivetrain = Components.getInstance().drivetrain;
        this.parent = parent;

        calc = new PurePursuitCalc(pt1, pt2, lookAheadDist, targetVelocity);

        requires(drivetrain);
    }

    @Override
    protected void initialize() {
        System.out.println("Pure Pursuit Drive Init");

        currXPos = drivetrain.getXPos();
        currYPos = drivetrain.getYPos();
        initHeading = parent.getInitHeading();
        Robot.PPLog.log(calc.getDataHeader());
    }

    @Override
    protected void execute() {
        currXPos = drivetrain.getXPos();
        currYPos = drivetrain.getYPos();
        currHeading = 90 - (drivetrain.getGyroAngle() - initHeading); //correct robot heading so 90° is forwards

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