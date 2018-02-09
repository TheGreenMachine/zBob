package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Collector;

public class ToggleCollectorCommand extends Command {

    private Collector collector;
    private boolean collectorToggle;

    public ToggleCollectorCommand(boolean collectorToggle) {
        super("toggleslowmodecommand");
        collector = Components.getInstance().collector;
        requires(collector);
    }

    protected void initialize() {
        collector.toggleCollector(collectorToggle);
    }

    protected void execute() {}

    @Override
    protected boolean isFinished() {
        return true;
    }

    protected void end() {}

    protected void interrupted() { }
}
