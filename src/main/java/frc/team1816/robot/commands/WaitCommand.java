package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public class WaitCommand extends Command {
    private double secondsToWait;
    private double millisecondsToWait;

    public WaitCommand(double secondsToWait) {
        super("waitcommand");
        this.secondsToWait = secondsToWait;
    }

    @Override
    protected void initialize() {
        millisecondsToWait = System.currentTimeMillis() + secondsToWait * 1000;
        System.out.println("Waiting...");
    }

    @Override
    protected boolean isFinished() {
        if (System.currentTimeMillis() < millisecondsToWait) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void end() {
        System.out.println("Finished waiting");
    }
}
