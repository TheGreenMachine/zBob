package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Collector extends Subsystem {
    private TalonSRX right;
    private TalonSRX left;

    private Relay clawLift;

    public Collector(int leftTalon, int rightTalon, int clawLiftPort) {
        super();

        this.left = new TalonSRX(leftTalon);
        this.right = new TalonSRX(rightTalon);
        this.clawLift = new Relay(clawLiftPort, Relay.Direction.kBoth);

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

    public void clawLiftUp() {
        clawLift.set(Relay.Value.kForward);
    }

    public void clawLiftDown() {
        clawLift.set(Relay.Value.kReverse);
    }

    public void clawLiftStop() {
        clawLift.set(Relay.Value.kOff);
    }

    public void clawLiftOn() {
        clawLift.set(Relay.Value.kOn);
    }

    public void initDefaultCommand() {
    }
}
