package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.edinarobotics.utils.subsystems.Subsystem1816;

public class Collector extends Subsystem1816 {
    private TalonSRX right;
    private TalonSRX left;
    private double p = 0.2;
    private double i = 0;
    private double d = 0;
    private double f = 0.34;
    private int izone = 100;
    private double power;

    public Collector(int right, int left) {
        super();

        this.right = new TalonSRX(right);
        this.left = new TalonSRX(left);

        this.right.setInverted(true);

        this.right.setNeutralMode(NeutralMode.Brake);
        this.left.setNeutralMode(NeutralMode.Brake);

        this.right.set(ControlMode.Velocity, right);
        this.left.set(ControlMode.Velocity, left);

        this.right.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        this.left.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);

        this.right.configNominalOutputForward(0, 10);
        this.right.configNominalOutputReverse(0, 10);
        this.right.configPeakOutputForward(1, 10);
        this.right.configPeakOutputReverse(-1, 10);

        this.left.configNominalOutputForward(0, 10);
        this.left.configNominalOutputReverse(0, 10);
        this.left.configPeakOutputForward(1, 10);
        this.left.configPeakOutputReverse(-1, 10);

        this.right.config_kP(0, p, 20);
        this.right.config_kI(0, i, 20);
        this.right.config_kD(0, d, 20);
        this.right.config_kF(0, f, 20);
        this.right.config_IntegralZone(0, izone, 20);
        this.left.config_kP(0, p, 20);
        this.left.config_kI(0, i, 20);
        this.left.config_kD(0, d, 20);
        this.left.config_kF(0, f, 20);
        this.left.config_IntegralZone(0, izone, 20);
    }

    public void setCollectorSpeed(double power) {
        this.power = power;
        update();
    }

    @Override
    public void update() {
        right.set(ControlMode.PercentOutput, power);
        left.set(ControlMode.PercentOutput, power);
    }
}
