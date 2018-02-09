package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.edinarobotics.utils.subsystems.Subsystem1816;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem {

    private TalonSRX elevatorMaster;
    private TalonSRX climberOne, climberTwo, climberThree, climberFour;
    private DigitalInput upperLimit, lowerLimit;
    private double speed;
    private double climberSpeed;
    private Encoder elevatorEncoder;
    private Solenoid shifterSolenoid;

    private boolean climberEngaged = false;

    private static int MAX_ENCODER_TICKS = 1000;

    public Elevator(int elevatorMaster, int encoderPort1, int encoderPort2, int upperLimit, int lowerLimit, int shifterSolenoidID
            , int climberOne, int climberTwo, int climberThree, int climberFour) {
        super();
        this.elevatorMaster = new TalonSRX(elevatorMaster);
        this.elevatorEncoder = new Encoder(encoderPort1, encoderPort2, false, Encoder.EncodingType.k4X);
        this.upperLimit = new DigitalInput(upperLimit);
        this.lowerLimit = new DigitalInput(lowerLimit);
        this.shifterSolenoid = new Solenoid(shifterSolenoidID);

        this.climberOne = new TalonSRX(climberOne);
        this.climberTwo = new TalonSRX(climberTwo);
        this.climberThree = new TalonSRX(climberThree);
        this.climberFour = new TalonSRX(climberFour);

        this.climberTwo.set(ControlMode.Follower, climberOne);
        this.climberThree.set(ControlMode.Follower, climberOne);
        this.climberFour.set(ControlMode.Follower, climberOne);

        this.elevatorMaster.set(ControlMode.PercentOutput, 0.0);
        this.elevatorMaster.setNeutralMode(NeutralMode.Brake);
    }

    public void setElevatorSpeed(double speed) {
        if (this.speed != speed) {
            this.speed = speed;

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

    public void setClimberSpeed(double speed) {
        if(speed > 0) {
            this.climberSpeed = 0;
        } else {
            this.climberSpeed = speed;
        }

        climberOne.set(ControlMode.PercentOutput, this.speed);
    }

    public void engageShifter() {
        if(climberEngaged) {
            shifterSolenoid.set(true);
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
        return (elevatorEncoder.get() / MAX_ENCODER_TICKS * 100);
    }

    public void resetEncoders() {
        this.elevatorEncoder.reset();
        elevatorEncoder.reset();
    }

    @Override
    public void periodic() {
        if (getUpperLimit() && speed > 0) {
            System.out.println("periodic: stopped elevator up");
            speed = 0;
            elevatorMaster.set(ControlMode.PercentOutput, speed);
        } else if (getLowerLimit()) {
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