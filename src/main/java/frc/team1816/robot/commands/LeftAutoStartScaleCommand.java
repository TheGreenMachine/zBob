package frc.team1816.robot.commands;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1816.robot.Robot;

public class LeftAutoStartScaleCommand extends CommandGroup {
    char target;

    public LeftAutoStartScaleCommand() {

    }

    public void selectAuto() {
        try {
            target = DriverStation.getInstance().getGameSpecificMessage().charAt(0);

            System.out.println("Scale Auto Selection: " + target);
        } catch (Exception e) {
            System.out.println("NO TARGET");
            target = 'n';
        }

        if (target == 'L') {
            System.out.println("Scale ---- Target: L");

            //Cube One
            addParallel(new RaiseElevatorCommand());
            addSequential(new DriveXInchesCommand(285,0.8));

            addSequential(new WaitCommand(0.1));
            addSequential(new RotateXDegreesCommand(45,true,0.6),3);
            addSequential(new WaitCommand(0.1));

            addSequential(new ToggleCollectorCommand(true));
            addSequential(new SetCollectorSpeedCommand(-1));
            addSequential(new WaitCommand(1));
            addSequential(new DriveXInchesCommand(13,-0.6),5);
            addParallel(new LowerElevatorCommand());
            addSequential(new SetCollectorSpeedCommand(0));
            addSequential(new WaitCommand(0.2));

            //Turn and collect second cube
            addSequential(new RotateXDegreesCommand(115,true,0.6),3);
            addParallel(new SetCollectorSpeedCommand(1));
            addSequential(new DriveXInchesCommand(72,0.6,0.4,0.2),4);
            addSequential(new WaitCommand(1));

            //Turn and place cube on scale
            addSequential(new DriveXInchesCommand(70,-0.8,0.6,0.2));
            addParallel(new RaiseElevatorCommand());
            addSequential(new RotateXDegreesCommand(-120,true,0.4),3); // why cant we turn left
//            addSequential(new RotateXDegreesCommand(250, true,0.5),3); //maybe we can turn left now
            addSequential(new SetCollectorSpeedCommand(0));
            addSequential(new DriveXInchesCommand(12,0.8));
            addSequential(new WaitCommand(1));
            addSequential(new SetCollectorSpeedCommand(-1));
            addSequential(new WaitCommand(0.5));
            addSequential(new SetCollectorSpeedCommand(0));

        } else if (target == 'R') {
            System.out.println("Scale ---- Target: R");

            addSequential(new DriveXInchesCommand(225,0.8));
            addSequential(new DriveXInchesCommand(12,0.8));

            addSequential(new WaitCommand(0.2));
            addSequential(new RotateXDegreesCommand(90,true,0.7),3);
            addSequential(new DriveXInchesCommand(180, 0.8));
            addParallel(new RaiseElevatorCommand());
            addSequential(new RotateXDegreesCommand(-90,true,0.6),3);
            addSequential(new DriveXInchesCommand(20,0.6),5);

            addSequential(new WaitCommand(3));
            addSequential(new ToggleCollectorCommand(true));
            addSequential(new SetCollectorSpeedCommand(-1));
            addSequential(new WaitCommand(1));
            addSequential(new LowerElevatorCommand());
            addSequential(new SetCollectorSpeedCommand(0));
            addSequential(new WaitCommand(0.2));

            addSequential(new DriveXInchesCommand(12, -0.5));

        } else {
            System.out.println("Scale ---- Auto-Run");
            addSequential(new DriveXInchesCommand(140,0.5));
        }
    }
}
