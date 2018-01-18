package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterAutoStartCommand extends CommandGroup{
    char target;

    public CenterAutoStartCommand() {

        target = DriverStation.getInstance().getGameSpecificMessage().charAt(0);

        if(target == 'L') {

        } else {

        }
    }
}
