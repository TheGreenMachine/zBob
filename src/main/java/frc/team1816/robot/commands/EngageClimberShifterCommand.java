package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Elevator;


public class EngageClimberShifterCommand extends Command {
    private Elevator elevator;
    private boolean shift;

    public EngageClimberShifterCommand(boolean shift) {
        super("engageclimbershiftercommand");
        elevator = Components.getInstance().elevator;
        this.shift = shift;
        requires(elevator);
    }

    @Override
    protected void initialize() {
        elevator.engageShifter();
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
