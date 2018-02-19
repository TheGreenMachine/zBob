package frc.team1816.robot;

import com.edinarobotics.utils.gamepad.Gamepad;
import com.edinarobotics.utils.log.Logging;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1816.robot.commands.*;
import frc.team1816.robot.subsystems.Climber;
import frc.team1816.robot.subsystems.Collector;
import frc.team1816.robot.subsystems.Drivetrain;
import frc.team1816.robot.subsystems.Elevator;

public class Robot extends IterativeRobot {

    public static Logging logger;
    private Drivetrain drivetrain;
    private Elevator elevator;
    private Collector collector;
    private Climber climber;

    private SendableChooser<Command> autoChooser;

    private LeftAutoStartCommand leftAuto;
    private RightAutoStartCommand rightAuto;
    private LeftAutoStartScaleCommand leftScaleAuto;
    private RightAutoStartScaleCommand rightScaleAuto;

    public static NetworkTableInstance offSeasonNetworkTable;

    private NetworkTable table;

    public void robotInit() {
        Components.getInstance();
        Controls.getInstance();
        table = NetworkTable.getTable("Shuffleboard_PID");

        drivetrain = Components.getInstance().drivetrain;
        elevator = Components.getInstance().elevator;
        collector = Components.getInstance().collector;
        climber = Components.getInstance().climber;

        leftAuto = new LeftAutoStartCommand();
        rightAuto = new RightAutoStartCommand();
        leftScaleAuto = new LeftAutoStartScaleCommand();
        rightScaleAuto = new RightAutoStartScaleCommand();

        autoChooser = new SendableChooser<>();
        autoChooser.addObject("Left Start Auto", leftAuto);
        autoChooser.addObject("Right Start Auto", rightAuto);
        autoChooser.addObject("Left Start Scale Auto", leftScaleAuto);
        autoChooser.addObject("Right Start Scale Auto", rightScaleAuto);
//        autoChooser.addObject("Center Start Auto", new CenterAutoStartCommand());
        autoChooser.addDefault("Auto-Run", new DriveXInchesCommand(100, 0.8));

        SmartDashboard.putData("Autonomous", autoChooser);

         table.putNumber("Left P", drivetrain.p_L);
         table.putNumber("Left I", drivetrain.i_L);
         table.putNumber("Left D", drivetrain.d_L);
         table.putNumber("Left F", drivetrain.f_L);
         table.putNumber("Left izone", drivetrain.izone_L);

         table.putNumber("Right P", drivetrain.p_R);
         table.putNumber("Right I", drivetrain.i_R);
         table.putNumber("Right D", drivetrain.d_R);
         table.putNumber("Right F", drivetrain.f_R);
         table.putNumber("Right izone", drivetrain.izone_R);
    }

    @Override
    public void disabledInit() {
        try {
            logger.close();
            System.out.println("Logger closed");
        } catch (Exception e) {
            System.out.println("Logger not instantiated yet...");
        }
    }

    @Override
    public void autonomousInit() {
//        logger = new Logging("AutoLog");
        logger = Logging.getInstance("Autolog");

        logger.log(DriverStation.getInstance().getGameSpecificMessage());

        StringBuilder builder = new StringBuilder();
        builder.append("Current Time").append(",").append("Left Inches").append(",").append("Right Inches").append(",")
                .append("Left Velocity").append(",").append("Right Velocity").append(",").append("Set Power L").append(",")
                .append("Set Power R").append(",").append("Set Power Gyro").append(",").append("Gyro Heading");
        logger.log(builder.toString());

        drivetrain.resetEncoders();

        try {
            leftAuto.selectAuto();
            rightAuto.selectAuto();
            leftScaleAuto.selectAuto();
            rightScaleAuto.selectAuto();
        } catch (Exception e) {
            System.out.println("-----AUTO ALREADY CREATED, RUNNING PREVIOUS-----");
        }

        Command autoCommand = autoChooser.getSelected();

//        Command autoCommand = new ArcDriveCommand(48,0.4,90);
//        Command autoCommand = new ArcDriveGyroCommand(48, 0.4, 90);
//        Command autoCommand = new RotateXDegreesCommand(90);
//        Command autoCommand = new DriveXInchesCommand(240,0.75);

        System.out.println("Auto Running: " + autoCommand.getName());
        autoCommand.start();
    }

    @Override
    public void teleopInit() {
//        logger = new Logging("TeleopLog1");
        logger = Logging.getInstance("TeleopLog");

        Gamepad gamepad0 = Controls.getInstance().gamepad0;
        Gamepad gamepad1 = Controls.getInstance().gamepad1;

        drivetrain.resetEncoders();
        drivetrain.setDefaultCommand(new GamepadDriveCommand(gamepad0));
        elevator.setDefaultCommand(new GamepadElevatorCommand(gamepad1));
        climber.setDefaultCommand(new GamepadClimberCommand(gamepad1));
        collector.setDefaultCommand(new GamepadCollectorCommand(gamepad1));

        double pValueL = table.getDouble("Left P", drivetrain.p_L);
        double iValueL = table.getDouble("Left I", drivetrain.i_L);
        double dValueL = table.getDouble("Left D", drivetrain.d_L);
        double fValueL = table.getDouble("Left F", drivetrain.f_L);
        double izoneL = table.getDouble("Left izone", drivetrain.izone_L);

        double pValueR = table.getDouble("Right P", drivetrain.p_R);
        double iValueR = table.getDouble("Right I", drivetrain.i_R);
        double dValueR = table.getDouble("Right D", drivetrain.d_R);
        double fValueR = table.getDouble("Right F", drivetrain.f_R);
        double izoneR = table.getDouble("Right izone", drivetrain.izone_R);

        drivetrain.updatePIDValuesL(pValueL, iValueL, dValueL, fValueL, (int) izoneL);
        drivetrain.updatePIDValuesR(pValueR, iValueR, dValueR, fValueR, (int) izoneR);

    }

    @Override
    public void testInit() {

    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void autonomousPeriodic() {
        logger.log(drivetrain.getLogString());
        Scheduler.getInstance().run();
    }

    public void teleopPeriodic() {
//        System.out.println("L Velocity (ticks/100ms): " + drivetrain.getLeftTalonVelocity());
//        System.out.println("R Velocity (ticks/100ms): " + drivetrain.getRightTalonVelocity());
//        System.out.println("Left Ticks (grayhill): " + drivetrain.talonPositionLeft());
//        System.out.println("Right Ticks (grayhill): " + drivetrain.talonPositionRight());
//        System.out.println("Gyro: " + drivetrain.getGyroAngle());
//        System.out.println("Gyro Status: " + drivetrain.gyroActiveCheck());

//        System.out.println("Elevator ticks: " + elevator.getTicks());

//        System.out.println("left talon v " + drivetrain.getLeftTalonVelocity());
//        System.out.println("right talon v" + drivetrain.getRightTalonVelocity());
//        System.out.println("elevator out voltage" + elevator.getElevatorOutputVoltage());

        Scheduler.getInstance().run();
    }

    @Override
    public void testPeriodic() {
        LiveWindow.run();
    }



    public void closeLogger() {
        logger.close();
    }
}
