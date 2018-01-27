package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftAutoStartCommand extends CommandGroup {

    char target;

    public LeftAutoStartCommand() {

        try {
            target = DriverStation.getInstance().getGameSpecificMessage().charAt(0);
        } catch (Exception e) {
            System.out.println("NO TARGET!");
            target = 'n';
        }

        if(target == 'L') {
            System.out.println("Left Start Auto ---- Target: L");
            addSequential(new DriveXInchesCommand(70, 0.3));
            addSequential(new RotateXDegreesCommand(90));
            addSequential(new DriveXInchesCommand(50, 0.3));
            addSequential(new DriveXInchesCommand(50, -0.3));
            addSequential(new RotateXDegreesCommand(-90));
            addSequential(new DriveXInchesCommand(12, 0.3));
        } else if (target == 'R'){
            System.out.println("Left Start Auto ---- Target: R");
            addSequential(new DriveXInchesCommand(260, 0.3));
            addSequential(new RotateXDegreesCommand(90));
            addSequential(new DriveXInchesCommand(125, 0.3));
            addSequential(new RotateXDegreesCommand(90));
            addSequential(new DriveXInchesCommand(10, 0.1));
        } else {
            addSequential(new DriveXInchesCommand(140,0.5));
        }
    }
}
