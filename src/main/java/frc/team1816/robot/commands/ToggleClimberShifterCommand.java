package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Climber;


public class ToggleClimberShifterCommand extends Command {
    private Climber climber;
    private boolean shift;

    public ToggleClimberShifterCommand(boolean shift) {
        super("toggleclimbershiftercommand");
        climber = Components.getInstance().climber;
        this.shift = shift;
        requires(climber);
    }

    @Override
    protected void initialize() {
        if(shift) {
            climber.engageShifter();
        } else {
            climber.disengageShifter();
        }
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {

    }

    @Override
    protected void interrupted() {
        super.interrupted();
    }
}
