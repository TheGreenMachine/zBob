package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class NearSwitchAutoCommand extends CommandGroup {
    char target;
    char startPos;

    public NearSwitchAutoCommand() {

    }

    public void selectAuto(String data, String pos) {
        try {
            target = data.charAt(0);
            startPos = pos.charAt(0);

            System.out.println("Switch Auto Selection: " + target);
        } catch (Exception e) {
            System.out.println("NO TARGET!");
            target = 'n';
        }

        if(startPos == 'L') {
            if(target == 'L') {
                System.out.println("LStart Switch ---- Target: L");

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

            } else {
                System.out.println("Switch ---- Auto-Run");
                addSequential(new DriveXInchesCommand(140,0.5));
            }
        } else if (startPos == 'R') {
            if (target == 'R') {
                System.out.println("RStart Switch ---- Target: R");

                addParallel(new LowerCollectorClawCommand(false,2));
                addParallel(new SetElevatorHeightPercentCommand(40,.5),2);

                addSequential(new DriveXInchesCommand(154, 0.5));
                addSequential(new RotateXDegreesCommand(-90, true,.29),2);
                addSequential(new WaitCommand(0.5));
                addSequential(new DriveXInchesCommand(18, 0.3),2);

                addSequential(new WaitCommand(0.2));
                addSequential(new SetCollectorSpeedCommand(.5));
                addSequential(new WaitCommand(1));
                addSequential(new DriveXInchesCommand(8, -0.5), 3);
                addSequential(new SetCollectorSpeedCommand(0));
                addSequential(new LowerElevatorCommand());
                addSequential(new WaitCommand(0.2));

                addSequential(new DriveXInchesCommand(12, -0.5));
                addSequential(new RotateXDegreesCommand(90,true,.2),3);

            } else {
                System.out.println("Switch ---- Auto-Run");
                addSequential(new DriveXInchesCommand(140,0.5));
            }
        } else {
            System.out.println("Auto-Run");
            addSequential(new DriveXInchesCommand(140,0.5));
        }
    }
}
