package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Collector;

public class LowerCollectorClawCommand extends Command {

    private Collector collector;

    public LowerCollectorClawCommand() {
        super("lowercollectorclawcommand");
        collector = Components.getInstance().collector;
        requires(collector);
    }

    protected void initialize() {
        collector.clawLiftDown();
    }

    protected void execute() {
        System.out.println("Lowering Collector Claw");
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    protected void end() {}

    protected void interrupted() { super.interrupted(); }
}
