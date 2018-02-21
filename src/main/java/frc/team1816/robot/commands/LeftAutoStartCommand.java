package frc.team1816.robot.commands;

import edu.wpi.first.networktables.NetworkTableInstance;
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
            System.out.println("Switch ---- Target: L");

            addParallel(new SetElevatorHeightPercentCommand(40));

            addSequential(new DriveXInchesCommand(128, 0.8));
            addSequential(new RotateXDegreesCommand(90, true),3);
            addSequential(new DriveXInchesCommand(4, 0.5),3);

            addSequential(new WaitCommand(0.2));
            addSequential(new ToggleCollectorCommand(true));
            addSequential(new SetCollectorSpeedCommand(-1));
            addSequential(new WaitCommand(1));
            addSequential(new DriveXInchesCommand(4, -0.5),3);
            addSequential(new SetCollectorSpeedCommand(0));
            addSequential(new LowerElevatorCommand());
            addSequential(new WaitCommand(0.2));

            addSequential(new DriveXInchesCommand(30, -0.5));
            addSequential(new RotateXDegreesCommand(90),3);
            addSequential(new DriveXInchesCommand(24, 0.5));

        } else if (target == 'R'){
            System.out.println("Switch ---- Target: R");

            addParallel(new SetElevatorHeightPercentCommand(20));

            addSequential(new DriveXInchesCommand(210, 0.7));
            addSequential(new RotateXDegreesCommand(90,true,.5),3);
            addSequential(new DriveXInchesCommand(138, 0.7,0.4,0.2));
            addParallel(new SetElevatorHeightPercentCommand(40));
            addSequential(new RotateXDegreesCommand(90,true,.5),3);
            addSequential(new DriveXInchesCommand(9, 0.5),3);

            addSequential(new WaitCommand(0.2));
            addSequential(new ToggleCollectorCommand(true));
            addSequential(new SetCollectorSpeedCommand(-1));
            addSequential(new WaitCommand(1));
            addSequential(new DriveXInchesCommand(4, -0.5),3);
            addSequential(new SetCollectorSpeedCommand(0));
            addSequential(new LowerElevatorCommand());
            addSequential(new WaitCommand(0.2));

            addSequential(new DriveXInchesCommand(20, -.5));

//            System.out.println("Experimental Gyro-Based Arc Auto");
//            addSequential(new DriveXInchesCommand(24,0.6,0.4,0.4));
//            addSequential(new ArcDriveCommand(48,0.4,90));
//            addSequential(new DriveXInchesCommand(24,0.6,0.4,0.4));
        } else {
            System.out.println("Switch ---- Auto-Run");
            addSequential(new DriveXInchesCommand(140,0.5));
        }
    }
}
