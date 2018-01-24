package frc.team1816.robot.commands;

import com.edinarobotics.utils.gamepad.Gamepad;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Elevator;

public class GamepadElevatorCommand extends Command {

    private Elevator elevator;
    private Gamepad gamepad;

    public GamepadElevatorCommand(Gamepad gamepad){
        super("gamepadelevatorcommand");
        this.elevator = Components.getInstance().elevator;
        this.gamepad = gamepad;
        requires(elevator);
    }

    @Override
    protected void initialize() {
        System.out.println("Initialized GamepadElevatorCommand");
    }

    @Override
    protected void execute() {
        //TODO figure out how to set Elevator speed.
        // double elevatorSpeed = gamepad.getDPadX();
    }


    @Override
    protected boolean isFinished() {
        return false;
    }
}
