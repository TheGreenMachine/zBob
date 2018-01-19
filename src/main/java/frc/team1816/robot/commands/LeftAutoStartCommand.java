package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftAutoStartCommand extends CommandGroup {

    char target;

    public LeftAutoStartCommand() {

        target = DriverStation.getInstance().getGameSpecificMessage().charAt(0);

        if(target == 'L') {
            System.out.println("Left Start Auto ---- Target: L");
            addSequential(new DriveXInchesCommand(140, 0.8));
            addSequential(new RotateXDegreesCommand(90));
            addSequential(new DriveXInchesCommand(10, 0.5));
        } else {
            System.out.println("Left Start Auto ---- Target: R");
            addSequential(new DriveXInchesCommand(200, 0.8));
            addSequential(new RotateXDegreesCommand(90));
            addSequential(new DriveXInchesCommand(130, 0.8));
            addSequential(new RotateXDegreesCommand(90));
            addSequential(new DriveXInchesCommand(10, 0.5));
        }
    }
}
