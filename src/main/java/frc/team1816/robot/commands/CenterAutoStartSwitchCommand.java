package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterAutoStartSwitchCommand extends CommandGroup {
     char target;

     public CenterAutoStartSwitchCommand(){}

     public void selectAuto(String data){
          try {
               target = data.charAt(0);

               System.out.println("Center Switch Auto Selection: " + target);
          } catch (Exception e) {
               System.out.println("NO TARGET!");
               target = 'n';
          }

          if (target == 'L'){
               System.out.println("Center Switch ---- Target: L");

               addParallel(new LowerCollectorClawCommand(false,3));
               addSequential(new DriveXInchesCommand(45, 0.7));
               addSequential(new RotateXDegreesCommand(-90, true, 0.3), 2);
               addSequential(new DriveXInchesCommand(75, 0.7));

               addParallel(new SetElevatorHeightPercentCommand(40, 0.5), 2);

               addSequential(new RotateXDegreesCommand(90, true, 0.3), 2);
               addSequential(new DriveXInchesCommand(51, 0.7));

               addSequential(new SetCollectorSpeedCommand(1));
               addSequential(new WaitCommand(1));
               addSequential(new DriveXInchesCommand(4, -0.5),3);

          } else if (target == 'R'){
               System.out.println("Center Switch ---- Target: R");

               addParallel(new LowerCollectorClawCommand(false,3));
               addSequential(new DriveXInchesCommand(45, 0.7));
               addSequential(new RotateXDegreesCommand(90, true, 0.3), 2);
               addSequential(new DriveXInchesCommand(75, 0.7));

               addParallel(new SetElevatorHeightPercentCommand(40, 0.5), 2);

               addSequential(new RotateXDegreesCommand(-90, true, 0.3), 2);
               addSequential(new DriveXInchesCommand(51, 0.7));

               addSequential(new SetCollectorSpeedCommand(1));
               addSequential(new WaitCommand(1));
               addSequential(new DriveXInchesCommand(4, -0.5),3);


          } else {
               System.out.println("Center Switch ---- Auto-Run");
               addSequential(new DriveXInchesCommand(140, 0.5));
          }
     }

}
