package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightAutoStartCommand extends CommandGroup{
    char target;

    public RightAutoStartCommand() {

        try {
            target = DriverStation.getInstance().getGameSpecificMessage().charAt(0);
        } catch (Exception e) {
            System.out.println("NO TARGET!");
            target = 'n';
        }
        if(target == 'R') {
            System.out.println("Right Start Auto ---- Target: R");
            addSequential(new DriveXInchesCommand(140, 0.8, false));
            addSequential(new RotateXDegreesCommand(-90));
            addSequential(new DriveXInchesCommand(10, 0.5, false));
            addSequential(new RotateXDegreesCommand(90));
            addSequential(new DriveXInchesCommand(12, 0.5, false));
        } else if (target == 'L'){
            System.out.println("Right Start Auto ---- Target: L");
            addSequential(new DriveXInchesCommand(260, 0.8, false));
            addSequential(new RotateXDegreesCommand(-90));
            addSequential(new DriveXInchesCommand(125, 0.8, false));
            addSequential(new RotateXDegreesCommand(-90));
            addSequential(new DriveXInchesCommand(10, 0.5, false));
        } else {
            addSequential(new DriveXInchesCommand(140,0.8, false));
        }
    }
}
