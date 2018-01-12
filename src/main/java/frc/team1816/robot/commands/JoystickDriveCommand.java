package frc.team1816.robot.commands;

import com.edinarobotics.utils.gamepad.Gamepad;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.command.Command;

public class JoystickDriveCommand extends Command{

    private Drivetrain drivetrain;
    private Gamepad gamepad0;

    public JoystickDriveCommand(Gamepad gamepad0) {
        super ("joystickdrivecommand");
        this.gamepad0 = gamepad0;
        drivetrain = Components.getInstance().drivetrain;
        requires(drivetrain);
    }

    @Override
    protected void execute() {
        double leftPower = gamepad0.getLeftY();
        double rightPower = gamepad0.getRightY();

        drivetrain.setDrivetrain(leftPower, rightPower);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {

    }

    protected void interrupted() {
        end();
    }
}
