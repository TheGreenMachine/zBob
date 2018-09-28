package frc.team1816.robot;

import com.edinarobotics.utils.gamepad.Gamepad;
import com.edinarobotics.utils.log.Logging;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
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
    public static Logging PPLog;

    private Drivetrain drivetrain;
    private Elevator elevator;
    private Collector collector;
    private Climber climber;
    private Ramp ramp;

    private Gamepad gamepad0, gamepad1;

    private SendableChooser<Command> autoChooser;
    private SendableChooser<String> startPosition;

    private SwitchAutoCommand switchAuto;
    private NearSwitchAutoCommand nearSwitchAuto;
    private ScaleAutoCommand scaleAuto;
    private PriorityAutoCommand priorityAuto;
    private NearSideAutoCommand nearAuto;
    private AvoidanceScaleAutoCommand avoidanceScaleAuto;
    private CenterAutoStartSwitchCommand centerAuto;
    private AvoidanceScaleAutoNearCommand avoidanceNearOnly;
    private ModdedScaleAutoCommand modScaleAuto;
    private StateElimsAutoCommand stateElimsAuto;
    private PurePursuitAutoTestCommand PPTestCommand;

    private NetworkTable table;
    private NetworkTable velocityGraph;
    private NetworkTable avoidanceParameter;

    private boolean rampsDeployed = false;

    public void robotInit() {
        Components.getInstance();
        Controls.getInstance();
        table = NetworkTableInstance.getDefault().getTable("Shuffleboard_PID");
        velocityGraph = NetworkTableInstance.getDefault().getTable("Velocity Graph");
        avoidanceParameter = NetworkTableInstance.getDefault().getTable("Avoidance Auto Parameters");
        CameraServer.getInstance().startAutomaticCapture();

        drivetrain = Components.getInstance().drivetrain;
        elevator = Components.getInstance().elevator;
        collector = Components.getInstance().collector;
        climber = Components.getInstance().climber;
        ramp = Components.getInstance().ramp;

        gamepad0 = Controls.getInstance().gamepad0;
        gamepad1 = Controls.getInstance().gamepad1;

        switchAuto = new SwitchAutoCommand();
        nearSwitchAuto = new NearSwitchAutoCommand();
        scaleAuto = new ScaleAutoCommand();
        priorityAuto = new PriorityAutoCommand();
        nearAuto = new NearSideAutoCommand();
        avoidanceScaleAuto = new AvoidanceScaleAutoCommand();
        centerAuto = new CenterAutoStartSwitchCommand();
        avoidanceNearOnly = new AvoidanceScaleAutoNearCommand();
        modScaleAuto = new ModdedScaleAutoCommand();
        stateElimsAuto = new StateElimsAutoCommand();
        PPTestCommand = new PurePursuitAutoTestCommand();

        startPosition = new SendableChooser<>();
        startPosition.addObject("Left Start", "Left Start");
        startPosition.addObject("Right Start", "Right Start");
        SmartDashboard.putData("Start Position", startPosition);

        autoChooser = new SendableChooser<>();

        autoChooser.addObject("PP Test Auto", PPTestCommand);

        autoChooser.addObject("State Eliminations Auto", stateElimsAuto);

        autoChooser.addObject("Modded Scale(2-cube compatible)", modScaleAuto);

        autoChooser.addObject("Switch Auto", switchAuto);
        autoChooser.addObject("Near Only Switch Auto", nearSwitchAuto);
        autoChooser.addObject("Center Switch Auto", centerAuto);

        autoChooser.addObject("Scale Auto", scaleAuto);
        autoChooser.addObject("Priority NearSw-NearSc-FarSw Auto", priorityAuto);
        autoChooser.addObject("Near Side Only Auto", nearAuto);
        autoChooser.addObject("Avoidance Scale Auto", avoidanceScaleAuto);

        autoChooser.addObject("Avoidance Scale Near Only Auto", avoidanceNearOnly);

        autoChooser.addDefault("Auto-Run", new DriveXInchesCommand(100, 0.8));
        autoChooser.addObject("Wait (debugging only)", new WaitCommand(1));
        autoChooser.addObject("ArcDrive Enc", new ArcDriveCommand(50,.3,90));
        autoChooser.addObject("ArcDrive Gyro", new ArcDriveGyroCommand(50,.3,90));
        autoChooser.addObject("Claw Down Test", new LowerCollectorClawCommand(false,1));

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

        avoidanceParameter.getEntry("Wait Time Near (s)").setDouble(0);
        avoidanceParameter.getEntry("Wait Time Far (s)").setDouble(0);
        avoidanceParameter.getEntry("Far Side - Distance From Wall (in)").setDouble(12);
        avoidanceParameter.getEntry("Long-Run Velocity").setDouble(1);

        collector.resetClawEnc(); //todo consider removing as redundancy
        SmartDashboard.putData("Manually Reset Collector Up/Down Encoder", new ResetClawEncoderCommand());
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
        posLog = Logging.getInstance("AutoPosLog");
        posLog.log("x,y,leftInches,rightInches,gyro");
        PPLog = Logging.getInstance("PPLog");

        collector.resetClawEnc();
        drivetrain.setDrivetrainBrakeMode();
        drivetrain.initCoordinateTracking();

        StringBuilder builder = new StringBuilder();
        builder.append("Current Time").append(",").append("Left Inches").append(",").append("Right Inches").append(",")
                .append("Left Velocity").append(",").append("Right Velocity").append(",").append("Set Power L").append(",")
                .append("Set Power R").append(",").append("Set Power Gyro").append(",").append("Gyro Heading");
        logger.log(builder.toString());

        drivetrain.resetEncoders();

        double secondsToWaitNear = avoidanceParameter.getEntry("Wait Time Near (s)").getDouble(0);
        double secondsToWaitFar = avoidanceParameter.getEntry("Wait Time Far (s)").getDouble(0);
        double distanceFromWall = avoidanceParameter.getEntry("Distance From Wall (in)").getDouble(12);
        double runVelocity = avoidanceParameter.getEntry("Long-Run Velocity").getDouble(1);

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

        String startPos = startPosition.getSelected();

        try {
            switchAuto.selectAuto(FMSmessage, startPos);
            nearSwitchAuto.selectAuto(FMSmessage, startPos);
            centerAuto.selectAuto(FMSmessage);
            scaleAuto.selectAuto(FMSmessage, startPos);
            priorityAuto.selectAuto(FMSmessage, startPos);
            nearAuto.selectAuto(FMSmessage, startPos);
            avoidanceScaleAuto.selectAuto(FMSmessage, startPos, secondsToWaitNear, secondsToWaitFar, distanceFromWall, runVelocity);
            avoidanceNearOnly.selectAuto(FMSmessage, startPos, secondsToWaitNear, secondsToWaitFar, distanceFromWall, runVelocity);
            modScaleAuto.selectAuto(FMSmessage, startPos, secondsToWaitNear, secondsToWaitFar, distanceFromWall, runVelocity);
            stateElimsAuto.selectAuto(FMSmessage, startPos, secondsToWaitNear, secondsToWaitFar, distanceFromWall, runVelocity);
        } catch (Exception e) {
            System.out.println("-----AUTO ALREADY CREATED, RUNNING PREVIOUS-----");
        }

        Command autoCommand = autoChooser.getSelected();

        System.out.println("Auto Running ---- " + startPos + "Start " + autoCommand.getName());
        autoCommand.start();
    }

    @Override
    public void teleopInit() {
        logger = Logging.getInstance("TeleopLog");
        posLog = Logging.getInstance("TeleopPosLog");
        posLog.log("x,y,leftInches,rightInches,gyro");

        drivetrain.setDrivetrainCoastMode();
        drivetrain.resetEncoders();
        drivetrain.initCoordinateTracking();

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

//        velocityGraph.getEntry("Left Velocity").setDouble(drivetrain.getLeftTalonVelocity());
//        velocityGraph.getEntry("Left Set V").setDouble(drivetrain.getLeftSetV());
//        velocityGraph.getEntry("Right Velocity").setDouble(drivetrain.getRightTalonVelocity());
//        velocityGraph.getEntry("Right Set V").setDouble(drivetrain.getRightSetV());

        Scheduler.getInstance().run();
    }

    public void teleopPeriodic() {
        if(gamepad0.middleLeft().get() && gamepad0.middleRight().get()) {
            ramp.deployRamps();
            rampsDeployed = true;
            System.out.println("Deploying Ramps");
        } else if(rampsDeployed) {
            ramp.resetRamps();
            rampsDeployed = false;
            System.out.println("Solenoid Reset");
        }

        posLog.log(drivetrain.getCoordinates());

        velocityGraph.getEntry("Left Velocity").setDouble(drivetrain.getLeftTalonVelocity());
        velocityGraph.getEntry("Left Set V").setDouble(drivetrain.getLeftSetV());
        velocityGraph.getEntry("Right Velocity").setDouble(drivetrain.getRightTalonVelocity());
        velocityGraph.getEntry("Right Set V").setDouble(drivetrain.getRightSetV());

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
