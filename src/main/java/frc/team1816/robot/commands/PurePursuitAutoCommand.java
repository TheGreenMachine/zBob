package frc.team1816.robot.commands;

import com.edinarobotics.purepursuit.commands.Point;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class PurePursuitAutoCommand extends CommandGroup {
    private Point pt1 = new Point(0,0);
    private Point pt2 = new Point(0, 60);

    public PurePursuitAutoCommand(){
        addSequential(new PurePursuitQuadraticCommand(pt1, pt2, 4, 0.25));
    }
}
