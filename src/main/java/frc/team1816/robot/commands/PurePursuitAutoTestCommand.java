package frc.team1816.robot.commands;

import com.edinarobotics.purepursuit.commands.PurePursuitCommand;
import com.edinarobotics.utils.math.PPPoint;
import edu.wpi.first.wpilibj.command.CommandGroup;


public class PurePursuitAutoTestCommand extends CommandGroup {
    private PPPoint pt1 = new PPPoint(0,0);
    private PPPoint pt2 = new PPPoint(0, 60);
    private PPPoint pt3 = new PPPoint(36, 60);
    private PPPoint pt4 = new PPPoint(60, 60);

    public PurePursuitAutoTestCommand() {
        addSequential(new PurePursuitCommand(pt1 ,pt2, 12, 0.25));
//        addSequential(new PurePursuitCommand(pt2, pt3, 12, 0.25));
//        addSequential(new PurePursuitCommand(pt3, pt4, 12, 0.25));

        System.out.println("Test Command End");
    }
}
