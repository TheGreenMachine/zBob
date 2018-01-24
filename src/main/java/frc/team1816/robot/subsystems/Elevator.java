package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.edinarobotics.utils.subsystems.Subsystem1816;
import edu.wpi.first.wpilibj.DigitalInput;

public class Elevator extends Subsystem1816 {

    private TalonSRX elevatorMaster;
    private DigitalInput upperLimit, lowerLimit;
    private double speed;

    public Elevator (int elevatorMaster, int upperLimit, int lowerLimit){
        super();
        this.elevatorMaster = new TalonSRX(elevatorMaster);
        this.upperLimit = new DigitalInput(upperLimit);
        this.lowerLimit = new DigitalInput(lowerLimit);

        this.elevatorMaster.set(ControlMode.PercentOutput, elevatorMaster); //TODO syntax from Drivetrain, makes little sense, look into this
        this.elevatorMaster.setNeutralMode(NeutralMode.Brake);
        this.elevatorMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10); //encoder type TBD
    }

    public void setElevatorSpeed(double speed){
        this.speed = speed;
    }

    @Override
    public void update() {
        elevatorMaster.set(ControlMode.PercentOutput, speed);
    }
}
