package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightAutoStartCommand extends CommandGroup {
    char switchPos, scalePos;

    public RightAutoStartCommand() {

    }

    public void selectAuto(String data) {
        try {
            switchPos = data.charAt(0);
            scalePos = data.charAt(1);

        } catch (Exception e) {
            System.out.println("NO TARGET!");
            switchPos = 'n';
            scalePos = 'n';
        }

        if(switchPos == 'R') {
            System.out.println("RStart Auto-Priority ---- Target: RSwitch");

//            addParallel(new LowerCollectorClawCommand(false,3));
            addParallel(new SetElevatorHeightPercentCommand(40,.5),2);

            addSequential(new DriveXInchesCommand(134, 0.8));
            addSequential(new RotateXDegreesCommand(-90, true,.2),2);
            addSequential(new WaitCommand(0.5));
            addSequential(new DriveXInchesCommand(18, 0.3),2);

            addSequential(new WaitCommand(0.2));
            addSequential(new SetCollectorSpeedCommand(1));
            addSequential(new WaitCommand(1));
            addSequential(new DriveXInchesCommand(4, -0.5),3);
            addSequential(new SetCollectorSpeedCommand(0));
            addSequential(new LowerElevatorCommand());
            addSequential(new WaitCommand(0.2));

            addSequential(new DriveXInchesCommand(30, -0.5));
            addSequential(new RotateXDegreesCommand(-90,true,.2),3);
            addSequential(new DriveXInchesCommand(24, 0.5));
        } else if(scalePos == 'R') {
            System.out.println("RStart Auto-Priority ---- Target: RScale");

            addParallel(new LowerCollectorClawCommand(false,3));

            //Cube One
            addParallel(new RaiseElevatorCommand(1));
            addSequential(new DriveXInchesCommand(270, 0.6));
            addSequential(new WaitCommand(0.1));
            addSequential(new RotateXDegreesCommand(-45, true, .2), 3);
            addSequential(new WaitCommand(0.1));

            addSequential(new SetCollectorSpeedCommand(1));
            addSequential(new WaitCommand(1));
            addSequential(new DriveXInchesCommand(13, -0.6), 5);
            addParallel(new LowerElevatorCommand());
            addSequential(new SetCollectorSpeedCommand(0));
            addSequential(new WaitCommand(0.2));

            //Turn and collect second cube
            addSequential(new RotateXDegreesCommand(-115, true, .2), 3);
            addParallel(new SetCollectorSpeedCommand(-1));
            addSequential(new DriveXInchesCommand(72, 0.7, 0.4, 0.2), 4);
            addSequential(new WaitCommand(0.5));

            //Turn and place cube on scale
            addSequential(new DriveXInchesCommand(70, -0.7, 0.6, 0.2));
            addParallel(new SetElevatorHeightPercentCommand(100),2);
            addSequential(new SetCollectorSpeedCommand(0));
            addSequential(new RotateXDegreesCommand(115, true, .2), 3);
            addSequential(new DriveXInchesCommand(12, 0.8));
            addSequential(new SetCollectorSpeedCommand(1));
        } else if(switchPos == 'L') {
            System.out.println("RStart Auto-Priority ---- Target: LScale");

            addParallel(new LowerCollectorClawCommand(false,3));
            addParallel(new SetElevatorHeightPercentCommand(20,.5),2);

            addSequential(new DriveXInchesCommand(226, 0.7));
            addSequential(new RotateXDegreesCommand(-90,true,.2),3);
            addSequential(new DriveXInchesCommand(192, 0.7,0.4,0.2));
            addParallel(new SetElevatorHeightPercentCommand(40,.5),2);
            addSequential(new RotateXDegreesCommand(-90,true,.2),3);
            addSequential(new DriveXInchesCommand(18, 0.3),1.5);
            addSequential(new SetElevatorHeightPercentCommand(40,1),3);

            addSequential(new WaitCommand(0.2));
            addSequential(new SetCollectorSpeedCommand(1));
            addSequential(new WaitCommand(1));
            addSequential(new DriveXInchesCommand(4, -0.5),3);
            addSequential(new SetCollectorSpeedCommand(0));
            addSequential(new LowerElevatorCommand());
            addSequential(new WaitCommand(0.2));

            addSequential(new DriveXInchesCommand(20, -.5));

        } else if (scalePos == 'L') {
            System.out.println("RStart Auto-Priority ---- Target: LScale");

            addParallel(new LowerCollectorClawCommand(false,3));
            addParallel(new SetElevatorHeightPercentCommand(20),2);

            addSequential(new DriveXInchesCommand(213, 0.7));
            addSequential(new RotateXDegreesCommand(-90,true,.2));
            addSequential(new DriveXInchesCommand(208, 0.7,0.4,0.2));
            addSequential(new RotateXDegreesCommand(90,true,.2),3);
            addParallel(new RaiseElevatorCommand());
            addSequential(new DriveXInchesCommand(52,0.8),5);

            addSequential(new WaitCommand(1));
            addSequential(new SetCollectorSpeedCommand(1));
            addSequential(new WaitCommand(1));
            addSequential(new LowerElevatorCommand());
            addSequential(new SetCollectorSpeedCommand(0));
            addSequential(new WaitCommand(0.2));
        } else {
            System.out.println("Switch ---- Auto-Run");
            addSequential(new DriveXInchesCommand(140,0.5));
        }
    }
}
