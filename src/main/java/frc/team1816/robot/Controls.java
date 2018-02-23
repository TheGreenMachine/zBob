package frc.team1816.robot;

import com.edinarobotics.utils.gamepad.FilteredGamepad;
import com.edinarobotics.utils.gamepad.Gamepad;
import com.edinarobotics.utils.gamepad.gamepadfilters.DeadzoneFilter;
import com.edinarobotics.utils.gamepad.gamepadfilters.GamepadFilter;
import com.edinarobotics.utils.gamepad.gamepadfilters.GamepadFilterSet;
import com.edinarobotics.utils.gamepad.gamepadfilters.PowerFilter;
import frc.team1816.robot.commands.*;

import java.util.ArrayList;
import java.util.List;

public class Controls {
    private static Controls instance;

    public Gamepad gamepad0;
    public Gamepad gamepad1;

    public Controls() {
        List<GamepadFilter> gamepadFilter0 = new ArrayList<>();
        gamepadFilter0.add(new DeadzoneFilter(0.05));
        gamepadFilter0.add(new PowerFilter(2));
        GamepadFilterSet driveGamepadFilterSet0 = new GamepadFilterSet(gamepadFilter0);

        gamepad0 = new FilteredGamepad(0, driveGamepadFilterSet0);
        gamepad1 = new FilteredGamepad(1, driveGamepadFilterSet0);

        //PID Tuning Controls
        gamepad0.diamondLeft().whileHeld(new FullPowerDriveCommand(14,13));
        gamepad0.diamondRight().whileHeld(new FullPowerDriveCommand(5,4));

        //Driver Controller Mapping
        gamepad0.leftBumper().whenPressed(new ToggleSlowModeCommand(true));
        gamepad0.leftBumper().whenReleased(new ToggleSlowModeCommand(false));

        //Operator Controller Mapping
        gamepad1.leftBumper().whenPressed(new ToggleCollectorCommand(true));
        gamepad1.rightBumper().whenReleased(new ToggleCollectorCommand(false));

        gamepad1.diamondLeft().whenPressed(new ToggleClimberShifterCommand(true));
        gamepad1.diamondRight().whenPressed(new ToggleClimberShifterCommand(false));

        gamepad1.dPadUp().whenPressed(new RaiseElevatorCommand());
        gamepad1.dPadDown().whenPressed(new LowerElevatorCommand());
        gamepad1.dPadLeft().whenPressed(new SetElevatorHeightPercentCommand(30));
        gamepad1.dPadRight().whenPressed(new SetElevatorHeightPercentCommand(60));

//        Ramp Deployal (Operator Gamepad)
//        Double button failsafe not working
//
//        if(gamepad0.middleLeft().get() && gamepad0.middleRight().get()){
//            System.out.println("Ramps triggered");
//            new DeployRampCommand();
//        }

//        gamepad0.diamondUp().whenPressed(new DeployRampCommand());

        gamepad0.middleRight().whenPressed(new DeployRampCommand());
        gamepad0.middleRight().whenReleased(new ResetRampsCommand());

    }

    public static Controls getInstance(){
        if (instance == null){
            instance = new Controls();
        }
        return instance;
    }
}
