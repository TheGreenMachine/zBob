package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightAutoStartScaleCommand extends CommandGroup {
    char target;

    public RightAutoStartScaleCommand() {

    }

    public void selectAuto(String data) {
        try {
            target = data.charAt(1);

            System.out.println("Scale Auto Selection: " + target);
        } catch (Exception e) {
            System.out.println("NO TARGET");
            target = 'n';
        }

        if (target == 'R') {
                System.out.println("2 Cube Scale ---- Target: R");

//                addParallel(new LowerCollectorClawCommand(false,1.8));

                //Cube One
                addParallel(new RaiseElevatorCommand(1));
                addSequential(new DriveXInchesCommand(248, 0.7));
                addSequential(new WaitCommand(0.1));
                addSequential(new RotateXDegreesCommand(-45, true, 0.32), 3);
                addSequential(new WaitCommand(0.1));

                addSequential(new SetCollectorSpeedCommand(1));
                addSequential(new WaitCommand(1));
                addSequential(new DriveXInchesCommand(13, -0.6), 5);
                addParallel(new LowerElevatorCommand());
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new WaitCommand(0.2));

                //Turn and collect second cube
                addSequential(new RotateXDegreesCommand(-90, true, 0.32), 3);
//                addParallel(new SetCollectorSpeedCommand(-1));
//                addSequential(new DriveXInchesCommand(68, 0.7, 0.4, 0.2), 4);
//                addSequential(new SetCollectorSpeedCommand(0));
//                addSequential(new WaitCommand(0.5));
//
//                //Turn and place cube on scale
//                addSequential(new DriveXInchesCommand(70, -0.7, 0.6, 0.2));
//                addParallel(new SetElevatorHeightPercentCommand(100),2);
//                addSequential(new SetCollectorSpeedCommand(0));
//                addSequential(new RotateXDegreesCommand(90, true, 0.3), 3); // why cant we turn left
//                addSequential(new DriveXInchesCommand(12, 0.8));
//                addSequential(new SetCollectorSpeedCommand(1));
//            }
        } else if (target == 'L') {
            System.out.println("Scale ---- Target: R");

//            addParallel(new LowerCollectorClawCommand(false,1.8));
            addParallel(new SetElevatorHeightPercentCommand(20),2);

            addSequential(new DriveXInchesCommand(213, 0.7));
            addSequential(new RotateXDegreesCommand(-90,true,.32),3);
            addSequential(new DriveXInchesCommand(208, 0.7,0.4,0.2));
            addSequential(new RotateXDegreesCommand(90,true,.32),3);
            addParallel(new RaiseElevatorCommand());
            addSequential(new DriveXInchesCommand(52,0.8),5);

            addSequential(new WaitCommand(1));
            addSequential(new SetCollectorSpeedCommand(1));
            addSequential(new WaitCommand(1));
            addSequential(new SetCollectorSpeedCommand(0));
            addSequential(new WaitCommand(0.2));

            addSequential(new DriveXInchesCommand(12, -0.7));

        } else {
            System.out.println("Scale ---- Auto-Run");
            addSequential(new DriveXInchesCommand(140,0.5));
        }
    }
}
