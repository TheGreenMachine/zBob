package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Elevator;

public class SetElevatorHeightPercentCommand extends Command {
    private Elevator elevator;
    private double targetHeightPercent;
    private double velocity;


    public SetElevatorHeightPercentCommand(double heightPercent) {
        super ("setelevatorheightcommand");
        this.elevator = Components.getInstance().elevator;
        targetHeightPercent = heightPercent;
        this.velocity = 1;
        requires(elevator);
    }

    public SetElevatorHeightPercentCommand(double heightPercent, double velocity) {
        super("setelevatorheightcommand");
        this.elevator = Components.getInstance().elevator;
        targetHeightPercent = heightPercent;
        this.velocity = velocity;
        requires(elevator);
    }

    public void initialize() {
        System.out.println("Raising Elevator");
        if(elevator.getHeightPercent() > targetHeightPercent) {
            elevator.setElevatorSpeed(-velocity);
        } else if (elevator.getHeightPercent() < targetHeightPercent) {
            elevator.setElevatorSpeed(velocity);
        }
    }

    @Override
    protected boolean isFinished() {
        if(elevator.getUpperLimit() || (Math.abs(targetHeightPercent-elevator.getHeightPercent()) < 1)) {

            elevator.setElevatorSpeed(0);
            System.out.println("stopped raising");
            return true;
        } else {
            return false;
        }
    }
}
