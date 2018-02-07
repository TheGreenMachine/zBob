package frc.team1816.robot;

import com.edinarobotics.utils.gamepad.Gamepad;
import com.edinarobotics.utils.log.Logging;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1816.robot.commands.*;
import frc.team1816.robot.subsystems.Collector;
import frc.team1816.robot.subsystems.Drivetrain;
import frc.team1816.robot.subsystems.Elevator;

public class Robot extends IterativeRobot {

    public static Logging logger;
    private Drivetrain drivetrain;
    private Elevator elevator;
    private Collector collector;

    private SendableChooser<Command> autoChooser;

    private LeftAutoStartCommand leftAuto;
    private RightAutoStartCommand rightAuto;

    private NetworkTable table;

    public void robotInit() {
        Components.getInstance();
        Controls.getInstance();
        table = NetworkTable.getTable("Shuffleboard_PID");

        drivetrain = Components.getInstance().drivetrain;
        elevator = Components.getInstance().elevator;
        collector = Components.getInstance().collector;

        leftAuto = new LeftAutoStartCommand();
        rightAuto = new RightAutoStartCommand();

        autoChooser = new SendableChooser<>();
        autoChooser.addObject("Left Start Auto", leftAuto);
        autoChooser.addObject("Right Start Auto", rightAuto);
//        autoChooser.addObject("Center Start Auto", new CenterAutoStartCommand());
        autoChooser.addDefault("Auto-Run", new DriveXInchesCommand(100, 0.8, false));

        SmartDashboard.putData("Autonomous", autoChooser);

         table.putNumber("P", drivetrain.p);
         table.putNumber("I", drivetrain.i);
         table.putNumber("D", drivetrain.d);
         table.putNumber("F", drivetrain.f);
         table.putNumber("izone", drivetrain.izone);
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void autonomousInit() {
        logger = new Logging("AutoLog");
        drivetrain.resetEncoders();

        leftAuto.selectAutoL();
        rightAuto.selectAutoR();

        Command autoCommand = autoChooser.getSelected();

//        Command autoCommand = new ArcDriveCommand(48, 0.4, 90);
//        Command autoCommand = new RotateXDegreesCommand(90);
//        Command autoCommand = new DriveXInchesCommand(48,0.5, false);

        System.out.println("Auto Running: " + autoCommand.getName());
        autoCommand.start();
    }

    @Override
    public void teleopInit() {
        logger = new Logging("TeleopLog1");

        Gamepad gamepad0 = Controls.getInstance().gamepad0;
        Gamepad gamepad1 = Controls.getInstance().gamepad1;

        drivetrain.resetEncoders();
        drivetrain.setDefaultCommand(new GamepadDriveCommand(gamepad0));
        elevator.setDefaultCommand(new GamepadElevatorCommand(gamepad1));

        // collector.setDefaultCommand(new GamepadCollectorCommand(gamepad1));

         double pValue = table.getDouble("P", drivetrain.p);
         double iValue = table.getDouble("I", drivetrain.i);
         double dValue = table.getDouble("D", drivetrain.d);
         double fValue = table.getDouble("F", drivetrain.f);
         double izone = table.getDouble("izone", drivetrain.izone);

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
        Scheduler.getInstance().run();
    }

    public void teleopPeriodic() {
        System.out.println("L Velocity (ticks/100ms): " + drivetrain.getLeftTalonVelocity());
        System.out.println("R Velocity (ticks/100ms): " + drivetrain.getRightTalonVelocity());
        System.out.println("Left Ticks (grayhill): " + drivetrain.talonPositionLeft());
        System.out.println("Right Ticks (grayhill): " + drivetrain.talonPositionRight());
        System.out.println("Gyro: " + drivetrain.getGyroAngle());
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
