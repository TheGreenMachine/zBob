package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.Robot;
import frc.team1816.robot.subsystems.Drivetrain;

public class DriveAndRotateCommand extends Command {

    private Drivetrain drivetrain;
    private AnalogInput analogInput;
    private double initPositionLeft;
    private double initPositionRight;
    private double inches;
    private double speed;
    private double ticks;
    private double rightTarget;
    private double leftTarget;
    private double remainingInchesRight;
    private double remainingInchesLeft;
    private double heading;
    private boolean leftTurn = false;
    private boolean tripped = false;
    private static final double ROTATION_OFFSET_P = 0.03;

    public DriveAndRotateCommand(double inches, double speed, double heading) {
        super("drivexinchescommand");
        this.inches = inches;
        this.speed = speed;
        drivetrain = Components.getInstance().drivetrain;
        analogInput = Components.getInstance().ai;
        analogInput.setOversampleBits(4);
        analogInput.setAverageBits(2);
        AnalogInput.setGlobalSampleRate(62500);
        ticks = (int) (inches * Drivetrain.TICKS_PER_INCH);
        this.heading = heading;
        //drivetrain.getRightMain().setSelectedSensorPosition(0,0,10);
        //drivetrain.getLeftMain().setSelectedSensorPosition(0,0,10);
    }

    @Override
    protected void initialize() {
        System.out.println("DriveX Init");
        initPositionLeft = drivetrain.talonPositionLeft();
        initPositionRight = drivetrain.talonPositionRight();

        if (Math.signum(heading) == 1) {
            rightTarget = (heading / 360) * 2 * Math.PI * (inches);
            leftTarget = (heading / 360) * 2 * Math.PI * (inches + Drivetrain.WHEEL_THICKNESS);
        } else {
            heading *= -1;
            leftTurn = true;

            rightTarget = (heading / 360) * 2 * Math.PI * (inches + Drivetrain.WHEEL_THICKNESS);
            leftTarget = (heading / 360) * 2 * Math.PI * (inches);
        }
    }

    @Override
    protected void execute() {
        double deltaAngle = drivetrain.getGyroAngle() - heading;
        double leftVelocity;
        double rightVelocity;
        double currentPositionLeft = drivetrain.talonPositionLeft() - initPositionLeft;
        double currentPositionRight = drivetrain.talonPositionRight() - initPositionRight;
        double currentInchesLeft = currentPositionLeft / Drivetrain.TICKS_PER_INCH;
        double currentInchesRight = currentPositionRight / Drivetrain.TICKS_PER_INCH;
        StringBuilder sb = new StringBuilder();

        remainingInchesLeft = inches - Math.abs(currentInchesLeft);
        remainingInchesRight = inches - Math.abs(currentInchesRight);

        if (leftTurn) {
            if (remainingInchesLeft <= 0) {
                leftVelocity = 0;
                System.out.println("STOPPED LEFT");
            } else {
                leftVelocity = (speed * inches) / 100;
                System.out.println("Left Velocity: " + leftVelocity);
            }

            rightVelocity = (speed * (inches + Drivetrain.WHEEL_THICKNESS)) / 100;
            System.out.println("Right Velocity: " + rightVelocity);
        } else {
            if (remainingInchesRight <= 0) {
                rightVelocity = 0;
                System.out.println("STOPPED RIGHT");
            } else {
                rightVelocity = (speed * inches) / 100;
                System.out.println("Right Velocity: " + rightVelocity);
            }
            leftVelocity = (speed * (inches + Drivetrain.WHEEL_THICKNESS)) / 100;
            System.out.println("Left Velocity: " + leftVelocity);
        }

        drivetrain.setDrivetrain(leftVelocity, rightVelocity);

//        if (remainingInches < 6) {
//            if (speed > 0) {
//                if ((leftVelocity * (remainingInches / 6)) > .15) {
//
//                    leftVelocity = leftVelocity * (remainingInches / 6);
//                } else {
//                    leftVelocity = .15;
//                }
//            } else {
//                if ((leftVelocity * (remainingInches / 6)) < -.15) {
//
//                    leftVelocity = leftVelocity * (remainingInches / 6);
//                } else {
//                    leftVelocity = -.15;
//                }
//            }
//
//            rightVelocity = leftVelocity;
//            Robot.logger.log("Ticks:" + "," + drivetrain.talonPositionRight());
//            drivetrain.setDrivetrain(leftVelocity, rightVelocity);
//
//        } else if (deltaAngle < 0) {
//            System.out.println("DriveX Correcting Right\t delta angle: " + deltaAngle);
//            rightVelocity = rightVelocity - Math.abs(deltaAngle * ROTATION_OFFSET_P);
//            drivetrain.setDrivetrain(leftVelocity, rightVelocity);
//            System.out.println("L Velocity: " + leftVelocity + " R Velocity: " + rightVelocity);
//            System.out.println("---");
//        } else if (deltaAngle > 0) {
//            System.out.println("DriveX Correcting Left\t delta angle: " + deltaAngle);
//            drivetrain.setDrivetrain(leftVelocity, rightVelocity);
//            leftVelocity = leftVelocity - Math.abs(deltaAngle * ROTATION_OFFSET_P);
//            System.out.println("L Velocity: " + leftVelocity + " R Velocity: " + rightVelocity);
//            System.out.println("---");
//        } else {
//            System.out.println("DriveX Straight\t delta angle: " + deltaAngle);
//            drivetrain.setDrivetrain(leftVelocity, rightVelocity);
//            System.out.println("R + L Velocity: " + leftVelocity);
//            System.out.println("---");
//        }

        System.out.println("Remaining Inches Left: " + remainingInchesLeft);
        System.out.println("Remaining Inches Right: " + remainingInchesRight);

        sb.append(System.currentTimeMillis());
        sb.append(",");
        sb.append(drivetrain.talonPositionLeft());
        sb.append(",");
        sb.append(leftVelocity);
        sb.append(",");
        sb.append(rightVelocity);

        Robot.logger.log(sb.toString());
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
        if (remainingInchesLeft <= 0 && remainingInchesRight <= 0) {
            System.out.println("DriveX Finished");
            return true;
        } else {
            return false;
        }
    }

}
