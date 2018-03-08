package frc.team1816.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1816.robot.Components;

public class Climber extends Subsystem {

    private TalonSRX climberMain, climberSlaveOne, climberSlaveTwo;
    private double climberSpeed;
    private Solenoid shifterSolenoid;

    public Climber(int climberMain, int climberSlaveOne, int climberSlaveTwo, int shifterSolenoidID, int pcmNode) {
        super();

        this.shifterSolenoid = new Solenoid(pcmNode, shifterSolenoidID);

        this.climberMain = new TalonSRX(climberMain);
        this.climberSlaveOne = new TalonSRX(climberSlaveOne);
        this.climberSlaveTwo = new TalonSRX(climberSlaveTwo);

        this.climberSlaveOne.set(ControlMode.Follower, climberMain);
        this.climberSlaveTwo.set(ControlMode.Follower, climberMain);

        this.climberMain.setNeutralMode(NeutralMode.Brake);
        this.climberSlaveOne.setNeutralMode(NeutralMode.Brake);
        this.climberSlaveTwo.setNeutralMode(NeutralMode.Brake);

        shifterSolenoid.set(false); //TODO (double and triple) check tubing so false = disengaged
    }

    public void setClimberSpeed(double speed) {
        if(speed > 0) {
            this.climberSpeed = 0;
        } else {
            this.climberSpeed = speed;
        }

        climberMain.set(ControlMode.PercentOutput, this.climberSpeed);
    }

    public void engageShifter() {
        shifterSolenoid.set(true);
    }

    public void disengageShifter() {
        shifterSolenoid.set(false);
    }

    public void initDefaultCommand() {
    }
}

