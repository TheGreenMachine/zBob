package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.edinarobotics.utils.subsystems.Subsystem1816;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem {

    private TalonSRX elevatorMaster, elevatorSlave;
    private DigitalInput upperLimit, lowerLimit;
    private double speed;
    private Encoder elevatorEncoder;

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

    public void setElevatorSpeed(double inspeed) {
        if (this.speed != inspeed) {
            this.speed = inspeed;

            if (getUpperLimit() && speed > 0) {
                System.out.println("set speed: stopped elevator up");
                speed = 0;
            } else if (getLowerLimit() && speed < 0) {
                System.out.println("set speed: stopped elevator down");
                speed = 0;
            }

            elevatorMaster.set(ControlMode.PercentOutput, speed);
        }
    }

    //    Limit Switches are true when open
    public boolean getUpperLimit() {
        return !upperLimit.get();
    }

    public boolean getLowerLimit() {
        return !lowerLimit.get();
    }

    public double getTicks() {
        return elevatorEncoder.get();
    }

    public double getHeightPercent() {
        return (( getTicks() / MAX_ENCODER_TICKS ) * 100);
    }

    public double getElevatorOutputVoltage() {
        return elevatorMaster.getMotorOutputPercent();
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
//        System.out.println("periodic | Height Percent: " + getHeightPercent() + "\tspeed: " + speed);

//        ENCODER BROKEN
//
//        if(getHeightPercent() < 10 ) {
//            //elevator ramp down
//            speed *= 0.5;
//            elevatorMaster.set(ControlMode.PercentOutput, speed);
//        }

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

    @Override
    protected void initDefaultCommand() {

    }
}