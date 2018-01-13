package frc.team1816.robot;

import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import com.ctre.phoenix.motorcontrol.*;

public class Robot extends IterativeRobot {

    private TalonSRX _talon = new TalonSRX(3);
    private Joystick _joy = new Joystick(0);
    private StringBuilder _sb = new StringBuilder();
    private int _loops = 0;

    public void robotInit() {
        /* first choose the sensor */
        _talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, Constants.kTimeoutMs);
        _talon.setSensorPhase(true);

        /* set the peak and nominal outputs, 12V means full */
        _talon.configNominalOutputForward(0, Constants.kTimeoutMs);
        _talon.configNominalOutputReverse(0, Constants.kTimeoutMs);
        _talon.configPeakOutputForward(1, Constants.kTimeoutMs);
        _talon.configPeakOutputReverse(-1, Constants.kTimeoutMs);

        /* set closed loop gains in slot0 */
        _talon.config_kF(Constants.kPIDLoopIdx, 0.34, Constants.kTimeoutMs);
        _talon.config_kP(Constants.kPIDLoopIdx, 0.2, Constants.kTimeoutMs);
        _talon.config_kI(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);
        _talon.config_kD(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);
        _talon.getSensorCollection().setQuadraturePosition(0, Constants.kTimeoutMs);
    }
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        /* get gamepad axis */
        double leftYstick = _joy.getY();
        double motorOutput = _talon.getMotorOutputPercent();
        /* prepare line to print */
        _sb.append("\tout:");
        _sb.append(motorOutput);
        _sb.append("\tticks:");
        _sb.append(_talon.getSelectedSensorPosition(Constants.kPIDLoopIdx));

        if(_joy.getRawButton(1)){
            /* Speed mode */
            /* 4096 Units/Rev * 500 RPM / 600 100ms/min in either direction: velocity setpoint is in units/100ms */
            double targetSpeed = leftYstick * 4096 * 500.0 / 600;
            _talon.set(ControlMode.Velocity, targetSpeed); /* 1500 RPM in either direction */

            /* append more signals to print when in speed mode. */
            _sb.append("\terr:");
            _sb.append(_talon.getClosedLoopError(Constants.kPIDLoopIdx));
            _sb.append("\ttrg:");
            _sb.append(targetSpeed);
        } else {
            /* Percent voltage mode */
            _talon.set(ControlMode.PercentOutput, leftYstick);
        }

        if(++_loops >= 10) {
            _loops = 0;
            System.out.println(_sb.toString());
        }
        _sb.setLength(0);
    }
}