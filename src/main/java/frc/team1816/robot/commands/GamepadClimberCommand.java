package frc.team1816.robot.commands;

import com.edinarobotics.utils.gamepad.Gamepad;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Climber;
import frc.team1816.robot.subsystems.Elevator;

public class GamepadClimberCommand extends Command {

    private Climber climber;
    private Elevator elevator;
    private Gamepad gamepad;
    private double climberSpeed;

    public GamepadClimberCommand(Gamepad gamepad) {
        super("gamepadelevatorcommand");
        this.climber = Components.getInstance().climber;
        this.elevator = Components.getInstance().elevator;
        this.gamepad = gamepad;
        requires(climber);
    }

    @Override
    protected void initialize() {
//        System.out.println("Initialized GamepadElevatorCommand");
    }

    @Override
    protected void execute() {
        climberSpeed = - gamepad.getLeftY();

        if(elevator.getHeightPercent() > 15){
            climber.setClimberSpeed(climberSpeed);
        }
    }


    @Override
    protected boolean isFinished() {
        return false;
    }
}
