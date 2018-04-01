package frc.team1816.robot.commands;

import com.edinarobotics.utils.gamepad.Gamepad;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Collector;
import frc.team1816.robot.subsystems.Elevator;


public class RotateCubeCommand extends Command {
    private Collector collector;

    public RotateCubeCommand() {
        super("rotatecubecommand");
        this.collector = Components.getInstance().collector;

        requires(collector);
    }

    @Override
    protected void initialize() {
        collector.setCollectorSpeed(0.33,-0.66);
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
