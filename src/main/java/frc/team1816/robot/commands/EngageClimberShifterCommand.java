package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Climber;


public class EngageClimberShifterCommand extends Command {
    private Climber climber;
    private boolean shift;

    public EngageClimberShifterCommand(boolean shift) {
        super("engageclimbershiftercommand");
        climber = Components.getInstance().climber;
        this.shift = shift;
        requires(climber);
    }

    @Override
    protected void initialize() {
        climber.engageShifter();
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
