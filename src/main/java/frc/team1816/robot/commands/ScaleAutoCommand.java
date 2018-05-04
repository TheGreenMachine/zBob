package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ScaleAutoCommand extends CommandGroup {
    char target;
    char startPos;

    public ScaleAutoCommand() {

    }

    public void selectAuto(String data, String pos) {
        try {
            target = data.charAt(1);
            startPos = pos.charAt(0);

            System.out.println("Scale Auto Selection: " + target);
        } catch (Exception e) {
            System.out.println("NO TARGET!");
            target = 'n';
        }

        if(startPos == 'L') {
            if (target == 'L') {
                System.out.println("LStart Scale ---- Target: L");

                addParallel(new LowerCollectorClawCommand(false,2));

                //Cube One
                addParallel(new RaiseElevatorCommand(1));
                addSequential(new DriveXInchesCommand(266, 0.8));
                addSequential(new WaitCommand(0.1));
                addSequential(new RotateXDegreesCommand(45, true, 0.25), 5);
                addSequential(new DriveXInchesCommand(12,0.6),2);

                addSequential(new SetCollectorSpeedCommand(1));
                addSequential(new WaitCommand(1));
                addSequential(new DriveXInchesCommand(13, -0.6), 5);
                addParallel(new LowerElevatorCommand());
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new WaitCommand(0.2));

                //Turn and collect second cube
                addSequential(new RotateXDegreesCommand(100, true, 0.25), 5);
                addParallel(new SetCollectorSpeedCommand(-1));
                addSequential(new DriveXInchesCommand(68, 0.8, 0.4, 0.2), 2);
                addSequential(new SetCollectorSpeedCommand(0));

                //Turn and place cube on scale
                addSequential(new DriveXInchesCommand(70, -0.7, 0.6, 0.2));
                addParallel(new SetElevatorHeightPercentCommand(100),2);
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new RotateXDegreesCommand(-100, true, 0.25), 5);
                addSequential(new DriveXInchesCommand(12, 0.8),1);
                addSequential(new SetCollectorSpeedCommand(1));
                addSequential(new WaitCommand(1));
                addSequential(new SetCollectorSpeedCommand(0));
            } else if (target == 'R') {
                System.out.println("LStart Scale ---- Target: R");

                addParallel(new LowerCollectorClawCommand(false,2));
                addParallel(new SetElevatorHeightPercentCommand(20),2);

                addSequential(new DriveXInchesCommand(220, 0.8));
                addSequential(new RotateXDegreesCommand(90,true,.3),3);
                addSequential(new DriveXInchesCommand(200, 0.7,0.4,0.2));
                addSequential(new RotateXDegreesCommand(-90,true,.32),3);
                addSequential(new RaiseElevatorCommand());
                addSequential(new DriveXInchesCommand(48,0.8),3);

                addSequential(new SetCollectorSpeedCommand(1));
                addSequential(new WaitCommand(1));
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new WaitCommand(0.2));

                addSequential(new DriveXInchesCommand(12, -0.7));

            } else {
                System.out.println("Scale ---- Auto-Run");
                addSequential(new DriveXInchesCommand(140,0.5));
            }
        } else if (startPos == 'R') {
            if (target == 'R') {
                System.out.println("RScale ---- Target: R");

                addParallel(new LowerCollectorClawCommand(false,2));

                //Cube One
                addParallel(new RaiseElevatorCommand(1));
                addSequential(new DriveXInchesCommand(266, 0.8));
                addSequential(new WaitCommand(0.1));
                addSequential(new RotateXDegreesCommand(-45, true, 0.25), 5);
                addSequential(new DriveXInchesCommand(12,0.6),2);

                addSequential(new SetCollectorSpeedCommand(1));
                addSequential(new WaitCommand(1));
                addSequential(new DriveXInchesCommand(13, -0.6), 5);
                addParallel(new LowerElevatorCommand());
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new WaitCommand(0.2));

                //Turn and collect second cube
                addSequential(new RotateXDegreesCommand(-100, true, 0.25), 5);
                addParallel(new SetCollectorSpeedCommand(-1));
                addSequential(new DriveXInchesCommand(68, 0.8, 0.4, 0.2), 2);
                addSequential(new SetCollectorSpeedCommand(0));

                //Turn and place cube on scale
                addSequential(new DriveXInchesCommand(70, -0.7, 0.6, 0.2));
                addParallel(new SetElevatorHeightPercentCommand(100),2);
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new RotateXDegreesCommand(100, true, 0.25), 5);
                addSequential(new DriveXInchesCommand(12, 0.8),1);
                addSequential(new SetCollectorSpeedCommand(1));
                addSequential(new WaitCommand(1));
                addSequential(new SetCollectorSpeedCommand(0));
            } else if (target == 'L') {
                System.out.println("RScale ---- Target: R");

                addParallel(new LowerCollectorClawCommand(false,2));
                addParallel(new SetElevatorHeightPercentCommand(20),2);

                addSequential(new DriveXInchesCommand(220, 0.8));
                addSequential(new RotateXDegreesCommand(-90,true,.32),3);
                addSequential(new DriveXInchesCommand(200, 0.7,0.4,0.2));
                addSequential(new RotateXDegreesCommand(90,true,.32),3);
                addSequential(new RaiseElevatorCommand());
                addSequential(new DriveXInchesCommand(48,0.8),3);

                addSequential(new SetCollectorSpeedCommand(1));
                addSequential(new WaitCommand(1));
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new WaitCommand(0.2));

                addSequential(new DriveXInchesCommand(12, -0.7));

            } else {
                System.out.println("Scale ---- Auto-Run");
                addSequential(new DriveXInchesCommand(140,0.5));
            }
        } else {
            System.out.println("Auto-Run");
            addSequential(new DriveXInchesCommand(140,0.5));
        }
    }
}
