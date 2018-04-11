package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Elevator;

public class HoldElevatorPosCommand extends Command {
    private Elevator elevator;
    boolean holdPos;

    public HoldElevatorPosCommand(boolean holdPos) {
        super("holdelevatorposcommand");
        this.holdPos = holdPos;
        elevator = Components.getInstance().elevator;
    }

    @Override
    protected void initialize() {
        if(holdPos) {
            elevator.setElevatorSpeed(.15);
        } else {
            elevator.setElevatorSpeed(0);
        }
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {

    }
}
