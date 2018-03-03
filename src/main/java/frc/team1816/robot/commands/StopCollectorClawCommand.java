package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Collector;

public class StopCollectorClawCommand extends Command {

    private Collector collector;

    public StopCollectorClawCommand() {
        super("lowercollectorclawcommand");
        collector = Components.getInstance().collector;
        requires(collector);
    }

    protected void initialize() {
        collector.clawLiftStop();
    }

    protected void execute() {}

    @Override
    protected boolean isFinished() {
        return true;
    }

    protected void end() {}

    protected void interrupted() { super.interrupted(); }
}
