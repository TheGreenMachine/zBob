package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.Robot;
import frc.team1816.robot.subsystems.Drivetrain;

public class DriveXInchesCommand extends Command {

    private Drivetrain drivetrain;
    private double initPosition;
    private double inches;
    private double speed;
//    private double ticks;
    private double remainingInches;
    private double initAngle;
    private boolean tripped = false;

    private double startSpeed;
    private double endSpeed;

    private static final double ROTATION_OFFSET_P = 0.06;
    private final double TOLERANCE = 0.1;
    private final double stopVoltage = 1.6;
    private final double RAMP_UP_INCHES = 6;
    private double RAMP_DOWN_INCHES = 36;

    public DriveXInchesCommand(double inches, double speed) {
        super("drivexinchescommand");
        this.inches = inches;
        this.speed = speed;

        this.endSpeed = 0.2;
        this.startSpeed = 0.2;

        if(inches < RAMP_DOWN_INCHES) {
            RAMP_DOWN_INCHES = inches;
        }

        drivetrain = Components.getInstance().drivetrain;
    }

    public DriveXInchesCommand(double inches, double speed, double startSpeed, double endSpeed) {
        super("drivexinchescommand");
        this.inches = inches;
        this.speed = speed;
        this.startSpeed = startSpeed;
        this.endSpeed = endSpeed;
        drivetrain = Components.getInstance().drivetrain;
    }

    @Override
    protected void initialize() {
        System.out.println("DriveX Init");
//        initPosition = drivetrain.talonPositionLeft();
        initPosition = drivetrain.getLeftTalonInches();

        if (drivetrain.getPrevTargetHeading() != null) {
            initAngle = Double.parseDouble(drivetrain.getPrevTargetHeading()); //gets the heading it should be at after rotateX
            drivetrain.setPrevTargetHeading(null);
            System.out.println("init target angle: " + initAngle);
            System.out.println("init gyro angle: " + drivetrain.getGyroAngle());
        } else {
            initAngle = drivetrain.getGyroAngle();
        }
    }

    @Override
    protected void execute() {
        double deltaAngle = drivetrain.getGyroAngle() - initAngle;
        double leftVelocity;
        double rightVelocity;
//        double currentPosition = drivetrain.talonPositionLeft() - initPosition;
        double currentInches = drivetrain.getLeftTalonInches() - initPosition;
        StringBuilder sb = new StringBuilder();

        remainingInches = inches - Math.abs(currentInches);

        leftVelocity = speed;

//        if(deltaAngle>velocity) {
//            deltaAngle = velocity / ROTATION_OFFSET_P;
//        }

//        deltaAngle = 0;


//        RAMP UP RATE
        if (currentInches < RAMP_UP_INCHES) {
            if (speed > 0) {
                if (leftVelocity * (currentInches / RAMP_UP_INCHES) < startSpeed) {
                    leftVelocity = startSpeed;
                } else {
                    leftVelocity = leftVelocity * (currentInches / RAMP_UP_INCHES);
                }
            } else {
                if (leftVelocity * (currentInches / RAMP_UP_INCHES) > -startSpeed) {
                    leftVelocity = -startSpeed;
                } else {
                    leftVelocity = leftVelocity * (currentInches / RAMP_UP_INCHES);
                }
            }
        }


//        RAMP DOWN RATE
        if (remainingInches < RAMP_DOWN_INCHES) {
            Robot.logger.log("-----ENTERING RAMP DOWN-----");
            if (speed > 0) {
                if ((leftVelocity * (remainingInches / RAMP_DOWN_INCHES)) > endSpeed) {
                    leftVelocity = leftVelocity * (remainingInches / RAMP_DOWN_INCHES);
                } else {
                    leftVelocity = endSpeed;
                }
            } else {
                if ((leftVelocity * (remainingInches / RAMP_DOWN_INCHES)) < -endSpeed) {

                    leftVelocity = leftVelocity * (remainingInches / RAMP_DOWN_INCHES);
                } else {
                    leftVelocity = -endSpeed;
                }
            }
        }

        rightVelocity = leftVelocity;

        if (deltaAngle < 0) {
            Robot.logger.log("-----RIGHT GYRO CORRECTION-----");
            System.out.println("DriveX Correcting Right\t delta angle: " + deltaAngle);
            rightVelocity = rightVelocity - Math.abs(deltaAngle * ROTATION_OFFSET_P);
            System.out.println("L Velocity: " + leftVelocity + " R Velocity: " + rightVelocity);
            System.out.println("---");
        } else if (deltaAngle > 0) {
            Robot.logger.log("-----LEFT GYRO CORRECTION-----");
            System.out.println("DriveX Correcting Left\t delta angle: " + deltaAngle);
            leftVelocity = leftVelocity - Math.abs(deltaAngle * ROTATION_OFFSET_P);
            System.out.println("L Velocity: " + leftVelocity + " R Velocity: " + rightVelocity);
            System.out.println("---");
        } else {
            System.out.println("DriveX Straight\t delta angle: " + deltaAngle);
            System.out.println("R + L Velocity: " + leftVelocity);
            System.out.println("---");
            rightVelocity = leftVelocity;
        }

        drivetrain.setDrivetrain(leftVelocity, rightVelocity);

        System.out.println("Remaining Inches: " + remainingInches);
        System.out.println("Inches Traveled: " + currentInches);
        System.out.println("Talon Pos L: " + drivetrain.talonPositionLeft());

//        sb.append(System.currentTimeMillis());
//        sb.append(",");
//        sb.append(drivetrain.getLeftTalonInches());
//        sb.append(",");
//        sb.append(drivetrain.getRightTalonInches());
//        sb.append(",");
//        sb.append(leftVelocity);
//        sb.append(",");
//        sb.append(rightVelocity);
//        sb.append(",");
//        sb.append(drivetrain.getLeftTalonVelocity());
//        sb.append(",");
//        sb.append(drivetrain.getRightTalonVelocity());
//        sb.append(",");
//        sb.append(drivetrain.getGyroAngle());
//
//        Robot.logger.log(sb.toString());
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
            System.out.println("DriveX Finished");
            return true;
        } else {
            return false;
        }
    }
}
