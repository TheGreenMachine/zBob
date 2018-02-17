package frc.team1816.robot.commands;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1816.robot.Robot;

public class RightAutoStartCommand extends CommandGroup{
    char target;

    public RightAutoStartCommand() {

    }

    public void selectAuto() {
        try {
            target = DriverStation.getInstance().getGameSpecificMessage().charAt(0);

//            String gameData = Robot.offSeasonNetworkTable
//                    .getTable("OffseasonFMSInfo")
//                    .getEntry("GameData")
//                    .getString("defaultValue");
//            System.out.println("Game Data: " + gameData);
//
//            target = gameData.charAt(0);

            System.out.println("Switch Auto Selection: " + target);
        } catch (Exception e) {
            System.out.println("NO TARGET!");
            target = 'n';
        }

        if(target == 'R') {
            System.out.println("Switch ---- Target: R");
            addSequential(new DriveXInchesCommand(134, 0.8));
            addSequential(new RotateXDegreesCommand(-90));
            addSequential(new DriveXInchesCommand(8, 0.5));

//            addSequential(new WaitCommand(1));

            addSequential(new WaitCommand(0.2));
            addSequential(new SetElevatorHeightPercentCommand(40));
            addSequential(new ToggleCollectorCommand(true));
            addSequential(new SetCollectorSpeedCommand(1));
            addSequential(new WaitCommand(1));
            addSequential(new LowerElevatorCommand());
            addSequential(new SetCollectorSpeedCommand(0));
            addSequential(new WaitCommand(0.2));

            addSequential(new DriveXInchesCommand(36, -0.5));
            addSequential(new RotateXDegreesCommand(90));
            addSequential(new DriveXInchesCommand(24, 0.5));
        } else if (target == 'L'){
            System.out.println("Switch ---- Target: L");
            addSequential(new DriveXInchesCommand(194, 0.8));
            addSequential(new RotateXDegreesCommand(-90));
            addSequential(new DriveXInchesCommand(114, 0.8));
            addSequential(new RotateXDegreesCommand(-90));
//            addSequential(new DriveXInchesCommand(10, 0.5));

            addSequential(new WaitCommand(0.2));
            addSequential(new SetElevatorHeightPercentCommand(40));
            addSequential(new ToggleCollectorCommand(true));
            addSequential(new SetCollectorSpeedCommand(1));
            addSequential(new WaitCommand(1));
            addSequential(new LowerElevatorCommand());
            addSequential(new SetCollectorSpeedCommand(0));
            addSequential(new WaitCommand(0.2));

            addSequential(new DriveXInchesCommand(20, -.5));
        } else {
            System.out.println("Switch ---- Auto-Run");
            addSequential(new DriveXInchesCommand(140,0.5));
        }
    }
}
