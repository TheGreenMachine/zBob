package frc.team1816.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.team1816.robot.subsystems.Collector;
import frc.team1816.robot.subsystems.Drivetrain;
import frc.team1816.robot.subsystems.Elevator;

public class Components {
    private static Components instance;

    public Drivetrain drivetrain;
    public Collector collector;
    public AnalogInput ai;
    public Elevator elevator;

    private static final int RIGHT_MAIN = 7;
    private static final int RIGHT_SLAVE_ONE = 8;
    private static final int RIGHT_SLAVE_TWO = 9;
    private static final int LEFT_MAIN = 1;
    private static final int LEFT_SLAVE_ONE = 2;
    private static final int LEFT_SLAVE_TWO = 3;

    private static final int COLLECTOR_LEFT = 4;
    private static final int COLLECTOR_RIGHT = 5;

    private static final int ELEVATOR_MAIN = 6;

    private static final int UPPER_LIMIT = 0; //optoswitch
    private static final int LOWER_LIMIT = 1; //optoswitch

    public Components(){
        drivetrain = new Drivetrain(RIGHT_MAIN, RIGHT_SLAVE_ONE, RIGHT_SLAVE_TWO, LEFT_MAIN, LEFT_SLAVE_ONE, LEFT_SLAVE_TWO);
        collector = new Collector(COLLECTOR_RIGHT, COLLECTOR_LEFT);
        ai = new AnalogInput(3);
        elevator = new Elevator(ELEVATOR_MAIN, UPPER_LIMIT, LOWER_LIMIT);
    }

    public static Components getInstance(){
        if (instance == null){
            instance = new Components();
        }
        return instance;
    }


}
