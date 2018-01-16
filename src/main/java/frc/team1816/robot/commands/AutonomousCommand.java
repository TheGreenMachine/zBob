package frc.team1816.robot.commands;
import edu.wpi.first.wpilibj.command.Command;

public class AutonomousCommand extends Command {
    public AutonomousCommand(AutonomousMode mode) {

        switch(mode){

        }
    }

    public enum AutonomousMode {

    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
