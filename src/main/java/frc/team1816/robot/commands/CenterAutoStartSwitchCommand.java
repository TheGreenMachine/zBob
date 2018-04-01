package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import javafx.scene.transform.Rotate;

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

               addParallel(new LowerCollectorClawCommand(false,.5));
               addSequential(new DriveXInchesCommand(33, 0.7));
               addSequential(new RotateXDegreesCommand(-90, true, 0.35), 3);
               addSequential(new DriveXInchesCommand(73, 0.7));

               addParallel(new SetElevatorHeightPercentCommand(40, 0.7), 2);

               addSequential(new RotateXDegreesCommand(90, true, 0.35), 3);
               addSequential(new DriveXInchesCommand(63, 0.7));

               addSequential(new SetCollectorSpeedCommand(1));
               addSequential(new WaitCommand(1));
               addSequential(new SetCollectorSpeedCommand(0));
               addSequential(new DriveXInchesCommand(12, -0.5),3);

//               addSequential(new RotateXDegreesCommand(90, true, .3));
//               addParallel(new SetCollectorSpeedCommand(-1));
//               addSequential(new DriveXInchesCommand(75,.5 ));
//               addSequential(new WaitCommand(.5));
//               addSequential(new SetCollectorSpeedCommand(0));
//               addSequential(new DriveXInchesCommand(75, -5));
//               addSequential(new RotateXDegreesCommand(-90, true, .3));
//               addSequential(new DriveXInchesCommand(12, .5));
//               addSequential(new SetCollectorSpeedCommand(1));
//               addSequential(new WaitCommand(.4));
//               addSequential(new SetCollectorSpeedCommand(0));


          } else if (target == 'R'){
               System.out.println("Center Switch ---- Target: R");

               addParallel(new LowerCollectorClawCommand(false,.5));
               addSequential(new DriveXInchesCommand(33, 0.5));
               addSequential(new RotateXDegreesCommand(90, true, 0.3), 3);
               addSequential(new DriveXInchesCommand(60, 0.5));

               addParallel(new SetElevatorHeightPercentCommand(40, 0.5), 2);

               addSequential(new RotateXDegreesCommand(-90, true, 0.3), 3);
               addSequential(new DriveXInchesCommand(63, 0.5));

               addSequential(new SetCollectorSpeedCommand(1));
               addSequential(new WaitCommand(1));
               addSequential(new SetCollectorSpeedCommand(0));
               addSequential(new DriveXInchesCommand(12, -0.5),3);

//               addSequential(new RotateXDegreesCommand(-90, true, .3));
//               addParallel(new SetCollectorSpeedCommand(-1));
//               addSequential(new DriveXInchesCommand(75,.5 ));
//               addSequential(new WaitCommand(.5));
//               addSequential(new SetCollectorSpeedCommand(0));
//               addSequential(new DriveXInchesCommand(75, -5));
//               addSequential(new RotateXDegreesCommand(90, true, .3));
//               addSequential(new DriveXInchesCommand(12, .5));
//               addSequential(new SetCollectorSpeedCommand(1));
//               addSequential(new WaitCommand(.4));
//               addSequential(new SetCollectorSpeedCommand(0));


          } else {
               System.out.println("Center Switch ---- Auto-Run");
               addSequential(new DriveXInchesCommand(140, 0.5));
          }
     }

}
