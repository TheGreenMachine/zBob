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

        gamepad0.leftBumper().whenPressed(new ToggleSlowModeCommand(true));
        gamepad0.leftBumper().whenReleased(new ToggleSlowModeCommand(false));

        gamepad1.diamondUp().whenPressed(new ToggleCollectorCommand(true));
        gamepad1.diamondDown().whenReleased(new ToggleCollectorCommand(false));

        gamepad1.dPadLeft().whenPressed(new RaiseElevatorCommand());
        gamepad1.dPadRight().whenPressed(new LowerElevatorCommand());

        gamepad1.rightBumper().whenPressed(new SetCollectorSpeedCommand(.75));
        gamepad1.rightBumper().whenReleased(new SetCollectorSpeedCommand(0));

        gamepad1.leftBumper().whenPressed(new SetCollectorSpeedCommand(-.75));
        gamepad1.leftBumper().whenReleased(new SetCollectorSpeedCommand(0));

        gamepad1.dPadUp().whileHeld(new TalonTestCommand(8, 0.5));
    }

    public static Controls getInstance(){
        if (instance == null){
            instance = new Controls();
        }
        return instance;
    }
}
