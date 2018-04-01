package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Climber;
import frc.team1816.robot.subsystems.Elevator;


public class ToggleClimberShifterCommand extends Command {
    private Climber climber;
    private Elevator elevator;
    private boolean shift;
    private long initTime, deltaTime;

    public ToggleClimberShifterCommand(boolean shift) {
        super("toggleclimbershiftercommand");
        climber = Components.getInstance().climber;
        elevator = Components.getInstance().elevator;
        this.shift = shift;

        initTime = System.currentTimeMillis();
        requires(climber);
    }

    @Override
    protected void initialize() {
        if(shift) {
            climber.engageShifter();
        } else {
            climber.disengageShifter();

            elevator.setElevatorSpeed(1);
        }
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        if(System.currentTimeMillis() - initTime > 20) {
            elevator.setElevatorSpeed(0);
            return true;
        } else{
            return false;
        }
    }

    @Override
    protected void end() {

    }

    @Override
    protected void interrupted() {
        super.interrupted();
    }
}
