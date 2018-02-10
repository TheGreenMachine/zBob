package frc.team1816.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import frc.team1816.robot.subsystems.Climber;
import frc.team1816.robot.subsystems.Collector;
import frc.team1816.robot.subsystems.Drivetrain;
import frc.team1816.robot.subsystems.Elevator;

public class Components {
    private static Components instance;

    public Drivetrain drivetrain;
    public Collector collector;
    public AnalogInput ai;
    public Elevator elevator;
    public Climber climber;
    public Compressor compressor;

    //Drivetrain Constants
    private static final int RIGHT_MAIN = 6;
    private static final int RIGHT_SLAVE_ONE = 5;
//    private static final int RIGHT_SLAVE_TWO = 7;
    private static final int LEFT_MAIN = 14;
    private static final int LEFT_SLAVE_ONE = 13;
//    private static final int LEFT_SLAVE_TWO = 12;

    //Collector Constants
    private static final int COLLECTOR_LEFT = 2;
    private static final int COLLECTOR_RIGHT = 4;
    private static final int COLLECTOR_SOLENOID_FRONT = 0;

    //Elevator Constants
    private static final int ELEVATOR_MAIN = 1;
    private static final int ELEVATOR_SLAVE = 7;
    private static final int ELEVATOR_ENC_1 = 0;
    private static final int ELEVATOR_ENC_2 = 1;

    private static final int UPPER_LIMIT = 9;
    private static final int LOWER_LIMIT = 8;

    //Climber Constants
    private static final int CLIMBER_ONE = 8;
    private static final int CLIMBER_TWO = 9;
    private static final int CLIMBER_THREE = 10;
    private static final int CLIMBER_FOUR = 11;
    private static final int CLIMBER_SOLENOID = 1;

    //Compressor
    private static final int PCM_NODE_ID = 10;

    public Components(){
        drivetrain = new Drivetrain(RIGHT_MAIN, RIGHT_SLAVE_ONE, LEFT_MAIN, LEFT_SLAVE_ONE);
        collector = new Collector(COLLECTOR_LEFT, COLLECTOR_RIGHT, COLLECTOR_SOLENOID_FRONT, PCM_NODE_ID);
        ai = new AnalogInput(3);
        elevator = new Elevator(ELEVATOR_MAIN, ELEVATOR_SLAVE, ELEVATOR_ENC_1, ELEVATOR_ENC_2, UPPER_LIMIT, LOWER_LIMIT);
        climber = new Climber(CLIMBER_ONE, CLIMBER_TWO, CLIMBER_THREE, CLIMBER_FOUR, CLIMBER_SOLENOID, PCM_NODE_ID);

        compressor = new Compressor(PCM_NODE_ID);
        compressor.start();
    }

    public static Components getInstance(){
        if (instance == null){
            instance = new Components();
        }
        return instance;
    }


}
