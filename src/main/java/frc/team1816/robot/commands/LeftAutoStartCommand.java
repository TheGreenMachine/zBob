package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1816.robot.Robot;

public class LeftAutoStartCommand extends CommandGroup {

    public LeftAutoStartCommand() {
        char target;

        try {
            target = DriverStation.getInstance().getGameSpecificMessage().charAt(0);
        } catch (Exception e) {
            System.out.println("NO TARGET!");
            target = 'n';
        }

        System.out.println("Selection: " + target);

        if(target == 'L') {
            System.out.println("Left Start Auto ---- Target: L");
            addSequential(new DriveXInchesCommand(147, 0.3, false));
            addSequential(new RotateXDegreesCommand(90));
            addSequential(new DriveXInchesCommand(12, 0.3, true));
            addSequential(new WaitCommand(1));
            addSequential(new DriveXInchesCommand(12, -0.3, false));
            addSequential(new RotateXDegreesCommand(-90));
            addSequential(new DriveXInchesCommand(24, 0.3, false));
            Robot.logger.close();
        } else if (target == 'R'){
            System.out.println("Left Start Auto ---- Target: R");
            addSequential(new DriveXInchesCommand(260, 0.3, false));
            addSequential(new RotateXDegreesCommand(90));
            addSequential(new DriveXInchesCommand(125, 0.3, false));
            addSequential(new RotateXDegreesCommand(90));
            addSequential(new DriveXInchesCommand(10, 0.1, false));
        } else {
            addSequential(new DriveXInchesCommand(24,0.25, false));

        }
    }
}
