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
        if(power > 0.05) {
            power = 0.5 * power + 0.5;
        } else if (power < - 0.05) {
            power = 0.5 * power - 0.5;
        } else {
            power = 0;
        }

//        System.out.println("Upper opto: " + elevator.upperLimit.get() + "\t Lower opto: " + elevator.lowerLimit.get());

        if (elevator.getUpperLimit() && power < 0) {
            System.out.println("STOPPED : UPPER LIMIT");
            power = 0;
        } else if(elevator.getLowerLimit() && power > 0) {
            System.out.println("STOPPED : LOWER LIMIT");
            power = 0;
        }

        elevator.setElevatorSpeed(power);
    }


    @Override
    protected boolean isFinished() {
        return false;
    }
}
