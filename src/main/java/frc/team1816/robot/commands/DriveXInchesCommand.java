package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;

public class DriveXInchesCommand extends Command {

    private Drivetrain drivetrain;
    private double inches;
    private double speed;
    private double ticks;
    private double remainingInches;
    private double initAngle;
    private static final double ROTATION_OFFSET_P = 0.02;

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
        if(drivetrain.getPrevTargetHeading() != null) {
            initAngle = Double.parseDouble(drivetrain.getPrevTargetHeading()); //gets the heading it should be at after rotateX
            drivetrain.setPrevTargetHeading(null);
        } else {
            initAngle = drivetrain.getGyroAngle();
        }
    }

    @Override
    protected void execute() {
        double deltaAngle = drivetrain.getGyroAngle() - initAngle;
        double velocity;
        double currentPosition = drivetrain.talonPositionLeft();
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

            drivetrain.setDrivetrain(velocity, velocity);

            } else if (deltaAngle > 0) {
                System.out.println("Delta Angle: " + deltaAngle + " deltaAngle>1");
                drivetrain.setDrivetrain(velocity - deltaAngle * ROTATION_OFFSET_P, velocity);
                System.out.println("L Velocity: " + (velocity - deltaAngle * ROTATION_OFFSET_P) + " R Velocity: " + velocity);
            } else if (deltaAngle < 0) {
                System.out.println("Delta Angle: " + deltaAngle + " deltaAngle<1");
                drivetrain.setDrivetrain(velocity, velocity - deltaAngle * ROTATION_OFFSET_P);
                System.out.println("L Velocity: " + velocity + " R Velocity: " + (velocity - deltaAngle * ROTATION_OFFSET_P));
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
