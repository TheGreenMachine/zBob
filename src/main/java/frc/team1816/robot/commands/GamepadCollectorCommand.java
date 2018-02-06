package frc.team1816.robot.commands;

import com.edinarobotics.utils.gamepad.Gamepad;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Collector;


public class GamepadCollectorCommand extends Command {
    private Collector collector;
    private Gamepad gamepad;

    private double power;
    private boolean collectorOpen;

    public GamepadCollectorCommand(Gamepad gamepad) {
        super("gamepadcollectorcommand");
        this.collector = Components.getInstance().collector;
        this.gamepad = gamepad;

        requires(collector);
    }

    @Override
    protected void initialize() {
        System.out.println("Initialized GamepadCollectorCommand");
    }

    @Override
    protected void execute() {
        if(gamepad.leftTrigger().get()) {
            power = 0.75;
        } else if (gamepad.rightTrigger().get()) {
            power = -0.75;
        } else {
            power = 0;
        }

        collector.setCollectorSpeed(power);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
