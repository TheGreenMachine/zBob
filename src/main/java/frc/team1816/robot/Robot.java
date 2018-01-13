package frc.team1816.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.edinarobotics.utils.gamepad.Gamepad;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {

    private TalonSRX talonOne = new TalonSRX(1);
    private TalonSRX talonTwo = new TalonSRX(3);
    private TalonSRX talonThree = new TalonSRX(2);
    private TalonSRX talonFour = new TalonSRX(6);
    private TalonSRX talonFive = new TalonSRX(5);
    private TalonSRX talonSix = new TalonSRX(4);
    private Gamepad gamepad = new Gamepad(0);
    private StringBuilder _sb = new StringBuilder();
    private int _loops = 0;

    public void robotInit() {
        /* first choose the sensor */
        talonTwo.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, Constants.kTimeoutMs);
        talonTwo.setSensorPhase(true);

        talonFive.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, Constants.kTimeoutMs);
        talonFive.setSensorPhase(true);

        talonOne.setNeutralMode(NeutralMode.Coast);
        talonTwo.setNeutralMode(NeutralMode.Coast);
        talonThree.setNeutralMode(NeutralMode.Coast);
        talonFour.setNeutralMode(NeutralMode.Coast);
        talonFive.setNeutralMode(NeutralMode.Coast);
        talonSix.setNeutralMode(NeutralMode.Coast);

        /* set the peak and nominal outputs, 12V means full */
        talonTwo.configNominalOutputForward(0, Constants.kTimeoutMs);
        talonTwo.configNominalOutputReverse(0, Constants.kTimeoutMs);
        talonTwo.configPeakOutputForward(1, Constants.kTimeoutMs);
        talonTwo.configPeakOutputReverse(-1, Constants.kTimeoutMs);

        talonFive.configNominalOutputForward(0, Constants.kTimeoutMs);
        talonFive.configNominalOutputReverse(0, Constants.kTimeoutMs);
        talonFive.configPeakOutputForward(1, Constants.kTimeoutMs);
        talonFive.configPeakOutputReverse(-1, Constants.kTimeoutMs);

        /* set closed loop gains in slot0 */
        talonTwo.config_kF(Constants.kPIDLoopIdx, 0.34, Constants.kTimeoutMs);
        talonTwo.config_kP(Constants.kPIDLoopIdx, 0.2, Constants.kTimeoutMs);
        talonTwo.config_kI(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);
        talonTwo.config_kD(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);
        talonTwo.getSensorCollection().setQuadraturePosition(0, Constants.kTimeoutMs);

        talonFive.config_kF(Constants.kPIDLoopIdx, 0.34, Constants.kTimeoutMs);
        talonFive.config_kP(Constants.kPIDLoopIdx, 0.2, Constants.kTimeoutMs);
        talonFive.config_kI(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);
        talonFive.config_kD(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);
        talonFive.getSensorCollection().setQuadraturePosition(0, Constants.kTimeoutMs);

        talonFive.setInverted(true);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        /* get gamepad axis */
        double leftYstick = -gamepad.getLeftY();
        double rightYStick = -gamepad.getRightY();
        double motorOutput = talonTwo.getMotorOutputPercent();
        /* prepare line to print */
        _sb.append("\tout L:");
        _sb.append(motorOutput);
        _sb.append("\tticks L:");
        _sb.append(talonTwo.getSelectedSensorPosition(Constants.kPIDLoopIdx));
        _sb.append("\tout R:");
        _sb.append(talonFive.getMotorOutputPercent());
        _sb.append("\tticks R:");
        _sb.append(talonFive.getSelectedSensorPosition(Constants.kPIDLoopIdx));

        if (gamepad.diamondRight().get()) {
            /* Speed mode */
            /* 4096 Units/Rev * 500 RPM / 600 100ms/min in either direction: velocity setpoint is in units/100ms */
            double targetSpeed = leftYstick * 4096 * 500.0 / 600;
            talonTwo.set(ControlMode.Velocity, targetSpeed); /* 1500 RPM in either direction */

            /* append more signals to print when in speed mode. */
            _sb.append("\terr:");
            _sb.append(talonTwo.getClosedLoopError(Constants.kPIDLoopIdx));
            _sb.append("\ttrg:");
            _sb.append(targetSpeed);
        } else if (gamepad.diamondLeft().get()) {
            double targetSpeed = rightYStick * 4096 * 500.0 / 600;
            talonFive.set(ControlMode.Velocity, targetSpeed);

            _sb.append("\terr:");
            _sb.append(talonFive.getClosedLoopError(Constants.kPIDLoopIdx));
            _sb.append("\ttrg:");
            _sb.append(targetSpeed);
        }
        /* Percent voltage mode */
        talonTwo.set(ControlMode.PercentOutput, leftYstick);
        talonFive.set(ControlMode.PercentOutput, rightYStick);

        if (++_loops >= 10) {
            _loops = 0;
            System.out.println(_sb.toString());
        }
        _sb.setLength(0);
    }
}