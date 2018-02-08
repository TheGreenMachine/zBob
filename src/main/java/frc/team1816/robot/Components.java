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

    private static final int RIGHT_MAIN = 6;
    private static final int RIGHT_SLAVE_ONE = 5;
    private static final int RIGHT_SLAVE_TWO = 7;
    private static final int LEFT_MAIN = 14;
    private static final int LEFT_SLAVE_ONE = 13;
    private static final int LEFT_SLAVE_TWO = 12;

    private static final int COLLECTOR_LEFT = 2;
    private static final int COLLECTOR_RIGHT = 4;
    private static final int COLLECTOR_SOLENOID_FRONT = 5;

    private static final int ELEVATOR_MAIN = 1;
    private static final int ELEVATOR_ENC_1 = 1;
    private static final int ELEVATOR_ENC_2 = 2;
    private static final int CLIMBER_SOLENOID = 4;

    private static final int CLIMBER_ONE = 8;
    private static final int CLIMBER_TWO = 9;
    private static final int CLIMBER_THREE = 10;
    private static final int CLIMBER_FOUR = 11;

    private static final int UPPER_LIMIT = 9;
    private static final int LOWER_LIMIT = 8;

    public Components(){
        drivetrain = new Drivetrain(RIGHT_MAIN, RIGHT_SLAVE_ONE, RIGHT_SLAVE_TWO, LEFT_MAIN, LEFT_SLAVE_ONE, LEFT_SLAVE_TWO);
        collector = new Collector(COLLECTOR_LEFT, COLLECTOR_RIGHT, COLLECTOR_SOLENOID_FRONT);
        ai = new AnalogInput(3);
        elevator = new Elevator(ELEVATOR_MAIN, ELEVATOR_ENC_1, ELEVATOR_ENC_2, UPPER_LIMIT, LOWER_LIMIT, CLIMBER_SOLENOID,
                CLIMBER_ONE, CLIMBER_TWO, CLIMBER_THREE, CLIMBER_FOUR);
    }

    public static Components getInstance(){
        if (instance == null){
            instance = new Components();
        }
        return instance;
    }


}
