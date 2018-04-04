package frc.team1816.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Ramp extends Subsystem {
    private Solenoid rampSolenoid;
    private boolean deployState = true;

    public Ramp(int pcmNodeRamp, int rampID){
        super();
        rampSolenoid = new Solenoid(pcmNodeRamp, rampID);
        rampSolenoid.set(false);
    }

    public void deployRamps(){
        rampSolenoid.set(deployState);
    }

    public void resetRamps() {
        rampSolenoid.set(!deployState);
    }

    public void initDefaultCommand() {
    }
}
