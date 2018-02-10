package frc.team1816.robot.commands;

import com.edinarobotics.utils.gamepad.Gamepad;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Collector;


public class GamepadCollectorCommand extends Command {
    private Collector collector;
    private Gamepad gamepad;

    private double lpower;
    private double rpower;
    private boolean collectorOpen;

    public GamepadCollectorCommand(Gamepad gamepad) {
        super("gamepadcollectorcommand");
        this.collector = Components.getInstance().collector;
        this.gamepad = gamepad;

        requires(collector);
    }

    @Override
    protected void initialize() {
//        System.out.println("Initialized GamepadCollectorCommand");
    }

    @Override
    protected void execute() {
        if(gamepad.leftTrigger().get()) {
            lpower = 1;
            rpower = 1;
        } else if (gamepad.rightTrigger().get()) {
            lpower = -1;
            rpower = -1;
        } else {
            lpower = 0;
            rpower = 0;
        }

        collector.setCollectorSpeed(lpower, rpower);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}