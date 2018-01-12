package frc.team1816.robot;

import com.edinarobotics.utils.gamepad.Gamepad;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.team1816.robot.subsystems.Drivetrain;

public class Robot extends IterativeRobot {
    private Drivetrain drivetrain;
    private double time;
    private Gamepad gamepad;
    private SendableChooser autoChooser;

    @Override
    public void robotInit() {
        Components.getInstance();
        Controls.getInstance();

        drivetrain = Components.getInstance().drivetrain;

        //setupDashboard() equivalent: exists?
    }

    @Override
    public void disabledInit() {

    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void teleopInit() {
        Gamepad gamepad0 = Controls.getInstance().gamepad0;
        Gamepad gamepad1 = Controls.getInstance().gamepad1;

        //drivetrain.setDefaultCommand();
    }

    @Override
    public void testInit() {
        LiveWindow.setEnabled(false);
        teleopInit();
    }


    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }
    
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopPeriodic() {
        teleopPeriodic();
    }

    @Override
    public void testPeriodic() {
        teleopPeriodic();
    }
}