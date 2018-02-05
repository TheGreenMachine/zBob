package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightAutoStartCommand extends CommandGroup{
    char target;

    public RightAutoStartCommand() {

    }

    public void selectAutoR() {
        try {
            target = DriverStation.getInstance().getGameSpecificMessage().charAt(0);
        } catch (Exception e) {
            System.out.println("NO TARGET!");
            target = 'n';
        }
        if(target == 'R') {
            System.out.println("Right Start Auto ---- Target: R");
            addSequential(new DriveXInchesCommand(147, 0.3, false));
            addSequential(new RotateXDegreesCommand(-90));
            addSequential(new DriveXInchesCommand(12, 0.3, true));

            addSequential(new WaitCommand(0.5));
            addSequential(new RaiseElevatorCommand());
            addSequential(new WaitCommand(1));
            addSequential(new LowerElevatorCommand());
            addSequential(new WaitCommand(0.5));

            addSequential(new DriveXInchesCommand(12, -0.3, false));
            addSequential(new RotateXDegreesCommand(90));
            addSequential(new DriveXInchesCommand(24, 0.3, false));
        } else if (target == 'L'){
            System.out.println("Right Start Auto ---- Target: L");
            addSequential(new DriveXInchesCommand(260, 0.8, false));
            addSequential(new RotateXDegreesCommand(-90));
            addSequential(new DriveXInchesCommand(125, 0.8, false));
            addSequential(new RotateXDegreesCommand(-90));
            addSequential(new DriveXInchesCommand(10, 0.5, false));
        } else {
            System.out.println("Right Start Auto ---- Auto-Run");
            addSequential(new DriveXInchesCommand(140,0.8, false));
        }
    }
}
