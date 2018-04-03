package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;


public class Collector extends Subsystem {
    private TalonSRX right;
    private TalonSRX left;
    private double outputCurrent;

//    private Relay clawLift;
    private TalonSRX clawLift;

    public Collector(int leftTalon, int rightTalon, int clawLift) {
        super();

        this.left = new TalonSRX(leftTalon);
        this.right = new TalonSRX(rightTalon);
        this.clawLift = new TalonSRX(clawLift);

        this.right.setInverted(true);

        this.left.setNeutralMode(NeutralMode.Brake);
        this.right.setNeutralMode(NeutralMode.Brake);
        this.clawLift.setNeutralMode(NeutralMode.Brake);

        this.left.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        this.right.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);

    }

    public void setCollectorSpeed(double lpower, double rpower) {
        left.set(ControlMode.PercentOutput, lpower);
        right.set(ControlMode.PercentOutput, rpower);
    }

    public void clawLiftUp() {
        clawLift.set(ControlMode.PercentOutput,-.3);
    }

    public void clawLiftDown() {
        clawLift.set(ControlMode.PercentOutput, .4);
    }

    public void clawLiftStop() {
        clawLift.set(ControlMode.PercentOutput, 0);
    }

    public void setClawSpeed(double clawSpeed) {
        clawLift.set(ControlMode.PercentOutput, clawSpeed);
    }

    public void initDefaultCommand() {
    }
    public double getOutputCurrent(){
        return outputCurrent;
    }
    public void periodic(){
       outputCurrent =  clawLift.getOutputCurrent();
    }

    public void initSendable(SendableBuilder builder){
        super.initSendable(builder);
        builder.addDoubleProperty("Output Current", this::getOutputCurrent, null);
    }

}
