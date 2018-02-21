package frc.team1816.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Ramp extends Subsystem {
    private Solenoid rampSolenoid;
    private boolean deployState = true; //TODO test this value

    public Ramp(int pcmNodeRamp, int rampID){
        super();
        rampSolenoid = new Solenoid(pcmNodeRamp, rampID);
        rampSolenoid.set(false); //TODO test this value
    }

    public void deployRamps(){
        rampSolenoid.set(deployState);
    }

    public void resetRamps() {
        rampSolenoid.set(!deployState);
    }

    public void initDefaultCommand() {
        // TODO: Set the default command, if any, for a subsystem here. Example:
    }
}
