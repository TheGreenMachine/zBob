package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftAutoStartAvoidanceCommand extends CommandGroup {
    char target;

    public LeftAutoStartAvoidanceCommand() {

    }

    public void selectAuto(String data) {
        try {
            target = data.charAt(1);

            System.out.println("Scale Auto Selection: " + target);
        } catch (Exception e) {
            System.out.println("NO TARGET!");
            target = 'n';
        }

        if(target == 'L') {
            System.out.println("Scale Avoidance ---- Target: L");

            addSequential(new DriveXInchesCommand(6,0.5),2);
            addSequential(new RotateXDegreesCommand(-45,true,0.2),2);
            addSequential(new DriveXInchesCommand(24,0.5),2);
            addSequential(new RotateXDegreesCommand(45,true,0.2),2);

            addParallel(new SetElevatorHeightPercentCommand(0.6,0.5),3);
            addSequential(new DriveXInchesCommand(280,0.8),2);

            addSequential(new RaiseElevatorCommand(),3);
            addSequential(new RotateXDegreesCommand(90,true,0.2),3);
            addSequential(new DriveXInchesCommand(10,0.6),3);

            addSequential(new SetCollectorSpeedCommand(1));
            addSequential(new WaitCommand(1));
            addSequential(new SetCollectorSpeedCommand(0));
            addSequential(new DriveXInchesCommand(-12,0.5),2);

        } else if (target == 'R'){
            System.out.println("Scale Avoidance ---- Target: R");

        } else {
            System.out.println("Switch ---- Auto-Run");
            addSequential(new DriveXInchesCommand(140,0.5));
        }
    }
}
