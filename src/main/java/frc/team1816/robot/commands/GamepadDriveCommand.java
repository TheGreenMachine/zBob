package frc.team1816.robot.commands;

import com.edinarobotics.utils.gamepad.Gamepad;
import com.edinarobotics.utils.log.Logging;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
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
        double right = gamepad.getLeftY();
        double left = gamepad.getLeftY();
        double rotation = gamepad.getRightX();
        //System.out.println(right);
        //System.out.println(left);

        drivetrain.setDrivetrain(left, right, rotation);

//        System.out.println("Left Ticks: " + drivetrain.talonPositionLeft());
//        System.out.println("Right Ticks: " + drivetrain.talonPositionRight());
    }

    @Override
    protected boolean isFinished() {
//        System.out.println("GamePadDrive Command Terminated");
        return false;
    }
}
