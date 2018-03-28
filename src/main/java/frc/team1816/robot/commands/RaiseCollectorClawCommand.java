package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Collector;

public class RaiseCollectorClawCommand extends Command {

    private Collector collector;
    private boolean isTeleOp;
    private double msToWait, secondsToWait;


    public RaiseCollectorClawCommand(boolean isTeleOp, double secondsToWait) {
        super("raisecollectorclawcommand");
        collector = Components.getInstance().collector;
        this.isTeleOp = isTeleOp;
        this.secondsToWait = secondsToWait;
        requires(collector);
    }

    protected void initialize() {
        msToWait = secondsToWait * 1000 + System.currentTimeMillis();
        collector.clawLiftUp();
    }

    protected void execute() {
        System.out.println("Raising Collector Claw");
    }

    @Override
    protected boolean isFinished() {
        if(System.currentTimeMillis() > msToWait) {
            isTeleOp = true;
        }

        return isTeleOp;
    }

    protected void end() {
        collector.clawLiftStop();
    }

    protected void interrupted() { super.interrupted(); }
}
