package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ModdedScaleAutoCommand extends CommandGroup {
    char target;
    char startPos;
    double secondsToWaitNear = 0;
    double secondsToWaitFar = 0;
    double distanceFromWall = 12;
    double runVelocity = 1;

    public ModdedScaleAutoCommand() {

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
                System.out.println("LStart Scale neoAvoidance ---- Target: L");

                addSequential(new DriveXInchesCommand(306, 0.7));
                addSequential(new WaitCommand(0.2));
                addSequential(new RotateXDegreesCommand(90, true, 0.2), 5);
                addSequential(new WaitCommand(0.1));

                addParallel(new LowerCollectorClawCommand(false,0.3));
                addSequential(new DriveXInchesCommand(12, -.5),2);
                addSequential(new RaiseElevatorCommand());

                addSequential(new SetCollectorSpeedCommand(1));
                addSequential(new WaitCommand(1));
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new WaitCommand(0.2));

            } else if (target == 'R') {
                System.out.println("LModScale ---- Target: R");

                addParallel(new LowerCollectorClawCommand(false,2));
                addParallel(new SetElevatorHeightPercentCommand(20),2);

                addSequential(new DriveXInchesCommand(220, 0.8));
                addSequential(new RotateXDegreesCommand(90,true,.3),3);
                addSequential(new DriveXInchesCommand(200, 0.7,0.4,0.2));
                addSequential(new RotateXDegreesCommand(-90,true,.32),3);
                addSequential(new RaiseElevatorCommand());
                addSequential(new DriveXInchesCommand(45,0.8),3);

                addSequential(new SetCollectorSpeedCommand(0.4));
                addSequential(new WaitCommand(1));
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new WaitCommand(0.2));

                addSequential(new DriveXInchesCommand(12, -0.7));

            } else {
                System.out.println("Avoidance ---- Auto-Run");
                addSequential(new DriveXInchesCommand(140,0.5));
            }
        } else if (startPos == 'R') {
            if(target == 'R') {
                System.out.println("RStart Scale neoAvoidance ---- Target: R");

                addSequential(new DriveXInchesCommand(306, 0.7));
                addSequential(new WaitCommand(0.2));
                addSequential(new RotateXDegreesCommand(-90, true, 0.2), 5);
                addSequential(new WaitCommand(0.1));

                addParallel(new LowerCollectorClawCommand(false,0.3));
                addSequential(new DriveXInchesCommand(12, -.5),2);
                addSequential(new RaiseElevatorCommand());

                addSequential(new SetCollectorSpeedCommand(1));
                addSequential(new WaitCommand(1));
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new WaitCommand(0.2));

            } else if (target == 'L') {
                System.out.println("RModScale ---- Target: L");

                addParallel(new LowerCollectorClawCommand(false,2));
                addParallel(new SetElevatorHeightPercentCommand(20),2);

                addSequential(new DriveXInchesCommand(220, 0.8));
                addSequential(new RotateXDegreesCommand(-90,true,.3),3);
                addSequential(new DriveXInchesCommand(200, 0.7,0.4,0.2));
                addSequential(new RotateXDegreesCommand(90,true,.32),3);
                addSequential(new RaiseElevatorCommand());
                addSequential(new DriveXInchesCommand(45,0.8),3);

                addSequential(new SetCollectorSpeedCommand(0.4));
                addSequential(new WaitCommand(1));
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new WaitCommand(0.2));

                addSequential(new DriveXInchesCommand(12, -0.7));

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
