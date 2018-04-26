package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.edinarobotics.utils.subsystems.Subsystem1816;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class Elevator extends Subsystem {

    private TalonSRX elevatorMaster, elevatorSlave;
    private DigitalInput upperLimit, lowerLimit;
    private double speed;
    private Encoder elevatorEncoder;

    private double elevatorEncPos, elevatorOutputV, elevatorHeightPercent;
    private boolean upperLim, lowerLim;

    private static int MAX_ENCODER_TICKS = 3100;

    public Elevator(int elevatorMaster, int elevatorSlave, int encoderPort1, int encoderPort2, int upperLimit, int lowerLimit) {
        super();

        this.elevatorMaster = new TalonSRX(elevatorMaster);
        this.elevatorSlave = new TalonSRX(elevatorSlave);
        this.elevatorEncoder = new Encoder(encoderPort1, encoderPort2, false, Encoder.EncodingType.k4X);
        this.upperLimit = new DigitalInput(upperLimit);
        this.lowerLimit = new DigitalInput(lowerLimit);

        this.elevatorSlave.set(ControlMode.Follower, elevatorMaster);
        this.elevatorMaster.set(ControlMode.PercentOutput, 0.0);
        
        this.elevatorMaster.setNeutralMode(NeutralMode.Brake);
        this.elevatorSlave.setNeutralMode(NeutralMode.Brake);

        elevatorEncoder.setReverseDirection(true);
    }

    public void setElevatorSpeed(double inputSpeed) {
        if (this.speed != inputSpeed) {
            this.speed = inputSpeed;

            if (getUpperLimit() && this.speed > 0) {
                System.out.println("set speed: stopped elevator up");
                this.speed = 0;
            } else if (getLowerLimit() && this.speed < 0) {
                System.out.println("set speed: stopped elevator down");
                this.speed = 0;
            }

            elevatorMaster.set(ControlMode.PercentOutput, this.speed);
        }
    }

    //    Limit Switches are true when open
    public boolean getUpperLimit() {
        return upperLim;
    }

    public boolean getLowerLimit() {
        return lowerLim;
    }

    public double getTicks() {
        return elevatorEncPos;
    }

    public double getHeightPercent() {
        return elevatorHeightPercent;
    }

    public double getElevatorOutputVoltage() {
        return elevatorOutputV;
    }

    public double getSetSpeed() {
        return speed;
    }

    public void resetEncoders() {
//        System.out.println("Resetting Encoder");
        elevatorEncoder.reset();
    }

    public void setCoastMode() {
        elevatorMaster.setNeutralMode(NeutralMode.Coast);
        elevatorSlave.setNeutralMode(NeutralMode.Coast);
    }

    public void setBrakeMode() {
        elevatorMaster.setNeutralMode(NeutralMode.Brake);
        elevatorSlave.setNeutralMode(NeutralMode.Brake);
    }

    public void periodic() {
        elevatorEncPos = elevatorEncoder.get();
        elevatorHeightPercent = ( getTicks() / MAX_ENCODER_TICKS ) * 100;
        elevatorOutputV = elevatorMaster.getMotorOutputPercent();
        upperLim = !upperLimit.get();
        lowerLim = !lowerLimit.get();

        if (getUpperLimit() && speed > 0) {
            System.out.println("periodic: stopped elevator up");
            speed = 0;
            elevatorMaster.set(ControlMode.PercentOutput, speed);
        } else if (getLowerLimit()) {
//            System.out.println("Resetting elevator enc");
            resetEncoders();

            if (speed < 0) {
                System.out.println("periodic: stopped elevator down");
                speed = 0;
                elevatorMaster.set(ControlMode.PercentOutput, speed);
            }
        }
    }

    public void initSendable(SendableBuilder builder){
        super.initSendable(builder);
        builder.addDoubleProperty("Output Voltage", this::getElevatorOutputVoltage, null);
        builder.addDoubleProperty("Elevator Ticks", this::getTicks, null);
        builder.addDoubleProperty("Height %", this::getHeightPercent, null);
        builder.addDoubleProperty("Set Power %", this ::getSetSpeed, null);
        builder.addBooleanProperty("Upper Lim", this::getUpperLimit,null);
        builder.addBooleanProperty("Lower Lim", this::getLowerLimit,null);
    }

    protected void initDefaultCommand() {

    }
}
