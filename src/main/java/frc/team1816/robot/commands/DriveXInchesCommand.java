package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;

public class DriveXInchesCommand extends Command {

    private Drivetrain drivetrain;
    double inches;
    double speed;
    double ticks;
    double remainingInches;
    double initAngle;

    public DriveXInchesCommand(double inches, double speed) {
        super("drivexinchescommand");
        this.inches = inches;
        this.speed = speed;
        drivetrain = Components.getInstance().drivetrain;
        ticks = (int) (inches * Drivetrain.TICKS_PER_INCH);
        //drivetrain.getRightMain().setSelectedSensorPosition(0,0,10);
        //drivetrain.getLeftMain().setSelectedSensorPosition(0,0,10);
    }

    @Override
    protected void initialize() {
        System.out.println("Init");
        drivetrain.resetEncoders();
        initAngle = drivetrain.getGyroAngle();
    }

    @Override
    protected void execute() {
        double deltaAngle = drivetrain.getGyroAngle() - initAngle;
        double velocity;
        double currentPosition = drivetrain.talonPositionRight();
        double currentInches = currentPosition / Drivetrain.TICKS_PER_INCH;
        
        remainingInches = inches - currentInches;
        System.out.println("Remaining inches: " + remainingInches);
        System.out.println("Current inches: " + currentInches);
        System.out.println("Current Position: " + currentPosition);
        System.out.println("---");

        velocity = speed;




        //consider changing deltaAngle deadzone, need more testing
        if (remainingInches < 6) {
            if((velocity * (remainingInches / 6)) > .15) {
                System.out.println("1");
                velocity = velocity * (remainingInches / 6);
            } else {
                System.out.println("2");
                velocity = .15;
            }
            System.out.println("Ramped Down Velocity" + velocity);
            drivetrain.setDrivetrain(velocity, velocity);
            } else if (deltaAngle > 1) {
                System.out.println("Delta Angle: " + deltaAngle + " deltaAngle>1");
                drivetrain.setDrivetrain(velocity * 0.9, velocity);
                System.out.println("L Velocity: " + velocity * 0.9 + " R Velocity: " + velocity);
            } else if (deltaAngle < -1) {
                System.out.println("Delta Angle: " + deltaAngle + " deltaAngle<1");
                drivetrain.setDrivetrain(velocity, velocity * 0.9);
                System.out.println("L Velocity: " + velocity + " R Velocity: " + velocity * 0.9);
            } else {
                System.out.println("Delta Angle: " + deltaAngle);
                drivetrain.setDrivetrain(velocity, velocity);
                System.out.println("R + L Velocity: " + velocity);
            }
        }

    @Override
    protected void end() {
        drivetrain.setDrivetrain(0, 0);
        drivetrain.resetEncoders();
    }

    @Override
    protected void interrupted() {
        end();
    } 	

    @Override
    protected boolean isFinished() {
        if (remainingInches <= 0) {
            System.out.println("Finished");
            return true;
        } else {
            return false;
        }
    }
}
