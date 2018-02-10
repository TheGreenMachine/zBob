package frc.team1816.robot.commands;

import com.edinarobotics.utils.gamepad.Gamepad;
import com.edinarobotics.utils.log.Logging;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.Robot;
import frc.team1816.robot.subsystems.Drivetrain;

public class GetPDPStats extends Command{

    private Drivetrain drivetrain;
    private Gamepad gamepad;
    private PowerDistributionPanel pdp;
    private double current;
    private double volts;
    private double power;
//    private Logging log;

    public GetPDPStats(Gamepad gamepad){
        super("getpdpstats");
        pdp = new PowerDistributionPanel();
//        log = new Logging("PDP");
        this.drivetrain = Components.getInstance().drivetrain;
        this.gamepad = gamepad;
        requires(drivetrain);
    }

    @Override
    protected void initialize() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 15; i++) {
            sb.append("Port " + i);
            sb.append(",");
        }
        Robot.logger.log(sb.toString());
    }

    @Override
    protected void execute() {
        StringBuilder sb = new StringBuilder();
        double right = gamepad.getLeftY();
        double left = gamepad.getLeftY();
        double rotation = gamepad.getRightX();
        for (int i = 0; i <= 15; i++) {
            current = pdp.getCurrent(i);
            sb.append(current);
            sb.append(",");
        }
//        power = pdp.getTotalPower();
//        volts = pdp.getVoltage();
//        sb.append(",");
//        sb.append("power");
//        sb.append(",");
//        sb.append(power);
//        sb.append(",");
//        sb.append("voltage");
//        sb.append(",");
//        sb.append(volts);
        Robot.logger.log(sb.toString());

//        System.out.println(pdp.getCurrent(0) + " " + pdp.getCurrent(1) + " " + pdp.getCurrent(2) + " " +
//                            pdp.getCurrent(13) + " " + pdp.getCurrent(14) + " " + pdp.getCurrent(15));
//        System.out.println(sb.toString());
        drivetrain.setDrivetrain(left, right, rotation);

    }

    @Override
    protected void end() {
//        Robot.logger.close();
    }

    @Override
    protected void interrupted() { end(); }

    @Override
    protected boolean isFinished() {
        return false;
    }
}