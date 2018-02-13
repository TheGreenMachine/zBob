package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1816.robot.Robot;

public class LeftAutoStartCommand extends CommandGroup {
    char target;

    public LeftAutoStartCommand() {

    }

    public void selectAuto() {
        try {
            target = DriverStation.getInstance().getGameSpecificMessage().charAt(0);
            System.out.println("Switch Auto Selection: " + target);
        } catch (Exception e) {
            System.out.println("NO TARGET!");
            target = 'n';
        }

        if(target == 'L') {
            System.out.println("Left Start Auto ---- Target: L");
            addSequential(new DriveXInchesCommand(140, 0.8));
            addSequential(new RotateXDegreesCommand(90));
            addSequential(new DriveXInchesCommand(10, 0.8));

            addSequential(new WaitCommand(1));

//            addSequential(new WaitCommand(0.5));
//            addSequential(new RaiseElevatorCommand());
//            addSequential(new WaitCommand(1));
//            addSequential(new LowerElevatorCommand());
//            addSequential(new WaitCommand(0.5));

            addSequential(new DriveXInchesCommand(12, -0.5));
            addSequential(new RotateXDegreesCommand(-90));
            addSequential(new DriveXInchesCommand(24, 0.5));

//            System.out.println("Experimental Arc Auto");
//            addSequential(new DriveXInchesCommand(60, 0.5, false));
//            addSequential(new ArcDriveCommand(48, 0.4, 90));
//            addSequential(new DriveXInchesCommand(12,0.5,false));

        } else if (target == 'R'){

            System.out.println("Left Start Auto ---- Target: R");
            addSequential(new DriveXInchesCommand(185, 0.8));
            addSequential(new RotateXDegreesCommand(90));
            addSequential(new DriveXInchesCommand(160, 0.8));
            addSequential(new RotateXDegreesCommand(90));
            addSequential(new DriveXInchesCommand(20, 0.5));
            addSequential(new RaiseElevatorCommand());
            addSequential(new WaitCommand(1));
            addSequential(new LowerElevatorCommand());
            addSequential(new DriveXInchesCommand(20, -.5));

//            System.out.println("Experimental Gyro-Based Arc Auto");
//            addSequential(new DriveXInchesCommand(24,0.6,0.4,0.4));
//            addSequential(new ArcDriveCommand(48,0.4,90));
//            addSequential(new DriveXInchesCommand(24,0.6,0.4,0.4));
        } else {
            System.out.println("Left Start Auto ---- Auto-Run");
            addSequential(new DriveXInchesCommand(140,0.5));
        }
    }
}
