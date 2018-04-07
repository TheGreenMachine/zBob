package frc.team1816.robot.commands;

import com.edinarobotics.utils.gamepad.Gamepad;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Collector;
import frc.team1816.robot.subsystems.Elevator;


public class GamepadCollectorCommand extends Command {
    private Collector collector;
    private Elevator elevator;
    private Gamepad operator, driver;

    private double lpower;
    private double rpower;
    private double clawSpeed;
    private boolean belowCloseThreshold = true;

    public GamepadCollectorCommand(Gamepad op, Gamepad driver) {
        super("gamepadcollectorcommand");
        this.collector = Components.getInstance().collector;
        this.elevator = Components.getInstance().elevator;
        this.operator = op;
        this.driver = driver;

        requires(collector);
    }

    @Override
    protected void initialize() {
//        System.out.println("Initialized GamepadCollectorCommand");
    }

    @Override
    protected void execute() {
        if(operator.leftTrigger().get() ) {
            lpower = 1;
            rpower = 1;
        }
        else if (driver.rightTrigger().get()){
            lpower = .5;
            rpower = .5;
        }
        else if (operator.rightTrigger().get()) {
            lpower = -1;
            rpower = -1;
        } else {
            lpower = 0;
            rpower = 0;
        }

        if(operator.leftBumper().get()) {
            clawSpeed = -0.2;
        } else if (operator.rightBumper().get()) {
            clawSpeed = 0.2;
        } else {
            clawSpeed = 0;
        }

        collector.setClawSpeed(clawSpeed);
        collector.setCollectorSpeed(lpower, rpower);

        if(elevator.getHeightPercent() > 5 && belowCloseThreshold) {
            belowCloseThreshold = false;
        } else if(elevator.getHeightPercent() < 5) {
            belowCloseThreshold = true;
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
