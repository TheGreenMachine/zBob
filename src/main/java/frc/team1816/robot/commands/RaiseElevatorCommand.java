package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Elevator;

public class RaiseElevatorCommand extends Command {
    private Elevator elevator;
    private double secondsToWait, msToWait;
    private double velocity;

    public RaiseElevatorCommand() {
        super ("raiseelevatorcommand");
        this.elevator = Components.getInstance().elevator;
        this.secondsToWait = 0;
        this.velocity = 1;
        requires(elevator);
    }

    public RaiseElevatorCommand(int secondsToWait) {
        super("raiseelevatorcommand");
        this.elevator = Components.getInstance().elevator;
        this.secondsToWait = secondsToWait;
        this.velocity = 1;
        requires(elevator);
    }

    public RaiseElevatorCommand(double velocity) {
        super ("raiseelevatorcommand");
        this.elevator = Components.getInstance().elevator;
        this.secondsToWait = 0;
        this.velocity = velocity;
        requires(elevator);
    }

    public RaiseElevatorCommand(double velocity, int secondsToWait) {
        super ("raiseelevatorcommand");
        this.elevator = Components.getInstance().elevator;
        this.secondsToWait = secondsToWait;
        this.velocity = velocity;
        requires(elevator);
    }

    public void initialize() {
        System.out.println("Raising Elevator");
        msToWait = System.currentTimeMillis() + secondsToWait * 1000;
    }

    public void execute() {
        if(System.currentTimeMillis() > msToWait) {
            elevator.setElevatorSpeed(velocity);
        } else {
            elevator.setElevatorSpeed(0);
        }
    }

    @Override
    protected boolean isFinished() {
        if(elevator.getUpperLimit()) {
//            elevator.setElevatorSpeed(0);
            System.out.println("stopped raising");
            return true;
        } else {
            return false;
        }
    }

    protected void end() {
        elevator.setElevatorSpeed(0);
    }
}
