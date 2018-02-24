package frc.team1816.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;
import frc.team1816.robot.subsystems.Elevator;

public class FullPowerDriveCommand extends Command {

    private Drivetrain drivetrain;
    private double power = 1;

    public FullPowerDriveCommand() {
        super("fullpowerdrivecommand");
        drivetrain = Components.getInstance().drivetrain;
    }

    public FullPowerDriveCommand(double power) {
        super("fullpowerdrivecommand");
        drivetrain = Components.getInstance().drivetrain;
        this.power = power;
    }

    protected void initialize() {

    }

    protected void execute() {
        drivetrain.setDrivetrain(power, power);
        System.out.println("L Set Pow: " + power + "\tL Out Pow: " + drivetrain.getLeftTalonVelocity());
        System.out.println("R Set Pow: " + power + "\tR Out Pow: " + drivetrain.getRightTalonVelocity());

    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    protected void end() {
        drivetrain.setDrivetrain(0,0);
    }

    protected void interrupted() {
        end();
    }
}
