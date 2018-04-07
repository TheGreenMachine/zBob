package frc.team1816.robot.commands;

import com.edinarobotics.utils.gamepad.ThreeAxisJoystick;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.Robot;
import frc.team1816.robot.subsystems.Collector;
import frc.team1816.robot.subsystems.Drivetrain;

public class JoystickDriveCommand extends Command {

    private Drivetrain drivetrain;
    private ThreeAxisJoystick joystick;

    public double prevPowerL = 0, prevPowerR = 0;
    public static final double SET_SPEED_DIFF_MAX = 0.04;

    public JoystickDriveCommand(ThreeAxisJoystick joystick) {
        super("joystickdrivecommand");
        this.drivetrain = Components.getInstance().drivetrain;
        this.joystick = joystick;
        requires(drivetrain);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        double leftPower = joystick.getY();
        double rightPower = joystick.getY();
        double rotation = joystick.getTwist();

        if(rotation == 0 || leftPower != 0) {
            //Acceleration curve in teleop
            if (Math.abs(leftPower - prevPowerL) > SET_SPEED_DIFF_MAX && leftPower != prevPowerL) {
                if (leftPower > prevPowerL) {
                    leftPower = prevPowerL + SET_SPEED_DIFF_MAX;
                } else if (leftPower < prevPowerL) {
                    leftPower = prevPowerL - SET_SPEED_DIFF_MAX;
                }
            }

            if (Math.abs(rightPower - prevPowerR) > SET_SPEED_DIFF_MAX && rightPower != prevPowerR) {
                if (rightPower > prevPowerR) {
                    rightPower = prevPowerR + SET_SPEED_DIFF_MAX;
                } else if (rightPower < prevPowerR) {
                    rightPower = prevPowerR - SET_SPEED_DIFF_MAX;
                }
            }
        }

        prevPowerL = leftPower;
        prevPowerR = rightPower;

        if(joystick.getHatSwitchX() < 0) {
            drivetrain.setDrivetrain(0.5,0.5,1);
        } else if(joystick.getHatSwitchX() > 0) {
            drivetrain.setDrivetrain(0.5,0.5,-1);
        } else if(Math.abs(joystick.getY()) < 0.03 || drivetrain.isVbusEnabled()) {
            drivetrain.setDrivetrainPercent(leftPower, rightPower, rotation);
        } else {
            drivetrain.setDrivetrain(leftPower, rightPower, rotation);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
