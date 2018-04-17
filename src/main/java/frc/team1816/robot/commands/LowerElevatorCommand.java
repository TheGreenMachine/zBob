package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Elevator;

public class LowerElevatorCommand extends Command {
    private Elevator elevator;
    private double velocity;

    public LowerElevatorCommand() {
        super ("lowerelevatorcommand");
        this.elevator = Components.getInstance().elevator;
        this.velocity = -1;
        requires(elevator);
    }

    public LowerElevatorCommand(double velocity) {
        super ("lowerelevatorcommand");
        this.elevator = Components.getInstance().elevator;
        this.velocity = velocity;
        requires(elevator);
    }

    public void initialize() {
        elevator.setElevatorSpeed(velocity);
        System.out.println("Lowering Elevator");
    }

    @Override
    protected boolean isFinished() {
        if(elevator.getLowerLimit()) {
//            elevator.setElevatorSpeed(0);
            System.out.println("stopped lowering");
            return true;
        } else {
            return false;
        }
    }

    protected void end() {
        elevator.setElevatorSpeed(0);
    }
}
