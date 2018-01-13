package frc.team1816.robot.commands;

import com.edinarobotics.utils.gamepad.Gamepad;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;

public class GamepadDriveCommand extends Command {

    private Drivetrain drivetrain;
    private Gamepad gamepad;

    public GamepadDriveCommand(Gamepad gamepad) {
        super("gamepaddrivecommand");
        this.drivetrain = Components.getInstance().drivetrain;
        this.gamepad = gamepad;
        requires(drivetrain);
    }

    @Override
    protected void initialize() {
//        System.out.println("GamePadDrive Command Init");
    }

    @Override
    protected void execute() {
//        System.out.println("GamePadDrive Command Executing...");
        double right = gamepad.getRightY();
        double left = gamepad.getLeftY();

        drivetrain.setDrivetrain(left, right);
    }

    @Override
    protected boolean isFinished() {
//        System.out.println("GamePadDrive Command Terminated");
        return false;
    }
}
