package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Collector extends Subsystem {
    private TalonSRX right;
    private TalonSRX left;

    private Solenoid frontSolenoid;

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
        left.set(ControlMode.PercentOutput, lpower);
        right.set(ControlMode.PercentOutput, rpower);
    }

    public void toggleCollector(boolean collectorToggle) {
        frontSolenoid.set(collectorToggle);
    }

    public void initDefaultCommand() {
        // TODO: Set the default command, if any, for a subsystem here. Example:
    }
}
