package frc.team1816.robot;

import com.edinarobotics.utils.gamepad.Gamepad;
import com.edinarobotics.utils.log.Logging;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1816.robot.commands.*;
import frc.team1816.robot.subsystems.Drivetrain;

public class Robot extends IterativeRobot {

    public static Logging logger;
    private Drivetrain drivetrain;
    private double time;
    private SendableChooser<Command> autoChooser;
    private int _loops = 0;

    public void robotInit() {
        Components.getInstance();
        Controls.getInstance();

        drivetrain = Components.getInstance().drivetrain;

        autoChooser = new SendableChooser<>();
        autoChooser.addObject("Left Start Auto", new LeftAutoStartCommand());
        autoChooser.addObject("Right Start Auto", new RightAutoStartCommand());
        autoChooser.addObject("Center Start Auto", new CenterAutoStartCommand());
        autoChooser.addDefault("Auto-Run", new DriveXInchesCommand(100, 0.8, false));

        SmartDashboard.putData("Autonomous", autoChooser);
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void autonomousInit() {
        logger = new Logging("AutoLog");
        drivetrain.resetEncoders();
//        Command autoCommand = autoChooser.getSelected();
        Command autoCommand = new ArcDriveCommand(24, 0.4, 90);
        System.out.println("Auto Running: " + autoCommand.getName());
//        Command autoCommand = new RotateXDegreesCommand(90);
//        Command autoCommand = new DriveXInchesCommand(120,0.2, true);
        autoCommand.start();
    }

    @Override
    public void teleopInit() {
        logger = new Logging("TeleopLog");
        Gamepad gamepad0 = Controls.getInstance().gamepad0;
        Gamepad gamepad1 = Controls.getInstance().gamepad1;

        drivetrain.resetEncoders();
        drivetrain.setDefaultCommand(new GamepadDriveCommand(gamepad0));
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