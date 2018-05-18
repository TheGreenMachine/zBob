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

              addSequential(new DriveXInchesCommand(6, 0.6),4);
              addSequential(new RotateXDegreesCommand(-45, true, 0.3), 3);

              addSequential(new DriveXInchesCommand(75, 0.6),5);
              addParallel(new SetElevatorHeightPercentCommand(40), 1);
              addSequential(new RotateXDegreesCommand(45));
              addSequential(new LowerCollectorClawCommand(false,0.7));
              addSequential(new DriveXInchesCommand(24, 0.7),1);

              addSequential(new SetCollectorSpeedCommand(0.4));
              addSequential(new WaitCommand(0.5));
              addSequential(new SetCollectorSpeedCommand(0));

              addParallel(new LowerElevatorCommand(-0.7));
              addSequential(new DriveXInchesCommand(54, -0.7),4);
              addSequential(new WaitCommand(0.5));
              addSequential(new RotateXDegreesCommand(42, true, .3),2);

              addParallel(new SetCollectorSpeedCommand(-0.7));
              addSequential(new DriveXInchesCommand(56,0.6),2);
              addSequential(new SetCollectorSpeedCommand(0));
              addSequential(new DriveXInchesCommand(50, -.6),2);
              addSequential(new RotateXDegreesCommand(-42, true, .3),2);

              addParallel(new SetElevatorHeightPercentCommand(40),1);
              addSequential(new DriveXInchesCommand(84, 0.6),4);
              addSequential(new SetCollectorSpeedCommand(0.4));
              addSequential(new WaitCommand(.4));
              addSequential(new SetCollectorSpeedCommand(0));

          } else if (target == 'R'){
              System.out.println("Center Switch ---- Target: R");

              addSequential(new DriveXInchesCommand(6, 0.6),4);
              addSequential(new RotateXDegreesCommand(45, true, 0.3), 3);

              addSequential(new DriveXInchesCommand(65, 0.7),3);
              addParallel(new SetElevatorHeightPercentCommand(40), 1);
              addSequential(new RotateXDegreesCommand(-45),3);
              addSequential(new LowerCollectorClawCommand(false,0.7));
              addSequential(new DriveXInchesCommand(30, 0.6),2);

              addSequential(new SetCollectorSpeedCommand(0.4));
              addSequential(new WaitCommand(0.5));
              addSequential(new SetCollectorSpeedCommand(0));

              addParallel(new LowerElevatorCommand(-0.7));
              addSequential(new DriveXInchesCommand(60, -0.6),4);
              addSequential(new WaitCommand(0.5));
              addSequential(new RotateXDegreesCommand(-53, true, .3),3);

              addParallel(new SetCollectorSpeedCommand(-0.7));
              addSequential(new DriveXInchesCommand(64,0.7),3);
              addSequential(new SetCollectorSpeedCommand(0));
              addSequential(new DriveXInchesCommand(50, -.76),3);
              addSequential(new RotateXDegreesCommand(55, true, .3),3);

              addParallel(new SetElevatorHeightPercentCommand(40),1);
              addSequential(new DriveXInchesCommand(78, 0.7),3);
              addSequential(new SetCollectorSpeedCommand(0.4));
              addSequential(new WaitCommand(.4));
              addSequential(new SetCollectorSpeedCommand(0));

          } else {
               System.out.println("Center Switch ---- Auto-Run");
               addSequential(new DriveXInchesCommand(140, 0.5));
          }
     }

}
