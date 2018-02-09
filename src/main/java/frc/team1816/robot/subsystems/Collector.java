package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.edinarobotics.utils.subsystems.Subsystem1816;

import edu.wpi.first.wpilibj.Solenoid;

public class Collector extends Subsystem1816 {
    private TalonSRX right;
    private TalonSRX left;

    private Solenoid frontSolenoid;

    private double leftPower;
    private double rightPower;
    private boolean collectorOpen;

    public Collector(int leftTalon, int rightTalon, int frontSolenoidID, int pcmNode) {
        super();

        this.left = new TalonSRX(leftTalon);
        this.right = new TalonSRX(rightTalon);

        this.frontSolenoid = new Solenoid(pcmNode, frontSolenoidID);

        this.right.setInverted(true);

        this.left.setNeutralMode(NeutralMode.Brake);
        this.right.setNeutralMode(NeutralMode.Brake);

        this.left.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        this.right.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
    }

    public void setCollectorSpeed(double lpower, double rpower) {
        this.leftPower = lpower;
        this.rightPower = rpower;
        update();
    }

    public void toggleCollector(boolean collectorToggle) {
        collectorOpen = collectorToggle;
    }

    @Override
    public void update() {
        left.set(ControlMode.PercentOutput, leftPower);
        right.set(ControlMode.PercentOutput, rightPower);

        frontSolenoid.set(collectorOpen);
    }
}
