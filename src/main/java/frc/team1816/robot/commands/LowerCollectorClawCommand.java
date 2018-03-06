package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Collector;

public class LowerCollectorClawCommand extends Command {

    private Collector collector;
    private boolean isTeleOp;
    private double msToWait, secondsToWait;

    public LowerCollectorClawCommand(boolean isTeleOp) {
        super("lowercollectorclawcommand");
        collector = Components.getInstance().collector;
        this.isTeleOp = isTeleOp;
        this.secondsToWait = 0;
        requires(collector);
    }

    public LowerCollectorClawCommand(boolean isTeleOp, double secondsToWait) {
        super("lowercollectorclawcommand");
        collector = Components.getInstance().collector;
        this.isTeleOp = isTeleOp;
        this.secondsToWait = secondsToWait;
        requires(collector);
    }

    protected void initialize() {
        msToWait = secondsToWait * 1000 + System.currentTimeMillis();
        collector.clawLiftDown();
    }

    protected void execute() {
        System.out.println("Lowering Collector Claw");
    }

    @Override
    protected boolean isFinished() {
        if(System.currentTimeMillis() > msToWait) {
            isTeleOp = true;
        }

        return isTeleOp;
    }

    protected void end() {}

    protected void interrupted() { super.interrupted(); }
}
