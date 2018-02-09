package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;

public class ToggleSlowModeCommand extends Command {

    private Drivetrain drivetrain;
    private boolean slowMode;

    public ToggleSlowModeCommand(boolean slowModeToggle) {
        super("toggleslowmodecommand");
        drivetrain = Components.getInstance().drivetrain;
        this.slowMode = slowModeToggle;
        requires(drivetrain);
    }

    protected void initialize() {
        drivetrain.setSlowMode(slowMode);
    }

    protected void execute() {}

    @Override
    protected boolean isFinished() {
        return true;
    }

    protected void end() {}

    protected void interrupted() { }
}
