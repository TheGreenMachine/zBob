package frc.team1816.robot;

import com.edinarobotics.utils.gamepad.Gamepad;
import com.edinarobotics.utils.log.Logging;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1816.robot.commands.*;
import frc.team1816.robot.subsystems.*;

public class Robot extends TimedRobot {

    public static Logging logger;
    public static Logging posLog;

    private Drivetrain drivetrain;
    private Elevator elevator;
    private Collector collector;
    private Climber climber;
    private Ramp ramp;

    private Gamepad gamepad0, gamepad1;

    private SendableChooser<Command> autoChooser;

    private LeftAutoStartSwitchCommand leftSwitchAuto;
    private RightAutoStartSwitchCommand rightSwitchAuto;
    private LeftAutoStartScaleCommand leftScaleAuto;
    private RightAutoStartScaleCommand rightScaleAuto;
    private LeftAutoStartCommand leftAuto;
    private RightAutoStartCommand rightAuto;
    private LeftAutoStartNearCommand leftAutoNearOnly;
    private RightAutoStartNearCommand rightAutoNearOnly;
    private LeftAutoStartAvoidanceCommand leftAvoidAuto;
    private RightAutoStartAvoidanceCommand rightAvoidAuto;
    private CenterAutoStartSwitchCommand centerSwitchAuto;


    private NetworkTable table;
    private NetworkTable velocityGraph;

    public void robotInit() {
        Components.getInstance();
        Controls.getInstance();
        table = NetworkTableInstance.getDefault().getTable("Shuffleboard_PID");
        velocityGraph = NetworkTableInstance.getDefault().getTable("Velocity Graph");

        drivetrain = Components.getInstance().drivetrain;
        elevator = Components.getInstance().elevator;
        collector = Components.getInstance().collector;
        climber = Components.getInstance().climber;
        ramp = Components.getInstance().ramp;

        gamepad0 = Controls.getInstance().gamepad0;
        gamepad1 = Controls.getInstance().gamepad1;

        leftSwitchAuto = new LeftAutoStartSwitchCommand();
        rightSwitchAuto = new RightAutoStartSwitchCommand();
        leftScaleAuto = new LeftAutoStartScaleCommand();
        rightScaleAuto = new RightAutoStartScaleCommand();
        leftAuto = new LeftAutoStartCommand();
        rightAuto = new RightAutoStartCommand();
        leftAutoNearOnly = new LeftAutoStartNearCommand();
        rightAutoNearOnly = new RightAutoStartNearCommand();
        leftAvoidAuto = new LeftAutoStartAvoidanceCommand();
        rightAvoidAuto = new RightAutoStartAvoidanceCommand();
        centerSwitchAuto = new CenterAutoStartSwitchCommand();


        autoChooser = new SendableChooser<>();
        autoChooser.addObject("Left Start Switch Auto", leftSwitchAuto);
        autoChooser.addObject("Right Start Switch Auto", rightSwitchAuto);
        autoChooser.addObject("Left Start Scale Auto", leftScaleAuto);
        autoChooser.addObject("Right Start Scale Auto", rightScaleAuto);
        autoChooser.addObject("Left Start NearSw-NearSc-FarSw", leftAuto);
        autoChooser.addObject("Right Start NearSw-NearSc-FarSw", rightAuto);
        autoChooser.addObject("Left Start Near-Side Only", leftAutoNearOnly);
        autoChooser.addObject("Right Start Near-Side Only", rightAutoNearOnly);
        autoChooser.addObject("Center Start Switch Auto", centerSwitchAuto);
        autoChooser.addObject("Left Start Scale Avoidance Auto", leftAvoidAuto);
        autoChooser.addObject("Right Start Scale Avoidance Auto", rightAvoidAuto);
        autoChooser.addDefault("Auto-Run", new DriveXInchesCommand(100, 0.8));
        autoChooser.addObject("Wait (debugging only)", new WaitCommand(1));
        autoChooser.addObject("ArcDrive Enc", new ArcDriveCommand(50,.3,90));
        autoChooser.addObject("ArcDrive Gyro", new ArcDriveGyroCommand(50,.3,90));

        SmartDashboard.putData("Autonomous", autoChooser);

        table.getEntry("kP").setDouble(drivetrain.kP);
        table.getEntry("kI").setDouble(drivetrain.kI);
        table.getEntry("kD").setDouble(drivetrain.kD);
        table.getEntry("kF").setDouble(drivetrain.kF);
        table.getEntry("izone").setDouble(drivetrain.izone);

        velocityGraph.getEntry("Left Velocity").setDouble(0);
        velocityGraph.getEntry("Left Set V").setDouble(0);
        velocityGraph.getEntry("Right Velocity").setDouble(0);
        velocityGraph.getEntry("Right Set V").setDouble(0);
    }

    @Override
    public void disabledInit() {
        try {
            logger.close();
            posLog.close();
            System.out.println("Logger closed");
        } catch (Exception e) {
            System.out.println("Logger not instantiated yet...");
        }
    }

    @Override
    public void autonomousInit() {
        logger = Logging.getInstance("Autolog");
        posLog = Logging.getInstance("PosLog");
        posLog.log("auto init");

        drivetrain.setDrivetrainBrakeMode();
        drivetrain.initCoordinateTracking();

        StringBuilder builder = new StringBuilder();
        builder.append("Current Time").append(",").append("Left Inches").append(",").append("Right Inches").append(",")
                .append("Left Velocity").append(",").append("Right Velocity").append(",").append("Set Power L").append(",")
                .append("Set Power R").append(",").append("Set Power Gyro").append(",").append("Gyro Heading");
        logger.log(builder.toString());

        drivetrain.resetEncoders();

        double initTime = System.currentTimeMillis();
        String FMSmessage = null;

        do {
            try {
                FMSmessage = DriverStation.getInstance().getGameSpecificMessage();
                System.out.println("Waiting For FMS Data");

            } catch (Exception e) {
                System.out.println("target not found");
            }
        } while ((FMSmessage == null || FMSmessage.equals("")) && System.currentTimeMillis() - initTime < 1000);

        System.out.println("FMS Data: " + FMSmessage);
        logger.log(FMSmessage);

        try {
            leftSwitchAuto.selectAuto(FMSmessage);
            rightSwitchAuto.selectAuto(FMSmessage);
            leftScaleAuto.selectAuto(FMSmessage);
            rightScaleAuto.selectAuto(FMSmessage);
            leftAuto.selectAuto(FMSmessage);
            rightAuto.selectAuto(FMSmessage);
            leftAvoidAuto.selectAuto(FMSmessage);
            rightAvoidAuto.selectAuto(FMSmessage);
            centerSwitchAuto.selectAuto(FMSmessage);
        } catch (Exception e) {
            System.out.println("-----AUTO ALREADY CREATED, RUNNING PREVIOUS-----");
        }

        Command autoCommand = autoChooser.getSelected();

//        Command autoCommand = new ArcDriveCommand(48,0.4,90);
//        Command autoCommand = new ArcDriveGyroCommand(48, 0.4, 90);
//        Command autoCommand = new RotateXDegreesCommand(-90,true);
//        Command autoCommand = new DriveXInchesCommand(240,0.75);
//        Command autoCommand = new setElevatorHeightPercentCommand(75);
        System.out.println("Auto Running: " + autoCommand.getName());
        autoCommand.start();
    }

    @Override
    public void teleopInit() {
        logger = Logging.getInstance("TeleopLog");
        posLog = Logging.getInstance("PosLog");
        posLog.log("teleop init");

        drivetrain.setDrivetrainCoastMode();
        drivetrain.resetEncoders();
        drivetrain.setDefaultCommand(new GamepadDriveCommand(gamepad0));
        elevator.setDefaultCommand(new GamepadElevatorCommand(gamepad1));
        climber.setDefaultCommand(new GamepadClimberCommand(gamepad1));
        collector.setDefaultCommand(new GamepadCollectorCommand(gamepad1, gamepad0));

        double pValue = table.getEntry("kP").getDouble(drivetrain.kP);
        double iValue = table.getEntry("kI").getDouble(drivetrain.kI);
        double dValue = table.getEntry("kD").getDouble(drivetrain.kD);
        double fValue = table.getEntry("kF").getDouble(drivetrain.kF);
        double izone = table.getEntry("izone").getDouble(drivetrain.izone);

        drivetrain.updatePIDValues(pValue, iValue, dValue, fValue, (int) izone);
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
        posLog.log(drivetrain.getCoordinates());

        velocityGraph.getEntry("Left Velocity").setDouble(drivetrain.getLeftTalonVelocity());
        velocityGraph.getEntry("Left Set V").setDouble(drivetrain.getLeftSetV());
        velocityGraph.getEntry("Right Velocity").setDouble(drivetrain.getRightTalonVelocity());
        velocityGraph.getEntry("Right Set V").setDouble(drivetrain.getRightSetV());

        Scheduler.getInstance().run();
    }

    public void teleopPeriodic() {
        if(gamepad0.middleLeft().get() && gamepad0.middleRight().get()) {
            ramp.deployRamps();
            System.out.println("Deploying Ramps");
        }

        posLog.log(drivetrain.getCoordinates());

        velocityGraph.getEntry("Left Velocity").setDouble(drivetrain.getLeftTalonVelocity());
        velocityGraph.getEntry("Left Set V").setDouble(drivetrain.getLeftSetV());
        velocityGraph.getEntry("Right Velocity").setDouble(drivetrain.getRightTalonVelocity());
        velocityGraph.getEntry("Right Set V").setDouble(drivetrain.getRightSetV());

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
