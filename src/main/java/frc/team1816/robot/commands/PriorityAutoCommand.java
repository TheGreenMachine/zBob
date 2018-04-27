package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class PriorityAutoCommand extends CommandGroup {
    char switchPos, scalePos;
    char startPos;

    public PriorityAutoCommand() {

    }

    public void selectAuto(String data, String pos) {
        try {
            switchPos = data.charAt(0);
            scalePos = data.charAt(1);
            startPos = pos.charAt(0);

        } catch (Exception e) {
            System.out.println("NO TARGET!");
            switchPos = 'n';
            scalePos = 'n';
        }

        if(startPos == 'L') {
            if(switchPos == 'L') {
                System.out.println("LStart Auto-Priority ---- Target: LSwitch");

                addParallel(new LowerCollectorClawCommand(false,2));
                addParallel(new SetElevatorHeightPercentCommand(40,.5),2);

                addSequential(new DriveXInchesCommand(154, 0.5));
                addSequential(new RotateXDegreesCommand(90, true,.29),2);
                addSequential(new WaitCommand(0.5));
                addSequential(new DriveXInchesCommand(18, 0.3),2);

                addSequential(new WaitCommand(0.2));
                addSequential(new SetCollectorSpeedCommand(.5));
                addSequential(new WaitCommand(1));
                addSequential(new DriveXInchesCommand(8, -0.5),3);
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new LowerElevatorCommand());
                addSequential(new WaitCommand(0.2));

                addSequential(new DriveXInchesCommand(12, -0.5));
                addSequential(new RotateXDegreesCommand(-90,true,.2),3);

            } else if(scalePos == 'L') {
                System.out.println("LStart Auto-Priority ---- Target: LScale");

                addParallel(new LowerCollectorClawCommand(false,2));

                //Cube One
                addParallel(new RaiseElevatorCommand(1));
                addSequential(new DriveXInchesCommand(248, 0.6));
                addSequential(new WaitCommand(0.1));
                addSequential(new RotateXDegreesCommand(45, true, 0.25), 3);
                addSequential(new DriveXInchesCommand(8,0.4),2);
                addSequential(new WaitCommand(0.1));

                addSequential(new SetCollectorSpeedCommand(1));
                addSequential(new WaitCommand(1));
                addSequential(new DriveXInchesCommand(13, -0.6), 5);
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new WaitCommand(0.2));

            } else if(switchPos == 'R') {
                System.out.println("LStart Auto-Priority ---- Target: Rswitch");

                addParallel(new LowerCollectorClawCommand(false,2));
                addParallel(new SetElevatorHeightPercentCommand(20,.5),2);

                addSequential(new DriveXInchesCommand(213, 0.7));
                addSequential(new RotateXDegreesCommand(90,true,.32),3);
                addSequential(new DriveXInchesCommand(189, 0.7,0.4,0.2));
                addParallel(new SetElevatorHeightPercentCommand(40,.5),2);
                addSequential(new RotateXDegreesCommand(90,true,.32),3);
                addSequential(new DriveXInchesCommand(21, 0.3),1.5);
                addSequential(new SetElevatorHeightPercentCommand(40,1),2);

                addSequential(new WaitCommand(0.2));
                addSequential(new SetCollectorSpeedCommand(1));
                addSequential(new WaitCommand(1));
                addSequential(new DriveXInchesCommand(8, -0.5),3);
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new LowerElevatorCommand());
                addSequential(new WaitCommand(0.2));

                addSequential(new DriveXInchesCommand(20, -.5));

            } else {
                System.out.println("Switch ---- Auto-Run");
                addSequential(new DriveXInchesCommand(140,0.5));
            }
        } else if (startPos == 'R') {
            if(switchPos == 'R') {
                System.out.println("RStart Auto-Priority ---- Target: RSwitch");

                addParallel(new LowerCollectorClawCommand(false,2));
                addParallel(new SetElevatorHeightPercentCommand(40,.5),2);

                addSequential(new DriveXInchesCommand(154, 0.5));
                addSequential(new RotateXDegreesCommand(-90, true,.29),2);
                addSequential(new WaitCommand(0.5));
                addSequential(new DriveXInchesCommand(18, 0.3),2);

                addSequential(new WaitCommand(0.2));
                addSequential(new SetCollectorSpeedCommand(.5));
                addSequential(new WaitCommand(1));
                addSequential(new DriveXInchesCommand(8, -0.5),3);
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new LowerElevatorCommand());
                addSequential(new WaitCommand(0.2));

                addSequential(new DriveXInchesCommand(12, -0.5));
                addSequential(new RotateXDegreesCommand(-90,true,.2),3);

            } else if(scalePos == 'R') {
                System.out.println("RStart Auto-Priority ---- Target: RScale");

                addParallel(new LowerCollectorClawCommand(false,2));

                //Cube One
                addParallel(new RaiseElevatorCommand(1));
                addSequential(new DriveXInchesCommand(248, 0.6));
                addSequential(new WaitCommand(0.1));
                addSequential(new RotateXDegreesCommand(-45, true, 0.25), 3);
                addSequential(new DriveXInchesCommand(8,0.4),2);
                addSequential(new WaitCommand(0.1));

                addSequential(new SetCollectorSpeedCommand(1));
                addSequential(new WaitCommand(1));
                addSequential(new DriveXInchesCommand(13, -0.6), 5);
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new WaitCommand(0.2));

            } else if(switchPos == 'L') {
                System.out.println("RStart Auto-Priority ---- Target: LSwitch");

                addParallel(new LowerCollectorClawCommand(false,2));
                addParallel(new SetElevatorHeightPercentCommand(20,.5),2);

                addSequential(new DriveXInchesCommand(213, 0.7));
                addSequential(new RotateXDegreesCommand(-90,true,.32),3);
                addSequential(new DriveXInchesCommand(189, 0.7,0.4,0.2));
                addParallel(new SetElevatorHeightPercentCommand(40,.5),2);
                addSequential(new RotateXDegreesCommand(-90,true,.32),3);
                addSequential(new DriveXInchesCommand(21, 0.3),1.5);
                addSequential(new SetElevatorHeightPercentCommand(40,1),2);

                addSequential(new WaitCommand(0.2));
                addSequential(new SetCollectorSpeedCommand(1));
                addSequential(new WaitCommand(1));
                addSequential(new DriveXInchesCommand(8, -0.5),3);
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new LowerElevatorCommand());
                addSequential(new WaitCommand(0.2));

                addSequential(new DriveXInchesCommand(20, -.5));

            } else {
                System.out.println("Priority ---- Auto-Run");
                addSequential(new DriveXInchesCommand(140,0.5));
            }
        } else {
            System.out.println("Auto-Run");
            addSequential(new DriveXInchesCommand(140,0.5));
        }
    }
}
