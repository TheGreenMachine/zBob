package frc.team1816.robot.commands;

import com.edinarobotics.utils.log.Logging;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;

public class IRTestingCommand extends Command{

    private AnalogInput IR;
    private Logging log;
    private Drivetrain drivetrain;
    double inches;
    double speed;
    double ticks;
    double remainingInches;
    private final double TOLERANCE = 0.1;
    private double stopVoltage = 1.4;
    boolean isStopped = false;

    public IRTestingCommand(double inches, double speed){
        super("irtestcommand");
        IR = Components.getInstance().ai;
        this.inches = inches;
        this.speed = speed;
        drivetrain = Components.getInstance().drivetrain;
        ticks = (int) (inches * Drivetrain.TICKS_PER_INCH);
    }

    @Override
    protected void initialize() {
        System.out.println("Init");
        drivetrain.resetEncoders();
        log = new Logging("IRTest");
    }

    @Override
    protected void execute() {
        int rawData = IR.getAverageValue();
        double volts = IR.getAverageVoltage();
        System.out.println("raw " + rawData + ", volts " + volts);
        //log.log("raw " + rawData + ", volts " + volts);
        double currentPosition = drivetrain.talonPositionRight();
        double currentInches = currentPosition / Drivetrain.TICKS_PER_INCH;

        remainingInches = inches - currentInches;
        System.out.println("Remaining inches: " + remainingInches);
        System.out.println("Current inches: " + currentInches);
        System.out.println("Current Position: " + currentPosition);
        System.out.println("---");

//        drivetrain.setDrivetrain(speed, speed);

//        consider changing deltaAngle deadzone, need more testing
//        if (Math.abs(stopVoltage-volts) <= TOLERANCE){
//            System.out.println("Target Reached; Stopping");
//            drivetrain.setDrivetrain(0,0);
//            isStopped = true;
//        }
    }

    @Override
    protected boolean isFinished() {
//        if (remainingInches <= 0 || isStopped) {
//            System.out.println("Finished");
//            return true;
//        } else {
//            return false;
//        }
        return false;
    }

    @Override
    protected void end() {
        //drivetrain.setDrivetrain(0, 0);
        //drivetrain.resetEncoders();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
