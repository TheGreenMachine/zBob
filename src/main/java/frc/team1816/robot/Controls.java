package frc.team1816.robot;

import com.edinarobotics.utils.gamepad.Gamepad;

public class Controls {
    private static Controls instance;

    public Gamepad gamepad0;
    public Gamepad gamepad1;

    public static Controls getInstance(){
        if (instance == null){
            instance = new Controls();
        }
        return instance;
    }
}
