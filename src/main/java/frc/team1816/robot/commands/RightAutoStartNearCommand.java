package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightAutoStartNearCommand extends CommandGroup {
    char switchPos, scalePos;

    public RightAutoStartNearCommand() {

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
            System.out.println("RStart Auto-Priority Near Only ---- Target: RSwitch");

//            addParallel(new LowerCollectorClawCommand(false,1.5));
            addParallel(new SetElevatorHeightPercentCommand(40,.5),2);

            addSequential(new DriveXInchesCommand(144, 0.8));
            addSequential(new RotateXDegreesCommand(-90, true,.3),2);
            addSequential(new WaitCommand(0.5));
            addSequential(new DriveXInchesCommand(18, 0.3),2);

            addSequential(new WaitCommand(0.2));
            addSequential(new SetCollectorSpeedCommand(.5));
            addSequential(new WaitCommand(1));
            addSequential(new DriveXInchesCommand(4, -0.5),3);
            addSequential(new SetCollectorSpeedCommand(0));
            addSequential(new LowerElevatorCommand());
            addSequential(new WaitCommand(0.2));

            addSequential(new DriveXInchesCommand(12, -0.5));
            addSequential(new RotateXDegreesCommand(-90,true,.2),3);

        } else if(scalePos == 'R') {
            System.out.println("RStart Auto-Priority Near Only ---- Target: RScale");

//            addParallel(new LowerCollectorClawCommand(false,1.5));

            //Cube One
            addParallel(new RaiseElevatorCommand(1));
            addSequential(new DriveXInchesCommand(248, 0.6));
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
            addParallel(new SetCollectorSpeedCommand(-1));
            addSequential(new DriveXInchesCommand(68, 0.7, 0.4, 0.2), 4);
            addSequential(new SetCollectorSpeedCommand(0));
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
        } else {
            System.out.println("Switch ---- Auto-Run");
            addSequential(new DriveXInchesCommand(140,0.5));
        }
    }
}
