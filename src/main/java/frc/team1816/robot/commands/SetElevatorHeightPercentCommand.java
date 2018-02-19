package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Elevator;

public class SetElevatorHeightPercentCommand extends Command {
    private Elevator elevator;
    private double targetHeightPercent;

    public SetElevatorHeightPercentCommand(double heightPercent) {
        super ("raiseelevatorcommand");
        this.elevator = Components.getInstance().elevator;
        targetHeightPercent = heightPercent;
        requires(elevator);
    }

    public void initialize() {
        System.out.println("Raising Elevator");
        if(elevator.getHeightPercent() > targetHeightPercent) {
            elevator.setElevatorSpeed(-.8);
        } else if (elevator.getHeightPercent() < targetHeightPercent) {
            elevator.setElevatorSpeed(.8);
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
