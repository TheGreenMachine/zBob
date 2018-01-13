package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;

public class DriveXInchesCommand extends Command {

    private Drivetrain drivetrain;
    double inches;
    double speed;
    double ticks;
    double tempInches;

    public DriveXInchesCommand(double inches, double speed) {
        super("drivexinchescommand");
        this.inches = inches;
        this.speed = speed;
        drivetrain = Components.getInstance().drivetrain;
        ticks = (int) (inches * drivetrain.TICKS_PER_INCH);
        //drivetrain.getRightMain().setSelectedSensorPosition(0,0,10);
        //drivetrain.getLeftMain().setSelectedSensorPosition(0,0,10);
    }

    @Override
    protected void initialize() {
        drivetrain.resetEncoders();
    }

    @Override
    protected void execute() {
        double velocity;
        double currentPosition = drivetrain.talonPositionRight() * -1;
        double currentInches = drivetrain.INCHES_PER_REV * currentPosition;

        tempInches = inches - currentInches;
        System.out.println("current inches: " + currentInches);

        velocity = speed;
        drivetrain.setDrivetrain(velocity, velocity);
    }

    @Override
    protected void end() {
        drivetrain.setDrivetrain(0,0);
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected boolean isFinished() {
        if (tempInches <= 0) {
            return true;
        } else {
            return false;
        }
    }
}
