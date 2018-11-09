package frc.team1816.robot.commands;

import com.edinarobotics.purepursuit.PPPoint;
import edu.wpi.first.wpilibj.command.CommandGroup;


public class PurePursuitAutoTestCommand extends CommandGroup {
    private PPPoint pt1 = new PPPoint(0,0);
    private PPPoint pt2 = new PPPoint(0, 24);
    private PPPoint pt3 = new PPPoint(36, 36);
//    private PPPoint pt4 = new PPPoint(60, 60);

    public PurePursuitAutoTestCommand() {
        addSequential(new PurePursuitLineCommand(pt1 ,pt2, 4, 0.25));
        addSequential(new PurePursuitLineCommand(pt2, pt3, 4, 0.25));
//        addSequential(new PurePursuitLineCommand(pt3, pt4, 12, 0.25));

        System.out.println("Test Command End");
    }
}
