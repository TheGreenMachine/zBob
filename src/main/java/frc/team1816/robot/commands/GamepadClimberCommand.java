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

        if(climberSpeed < 0) {
//            System.out.println("Elevator coast mode set");
            elevator.setCoastMode();
        } else if (elevator.getLowerLimit()){
            climberSpeed = 0;
        }
        else {
//            System.out.println("Elevator brake mode set");
            elevator.setBrakeMode();
        }

        climber.setClimberSpeed(climberSpeed);
    }


    @Override
    protected boolean isFinished() {
        return false;
    }
}
