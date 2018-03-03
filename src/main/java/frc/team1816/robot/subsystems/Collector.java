package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import com.sun.org.apache.regexp.internal.RE;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Collector extends Subsystem {
    private TalonSRX right;
    private TalonSRX left;

    private Relay clawLift;

    public Collector(int leftTalon, int rightTalon, int clawLiftDIO) {
        super();

        this.left = new TalonSRX(leftTalon);
        this.right = new TalonSRX(rightTalon);
        this.clawLift = new Relay(clawLiftDIO);

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

    public void initDefaultCommand() {
    }
}
