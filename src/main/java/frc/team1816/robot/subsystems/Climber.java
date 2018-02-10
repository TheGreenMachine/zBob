package frc.team1816.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Climber extends Subsystem {

    private TalonSRX climberMain, climberSlaveOne, climberSlaveTwo, climberSlaveThree;
    private double climberSpeed;
    private Solenoid shifterSolenoid;

    public Climber(int climberMain, int climberSlaveOne, int climberSlaveTwo, int climberSlaveThree, int shifterSolenoidID, int pcmNode) {
        super();

        this.shifterSolenoid = new Solenoid(pcmNode, shifterSolenoidID);

        this.climberMain = new TalonSRX(climberMain);
        this.climberSlaveOne = new TalonSRX(climberSlaveOne);
        this.climberSlaveTwo = new TalonSRX(climberSlaveTwo);
        this.climberSlaveThree = new TalonSRX(climberSlaveThree);

        this.climberSlaveOne.set(ControlMode.Follower, climberMain);
        this.climberSlaveTwo.set(ControlMode.Follower, climberMain);
        this.climberSlaveThree.set(ControlMode.Follower, climberMain);

        this.climberMain.setNeutralMode(NeutralMode.Brake);
        this.climberSlaveOne.setNeutralMode(NeutralMode.Brake);
        this.climberSlaveTwo.setNeutralMode(NeutralMode.Brake);
        this.climberSlaveThree.setNeutralMode(NeutralMode.Brake);
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
        // TODO: Set the default command, if any, for a subsystem here. Example:
    }
}

