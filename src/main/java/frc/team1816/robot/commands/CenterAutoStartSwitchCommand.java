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

               addSequential(new DriveXInchesCommand(33, 0.7));
               addSequential(new RotateXDegreesCommand(-90, true, 0.35), 3);
               addSequential(new DriveXInchesCommand(66, 0.7));

               addParallel(new SetElevatorHeightPercentCommand(30, 0.7), 2);
               addSequential(new LowerCollectorClawCommand(false,0.5));

               addSequential(new RotateXDegreesCommand(90, true, 0.35), 3);
               addSequential(new DriveXInchesCommand(63, 0.7));

               addSequential(new SetCollectorSpeedCommand(1));
               addSequential(new WaitCommand(1));
               addSequential(new SetCollectorSpeedCommand(0));
               addSequential(new DriveXInchesCommand(12, -0.5),3);

               addParallel(new LowerElevatorCommand(-0.8));
               addSequential(new RotateXDegreesCommand(85, true, .3));
               addParallel(new SetCollectorSpeedCommand(-1));
               addSequential(new DriveXInchesCommand(70,.5 ));
               addSequential(new WaitCommand(.5));
               addSequential(new SetCollectorSpeedCommand(0));
               addSequential(new DriveXInchesCommand(75, -.5));
               addSequential(new RotateXDegreesCommand(-85, true, .3));
               addSequential(new DriveXInchesCommand(12, .5),2);
               addSequential(new SetElevatorHeightPercentCommand(40));
               addSequential(new SetCollectorSpeedCommand(1));
               addSequential(new WaitCommand(.4));
               addSequential(new SetCollectorSpeedCommand(0));


          } else if (target == 'R'){
               System.out.println("Center Switch ---- Target: R");

               addSequential(new DriveXInchesCommand(6, 0.6),4);
               addSequential(new RotateXDegreesCommand(25, true, 0.3), 3);

               addParallel(new SetElevatorHeightPercentCommand(30, 0.7), 2);
               addSequential(new LowerCollectorClawCommand(false,0.73));
               addSequential(new DriveXInchesCommand(70, 0.7),5);

               addSequential(new SetCollectorSpeedCommand(1));
               addSequential(new WaitCommand(1));
               addSequential(new SetCollectorSpeedCommand(0));

               addParallel(new LowerElevatorCommand(-0.7));
               addSequential(new DriveXInchesCommand(24, -0.5),5);
               addSequential(new RotateXDegreesCommand(-40, true, .3),3);

               addParallel(new SetCollectorSpeedCommand(-1));
               addSequential(new DriveXInchesCommand(48,.7 ));
               addSequential(new SetCollectorSpeedCommand(0));
               addSequential(new DriveXInchesCommand(48, -.7));
               addSequential(new RotateXDegreesCommand(40, true, .3));

               addParallel(new SetElevatorHeightPercentCommand(40));
               addSequential(new DriveXInchesCommand(24, 0.7));
               addSequential(new SetCollectorSpeedCommand(1));
               addSequential(new WaitCommand(.4));
               addSequential(new SetCollectorSpeedCommand(0));


          } else {
               System.out.println("Center Switch ---- Auto-Run");
               addSequential(new DriveXInchesCommand(140, 0.5));
          }
     }

}
