package frc.team1816.robot.commands;

import com.edinarobotics.utils.gamepad.Gamepad;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Elevator;

public class GamepadElevatorCommand extends Command {

    private Elevator elevator;
    private Gamepad gamepad;
    private double power;

    public GamepadElevatorCommand(Gamepad gamepad) {
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
        power = - gamepad.getLeftY();

        if(power > 0) {
            power = 1;
        } else {
            power = -1;
        }
        if (elevator.upperLimit.get() && power>0) {
            power = 0;
        } else if(elevator.lowerLimit.get() && power<0) {
            power = 0;
        }

        elevator.setElevatorSpeed(power);
    }


    @Override
    protected boolean isFinished() {
        return false;
    }
}
