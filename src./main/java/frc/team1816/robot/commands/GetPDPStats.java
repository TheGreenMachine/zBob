package frc.team1816.robot.commands;

import com.edinarobotics.utils.log.Logging;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.hal.PDPJNI;

public class GetPDPStats extends Command{

    private PowerDistributionPanel pdp;
    private double current;
//    private double volts;
//    private double power;
    private Logging log;

    public GetPDPStats(){
        super("getpdpstats");
        pdp = new PowerDistributionPanel();

    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 15; i++) {
            current = pdp.getCurrent(i);
            sb.append(System.currentTimeMillis());
            sb.append(",");
            sb.append("Port " + i);
            sb.append(",");
            sb.append(current);
        }
        log.log(sb.toString());
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() { end(); }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
