package frc.team1816.robot.commands;

import com.edinarobotics.utils.gamepad.Gamepad;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;
import frc.team1816.robot.subsystems.Elevator;

public class GamepadElevatorCommand extends Command {

    private Elevator elevator;
    private Gamepad gamepad;
    private Drivetrain drivetrain;
    private double power;
    private boolean slowModeCheck;
    private boolean slowModeSetCheck;

    public GamepadElevatorCommand(Gamepad gamepad) {
        super("gamepadelevatorcommand");
        this.elevator = Components.getInstance().elevator;
        this.drivetrain = Components.getInstance().drivetrain;
        this.gamepad = gamepad;
        requires(elevator);
    }

    @Override
    protected void initialize() {
//        System.out.println("Initialized GamepadElevatorCommand");
    }

    @Override
    protected void execute() {
        power = gamepad.getRightY();
        if(power > 0.05) {
            power = 0.5 * power + 0.5;
        } else if (power < - 0.05) {
            power = 0.5 * power - 0.5;
        } else if (gamepad.dPadLeft().get()) {
            power = 0.15;
        } else {
            power = 0;
        }

//        System.out.println("Upper opto: " + elevator.upperLimit.get() + "\t Lower opto: " + elevator.lowerLimit.get());

//        System.out.println("elevator enc ticks: " + elevator.getTicks());
//        System.out.println("elevator height percent: " + elevator.getHeightPercent() + "%");

        elevator.setElevatorSpeed(power);

        if(elevator.getHeightPercent() > 50) {
            drivetrain.setSlowMode(true);
            slowModeCheck = true;
        }
        else {
            if(slowModeCheck) {
                drivetrain.setSlowMode(false);
                slowModeCheck = false;
            }
        }

    }


    @Override
    protected boolean isFinished() {
        return false;
    }
}
