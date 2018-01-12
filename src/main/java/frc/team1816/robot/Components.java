package frc.team1816.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.team1816.robot.subsystems.Drivetrain;

public class Components {
    private static Components instance;

    public Drivetrain drivetrain;
    public AnalogInput ai;

    private static final int RIGHT_MAIN = 1;
    private static final int RIGHT_SLAVE_ONE = 2;
    private static final int RIGHT_SLAVE_TWO = 3;
    private static final int LEFT_MAIN = 4;
    private static final int LEFT_SLAVE_ONE = 5;
    private static final int LEFT_SLAVE_TWO = 6;

    private static final int PORT_CHANNEL = 0;

    public Components(){
        drivetrain = new Drivetrain(RIGHT_MAIN, RIGHT_SLAVE_ONE, RIGHT_SLAVE_ONE, LEFT_MAIN, LEFT_SLAVE_ONE, LEFT_SLAVE_TWO);
        ai = new AnalogInput(PORT_CHANNEL);
    }

    public static Components getInstance(){
        if (instance == null){
            instance = new Components();
        }
        return instance;
    }


}
