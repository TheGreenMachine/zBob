package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Collector;

public class SetCollectorSpeedCommand extends Command {
    private double power;
    private Collector collector;

    public SetCollectorSpeedCommand(double power) {
        super("setcollectorspeedcommand");
        this.power = power;
        collector = Components.getInstance().collector;
        requires(collector);
    }

    @Override
    protected void initialize() {
        collector.setCollectorSpeed(power);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void interrupted() {
        collector.setCollectorSpeed(0);
        System.out.println("INTERRUPTED!!!");
    }
}
