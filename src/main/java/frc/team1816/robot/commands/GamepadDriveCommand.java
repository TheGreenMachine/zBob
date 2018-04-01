package frc.team1816.robot.commands;

import com.edinarobotics.utils.gamepad.Gamepad;
import com.edinarobotics.utils.log.Logger;
import com.edinarobotics.utils.log.Logging;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.Robot;
import frc.team1816.robot.subsystems.Collector;
import frc.team1816.robot.subsystems.Drivetrain;
import frc.team1816.robot.subsystems.Elevator;

public class GamepadDriveCommand extends Command {

    private Drivetrain drivetrain;
    private Collector collector;
    private Gamepad gamepad;
    public StringBuilder sb;
    public double prevPowerL = 0, prevPowerR = 0;
    public static final double SET_SPEED_DIFF_MAX = 0.04;

    public GamepadDriveCommand(Gamepad gamepad) {
        super("gamepaddrivecommand");
        this.drivetrain = Components.getInstance().drivetrain;
        this.collector = Components.getInstance().collector;
        this.gamepad = gamepad;
        requires(drivetrain);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        double rightPower = gamepad.getLeftY();
        double leftPower = gamepad.getLeftY();
        double rotation = gamepad.getRightX();

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

        if(Math.abs(gamepad.getLeftY()) < 0.03) {
            drivetrain.setDrivetrainPercent(leftPower, rightPower, rotation);
        }
        else {
            drivetrain.setDrivetrain(leftPower, rightPower, rotation);
        }
    }

    @Override
    protected boolean isFinished() {
//        System.out.println("GamePadDrive Command Terminated");
        return false;
    }
}
