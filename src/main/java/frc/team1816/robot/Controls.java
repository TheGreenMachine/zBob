package frc.team1816.robot;

import com.edinarobotics.utils.gamepad.FilteredGamepad;
import com.edinarobotics.utils.gamepad.Gamepad;
import com.edinarobotics.utils.gamepad.gamepadfilters.DeadzoneFilter;
import com.edinarobotics.utils.gamepad.gamepadfilters.GamepadFilter;
import com.edinarobotics.utils.gamepad.gamepadfilters.GamepadFilterSet;
import com.edinarobotics.utils.gamepad.gamepadfilters.PowerFilter;

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
    }

    public static Controls getInstance(){
        if (instance == null){
            instance = new Controls();
        }
        return instance;
    }
}
