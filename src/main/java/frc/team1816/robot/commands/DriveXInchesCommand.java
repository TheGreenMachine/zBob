package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.Robot;
import frc.team1816.robot.subsystems.Drivetrain;

public class DriveXInchesCommand extends Command {

    private Drivetrain drivetrain;
    private AnalogInput analogInput;
    private double initPosition;
    private double inches;
    private double speed;
    private double ticks;
    private double remainingInches;
    private double initAngle;
    private boolean tripped = false;
    private static final double ROTATION_OFFSET_P = 0.03;
    private final double TOLERANCE = 0.1;
    private final double stopVoltage = 1.6;
    private final boolean USE_IR;

    public DriveXInchesCommand(double inches, double speed, boolean useIr) {
        super("drivexinchescommand");
        this.inches = inches;
        this.speed = speed;
        drivetrain = Components.getInstance().drivetrain;
        analogInput = Components.getInstance().ai;
        analogInput.setOversampleBits(4);
        analogInput.setAverageBits(2);
        AnalogInput.setGlobalSampleRate(62500);
        ticks = (int) (inches * Drivetrain.TICKS_PER_INCH);
        USE_IR = useIr;
        //drivetrain.getRightMain().setSelectedSensorPosition(0,0,10);
        //drivetrain.getLeftMain().setSelectedSensorPosition(0,0,10);
    }

    @Override
    protected void initialize() {
        System.out.println("DriveX Init");
        initPosition = drivetrain.talonPositionLeft();
        if (drivetrain.getPrevTargetHeading() != null) {
            initAngle = Double.parseDouble(drivetrain.getPrevTargetHeading()); //gets the heading it should be at after rotateX
            drivetrain.setPrevTargetHeading(null);
            System.out.println("init target Angle: " + initAngle);
            System.out.println("intial gyro angle: " + drivetrain.getGyroAngle());
        } else {
            initAngle = drivetrain.getGyroAngle();
        }
    }

    @Override
    protected void execute() {
        double deltaAngle = drivetrain.getGyroAngle() - initAngle;
        double leftVelocity;
        double rightVelocity;
        double currentPosition = drivetrain.talonPositionLeft() - initPosition;
        double currentInches = currentPosition / Drivetrain.TICKS_PER_INCH;
        StringBuilder sb = new StringBuilder();

        if (USE_IR) {
            int rawData = analogInput.getValue();
            double volts = analogInput.getVoltage();
            System.out.println("raw " + rawData + ", volts " + volts);

            if (Math.abs(stopVoltage - volts) <= TOLERANCE && !tripped) {
                System.out.println("-----IR TRIPPED, SLOWING DOWN-----");
                initPosition = drivetrain.talonPositionLeft();
                inches = 6;
                currentPosition = drivetrain.talonPositionLeft() - initPosition;
                currentInches = currentPosition / Drivetrain.TICKS_PER_INCH;
                tripped = true;
            }
        }

        remainingInches = inches - Math.abs(currentInches);
//        System.out.println("---");
//        System.out.println("Remaining inches: " + remainingInches);
//        System.out.println("Current inches: " + currentInches);
//        System.out.println("Current Position: " + currentPosition);
//        System.out.println("---");

        leftVelocity = speed;
        rightVelocity = speed;

//        if(deltaAngle>velocity) {
//            deltaAngle = velocity / ROTATION_OFFSET_P;
//        }

//        deltaAngle = 0;

        if(currentInches < 6) {
            if(speed > 0) {
                if(leftVelocity * (currentInches/6) < .1) {
                    leftVelocity = .15;
                }
                else {
                    leftVelocity = leftVelocity * (currentInches / 6);
                }
            }
            else {
                if(leftVelocity * (currentInches/6) > -.1) {
                    leftVelocity = -.15;
                }
                else {
                    leftVelocity = leftVelocity * (currentInches / 6);
                }
            }
        }

        if (remainingInches < 10) {
            if (speed > 0) {
                if ((leftVelocity * (remainingInches / 6)) > .15) {
                    leftVelocity = leftVelocity * (remainingInches / 6);
                } else {
                    leftVelocity = .15;
                }
            } else {
                if ((leftVelocity * (remainingInches / 6)) < -.15) {

                    leftVelocity = leftVelocity * (remainingInches / 6);
                } else {
                    leftVelocity = -.15;
                }
            }
        }

            rightVelocity = leftVelocity;
            Robot.logger.log("Ticks:" + "," + drivetrain.talonPositionRight());
//            drivetrain.setDrivetrain(leftVelocity, rightVelocity);

//         } else if
         if (deltaAngle < 0) {
            System.out.println("DriveX Correcting Right\t delta angle: " + deltaAngle);
            rightVelocity = rightVelocity - Math.abs(deltaAngle * ROTATION_OFFSET_P);
            drivetrain.setDrivetrain(leftVelocity, rightVelocity);
            System.out.println("L Velocity: " + leftVelocity + " R Velocity: " + rightVelocity);
            System.out.println("---");
        } else if (deltaAngle > 0) {
            System.out.println("DriveX Correcting Left\t delta angle: " + deltaAngle);
            drivetrain.setDrivetrain(leftVelocity, rightVelocity);
            leftVelocity = leftVelocity - Math.abs(deltaAngle * ROTATION_OFFSET_P);
            System.out.println("L Velocity: " + leftVelocity + " R Velocity: " + rightVelocity);
            System.out.println("---");
        } else {
            System.out.println("DriveX Straight\t delta angle: " + deltaAngle);
            drivetrain.setDrivetrain(leftVelocity, rightVelocity);
            System.out.println("R + L Velocity: " + leftVelocity);
            System.out.println("---");
        }

        System.out.println("Remaining Inches: " + remainingInches);

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
        double volts = analogInput.getVoltage();

        if (remainingInches <= 0) {
            System.out.println("DriveX Finished");
            return true;
        } else {
            return false;
        }
    }
}
