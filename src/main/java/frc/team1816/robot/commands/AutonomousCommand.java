package frc.team1816.robot.commands;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousCommand extends CommandGroup {
    public AutonomousCommand(AutonomousMode mode) {

        switch(mode){
            case SWITCH_FORWARD:
                addSequential(new DriveXInchesCommand(110, 0.8, false));
                break;
            case RIGHT_SWITCH_FORWARD_TURN:
                addSequential(new DriveXInchesCommand(140, 0.8, false));
                addSequential(new RotateXDegreesCommand(90));
                addSequential(new DriveXInchesCommand(10, 0.8, false));
            case LEFT_SWITCH_FORWARD_TURN:
                addSequential(new DriveXInchesCommand(140, 0.8, false));
                addSequential(new RotateXDegreesCommand(-90));
                addSequential(new DriveXInchesCommand(10, 0.8, false));
        }
    }

    public enum AutonomousMode {
        SWITCH_FORWARD,
        RIGHT_SWITCH_FORWARD_TURN,
        LEFT_SWITCH_FORWARD_TURN
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
