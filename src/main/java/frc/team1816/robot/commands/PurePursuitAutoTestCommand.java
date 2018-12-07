package frc.team1816.robot.commands;

import com.edinarobotics.purepursuit.PPPoint;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;


public class PurePursuitAutoTestCommand extends CommandGroup {
    private Drivetrain drivetrain;
    private double initHeading;

    private PPPoint pt1 = new PPPoint(0,0);
    private PPPoint pt2 = new PPPoint(0, 24);
    private PPPoint pt3 = new PPPoint(36, 36);
    private PPPoint pt4 = new PPPoint(60, 60);

    public PurePursuitAutoTestCommand() {
        drivetrain = Components.getInstance().drivetrain;
        initHeading = drivetrain.getGyroAngle();
        
         addSequential(new PurePursuitLineCommand(pt1 ,pt2, 4, 0.25, initHeading));
        addSequential(new PurePursuitLineCommand(pt2, pt4, 8, 0.25, initHeading));
        // addSequential(new PurePursuitEndpointCommand(pt3, pt4, 4, 0.25, initHeading));

        System.out.println("Test Command End");
    }
}
