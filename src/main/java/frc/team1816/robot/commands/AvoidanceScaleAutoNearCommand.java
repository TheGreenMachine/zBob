package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AvoidanceScaleAutoNearCommand extends CommandGroup {
    char target;
    char startPos;
    double secondsToWaitNear = 0;
    double secondsToWaitFar = 0;
    double distanceFromWall = 12;
    double runVelocity = 1;

    public AvoidanceScaleAutoNearCommand() {

    }

    public void selectAuto(String data, String pos, double nearWait, double farWait, double distance, double velocity) {

        this.secondsToWaitNear = nearWait;
        this.secondsToWaitFar = farWait;
        this.runVelocity = velocity;

        if(this.distanceFromWall < 12) {
            this.distanceFromWall = 12;
        }
        this.distanceFromWall = distance;

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
                addSequential(new RotateXDegreesCommand(-45,true,0.3),3);
                addSequential(new DriveXInchesCommand(24,0.5),2);
                addSequential(new RotateXDegreesCommand(45,true,0.3),3);

                addParallel(new SetElevatorHeightPercentCommand(0.7,0.7),3);

                if(secondsToWaitNear == 0) {
                    addSequential(new DriveXInchesCommand(300, runVelocity),5);
                } else {
                    addSequential(new DriveXInchesCommand(192, 0.8), 5); //todo tune split distance
                    addSequential(new WaitCommand(secondsToWaitNear));
                    addSequential(new DriveXInchesCommand(108, 0.8), 3);
                }

                addParallel(new LowerCollectorClawCommand(false,0.5));
                addParallel(new RaiseElevatorCommand(),3);
                addSequential(new RotateXDegreesCommand(90,true,0.2),3);
                addSequential(new DriveXInchesCommand(10,0.6),3);

                addSequential(new SetCollectorSpeedCommand(0.3));
                addSequential(new WaitCommand(0.5));
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new DriveXInchesCommand(24 + distanceFromWall,-0.3),2);

            } else {
                System.out.println("Avoidance ---- Auto-Run");
                addSequential(new DriveXInchesCommand(140,0.5));
            }
        } else if (startPos == 'R') {
            if(target == 'R') {
                System.out.println("RStart Scale Avoidance ---- Target: R");

                addSequential(new DriveXInchesCommand(6,0.5),2);
                addSequential(new RotateXDegreesCommand(45,true,0.3),3);
                addSequential(new DriveXInchesCommand(24,0.5),2);
                addSequential(new RotateXDegreesCommand(-45,true,0.3),3);

                addParallel(new SetElevatorHeightPercentCommand(0.7,0.7),3);

                if(secondsToWaitNear == 0) {
                    addSequential(new DriveXInchesCommand(300, runVelocity),5);
                } else {
                    addSequential(new DriveXInchesCommand(192, 0.8), 5); //todo tune split distance
                    addSequential(new WaitCommand(secondsToWaitNear));
                    addSequential(new DriveXInchesCommand(108, 0.8), 3);
                }

                addParallel(new LowerCollectorClawCommand(false,0.5));
                addParallel(new RaiseElevatorCommand(),3);
                addSequential(new RotateXDegreesCommand(-90,true,0.2),3);
                addSequential(new DriveXInchesCommand(10,0.6),3);

                addSequential(new SetCollectorSpeedCommand(0.3));
                addSequential(new WaitCommand(0.5));
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new DriveXInchesCommand(24 + distanceFromWall,-0.3),2);

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
