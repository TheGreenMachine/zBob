package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;

public class ToggleDrivetrainVbusMode extends Command {

    private Drivetrain drivetrain;
    private boolean vBusEnabled;

    public ToggleDrivetrainVbusMode(boolean vbusEnabled) {
        super("toggledrivetrianvbusmode");
        drivetrain = Components.getInstance().drivetrain;
        this.vBusEnabled = vbusEnabled;
        requires(drivetrain);
    }

    protected void initialize() {
        drivetrain.setVbusMode(vBusEnabled);
    }

    protected void execute() {}

    @Override
    protected boolean isFinished() {
        return true;
    }

    protected void end() {}

    protected void interrupted() {
        end();
    }
}
