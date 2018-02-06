package frc.team1816.robot.commands;

import com.edinarobotics.utils.gamepad.Gamepad;
import com.edinarobotics.utils.log.Logging;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.Robot;
import frc.team1816.robot.subsystems.Collector;
import frc.team1816.robot.subsystems.Drivetrain;

public class GamepadDriveCommand extends Command {

    private Drivetrain drivetrain;
    private Collector collector;
    private Gamepad gamepad;

    public GamepadDriveCommand(Gamepad gamepad) {
        super("gamepaddrivecommand");
        this.drivetrain = Components.getInstance().drivetrain;
        this.collector = Components.getInstance().collector;
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
        StringBuilder sb = new StringBuilder();

        double right = gamepad.getLeftY();
        double left = gamepad.getLeftY();
        double rotation = gamepad.getRightX();

        sb.append(drivetrain.talonPositionLeft());
        sb.append(",");
        sb.append(drivetrain.talonPositionRight());
        Robot.logger.log(sb.toString());

        System.out.println("left enc" + drivetrain.talonPositionLeft() + " right enc: " + drivetrain.talonPositionRight());
        System.out.println("left spd" + drivetrain.getLeftTalonVelocity() + "right spd: " + drivetrain.getRightTalonVelocity());

        drivetrain.setDrivetrain(left, right, rotation);
    }

    @Override
    protected boolean isFinished() {
//        System.out.println("GamePadDrive Command Terminated");
        return false;
    }
}
