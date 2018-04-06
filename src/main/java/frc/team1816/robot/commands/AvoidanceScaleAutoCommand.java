package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AvoidanceScaleAutoCommand extends CommandGroup {
    char target;
    char startPos;
    double secondsToWait = 0;

    public AvoidanceScaleAutoCommand() {

    }

    public void selectAuto(String data, String pos, double wait) {

        this.secondsToWait = wait;

        try {
            target = data.charAt(1);
            startPos = pos.charAt(0);

            System.out.println("Scale Auto Selection: " + target);
        } catch (Exception e) {
            System.out.println("NO TARGET!");
            target = 'n';
        }

        if(startPos == 'L') {
            if(target == 'L') {
                System.out.println("LStart Scale Avoidance ---- Target: L");

                addSequential(new DriveXInchesCommand(6,0.5),2);
                addSequential(new RotateXDegreesCommand(-45,true,0.2),2);
                addSequential(new DriveXInchesCommand(24,0.5),2);
                addSequential(new RotateXDegreesCommand(45,true,0.2),2);

                addParallel(new SetElevatorHeightPercentCommand(0.6,0.5),3);
                addSequential(new DriveXInchesCommand(240,0.8),5); //todo tune split distance, total = 280 in
                addSequential(new WaitCommand(secondsToWait));
                addSequential(new DriveXInchesCommand(40,0.7),3);

                addSequential(new RaiseElevatorCommand(),3);
                addSequential(new RotateXDegreesCommand(90,true,0.2),3);
                addSequential(new DriveXInchesCommand(10,0.6),3);

                addSequential(new SetCollectorSpeedCommand(1));
                addSequential(new WaitCommand(1));
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new DriveXInchesCommand(-12,0.5),2);

            } else if (target == 'R'){
                System.out.println("LStart Scale Avoidance ---- Target: R");

                addSequential(new DriveXInchesCommand(6,0.5),2);
                addSequential(new RotateXDegreesCommand(90,true,0.2),2);
                addSequential(new DriveXInchesCommand(285,0.8),5);

                addSequential(new RotateXDegreesCommand(-45,true,0.2),2);
                addSequential(new DriveXInchesCommand(24,0.5),2);
                addSequential(new RotateXDegreesCommand(-45,true,0.2),2);

                addParallel(new SetElevatorHeightPercentCommand(0.6,0.5),3);
                addSequential(new DriveXInchesCommand(240,0.8),5); //todo tune split distance, total = 280 in
                addSequential(new WaitCommand(secondsToWait));
                addSequential(new DriveXInchesCommand(40,0.7),3);

                addSequential(new RaiseElevatorCommand(),3);
                addSequential(new RotateXDegreesCommand(90,true,0.2),3);
                addSequential(new DriveXInchesCommand(10,0.6),3);

                addSequential(new SetCollectorSpeedCommand(1));
                addSequential(new WaitCommand(1));
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new DriveXInchesCommand(-12,0.5),2);

            } else {
                System.out.println("Avoidance ---- Auto-Run");
                addSequential(new DriveXInchesCommand(140,0.5));
            }
        } else if (startPos == 'R') {
            if(target == 'R') {
                System.out.println("RStart Scale Avoidance ---- Target: R");

                addSequential(new DriveXInchesCommand(6,0.5),2);
                addSequential(new RotateXDegreesCommand(45,true,0.2),2);
                addSequential(new DriveXInchesCommand(24,0.5),2);
                addSequential(new RotateXDegreesCommand(-45,true,0.2),2);

                addParallel(new SetElevatorHeightPercentCommand(0.6,0.5),3);
                addSequential(new DriveXInchesCommand(240,0.8),5); //todo tune split distance, total = 280 in
                addSequential(new WaitCommand(secondsToWait));
                addSequential(new DriveXInchesCommand(40,0.7),3);

                addSequential(new RaiseElevatorCommand(),3);
                addSequential(new RotateXDegreesCommand(-90,true,0.2),3);
                addSequential(new DriveXInchesCommand(10,0.6),3);

                addSequential(new SetCollectorSpeedCommand(1));
                addSequential(new WaitCommand(1));
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new DriveXInchesCommand(-12,0.5),2);

            } else if (target == 'L'){
                System.out.println("RStart Scale Avoidance ---- Target: L");

                addSequential(new DriveXInchesCommand(6,0.5),2);
                addSequential(new RotateXDegreesCommand(-90,true,0.2),2);
                addSequential(new DriveXInchesCommand(285,0.8),5);

                addSequential(new RotateXDegreesCommand(45,true,0.2),2);
                addSequential(new DriveXInchesCommand(24,0.5),2);
                addSequential(new RotateXDegreesCommand(45,true,0.2),2);

                addParallel(new SetElevatorHeightPercentCommand(0.6,0.5),3);
                addSequential(new DriveXInchesCommand(240,0.8),5); //todo tune split distance, total = 280 in
                addSequential(new WaitCommand(secondsToWait));
                addSequential(new DriveXInchesCommand(40,0.7),3);

                addSequential(new RaiseElevatorCommand(),3);
                addSequential(new RotateXDegreesCommand(90,true,0.2),3);
                addSequential(new DriveXInchesCommand(10,0.6),3);

                addSequential(new SetCollectorSpeedCommand(1));
                addSequential(new WaitCommand(1));
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new DriveXInchesCommand(-12,0.5),2);
            } else {
                System.out.println("Avoidance ---- Auto-Run");
                addSequential(new DriveXInchesCommand(140,0.5));
            }
        } else {
            System.out.println("Auto-Run");
            addSequential(new DriveXInchesCommand(140,0.5));
        }
    }
}
