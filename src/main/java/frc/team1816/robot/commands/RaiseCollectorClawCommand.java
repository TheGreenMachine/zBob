package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Collector;

public class RaiseCollectorClawCommand extends Command {

    private Collector collector;

    public RaiseCollectorClawCommand() {
        super("raisecollectorclawcommand");
        collector = Components.getInstance().collector;
        requires(collector);
    }

    protected void initialize() {
        collector.clawLiftUp();
    }

    protected void execute() {
        System.out.println("Raising Collector Claw");
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    protected void end() {}

    protected void interrupted() { super.interrupted(); }
}
