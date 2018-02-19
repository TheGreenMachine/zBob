package frc.team1816.robot.commands;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1816.robot.Robot;

public class RightAutoStartScaleCommand extends CommandGroup {
    char target;

    public RightAutoStartScaleCommand() {

    }

    public void selectAuto() {
        try {
            target = DriverStation.getInstance().getGameSpecificMessage().charAt(0);

            System.out.println("Scale Auto Selection: " + target);
        } catch (Exception e) {
            System.out.println("NO TARGET");
            target = 'n';
        }

        if (target == 'R') {
            System.out.println("Scale ---- Target: R");

            addParallel(new RaiseElevatorCommand());
            addSequential(new DriveXInchesCommand(285,0.8));
//            addSequential(new RaiseElevatorCommand());

            addSequential(new WaitCommand(0.1));
            addSequential(new RotateXDegreesCommand(-45,true,0.5),2);
            addSequential(new WaitCommand(0.1));

            addSequential(new ToggleCollectorCommand(true));
            addSequential(new SetCollectorSpeedCommand(-1));
            addSequential(new WaitCommand(1));
            addSequential(new DriveXInchesCommand(4,-0.6),5);
            addSequential(new LowerElevatorCommand());
            addSequential(new SetCollectorSpeedCommand(0));
            addSequential(new WaitCommand(0.2));
        } else if (target == 'L') {
            System.out.println("Scale ---- Target: L");

            addSequential(new DriveXInchesCommand(225,0.8));

            addParallel(new SetElevatorHeightPercentCommand(50));

            addSequential(new WaitCommand(0.2));
            addSequential(new RotateXDegreesCommand(-90,true,0.6),3);
            addSequential(new WaitCommand(0.2));
            addSequential(new DriveXInchesCommand(200, 0.8));
            addSequential(new WaitCommand(0.2));
            addSequential(new RotateXDegreesCommand(90,true,0.6),3);
            addSequential(new WaitCommand(0.2));
            addSequential(new DriveXInchesCommand(20,0.6),5);

            addSequential(new RaiseElevatorCommand());
            addSequential(new ToggleCollectorCommand(true));
            addSequential(new SetCollectorSpeedCommand(-1));
            addSequential(new WaitCommand(1));
            addSequential(new SetCollectorSpeedCommand(0));
            addSequential(new LowerElevatorCommand());
            addSequential(new WaitCommand(0.2));

            addSequential(new DriveXInchesCommand(12, -0.5));

        } else {
            System.out.println("Scale ---- Auto-Run");
            addSequential(new DriveXInchesCommand(140,0.5));
        }
    }
}