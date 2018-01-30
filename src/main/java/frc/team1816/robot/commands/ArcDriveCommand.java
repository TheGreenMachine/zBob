package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.Robot;
import frc.team1816.robot.subsystems.Drivetrain;

public class ArcDriveCommand extends Command {

    private Drivetrain drivetrain;
    private AnalogInput analogInput;
    private double initPositionLeft;
    private double initPositionRight;
    private double radius;
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

    public ArcDriveCommand(double radius, double speed, double heading) {
        super("arcdrivecommand");
        this.radius = radius;
        this.speed = speed;
        drivetrain = Components.getInstance().drivetrain;
        this.heading = heading;
    }

    @Override
    protected void initialize() {
        System.out.println("ArcDrive Init");
        initPositionLeft = drivetrain.talonPositionLeft();
        initPositionRight = drivetrain.talonPositionRight();

        if (Math.signum(heading) == 1) {
            rightTarget = drivetrain.inchesToTicks((heading / 360) * 2 * Math.PI * (radius - Drivetrain.DRIVETRAIN_WIDTH / 2));
            leftTarget = drivetrain.inchesToTicks((heading / 360) * 2 * Math.PI * (radius + Drivetrain.DRIVETRAIN_WIDTH / 2));
        } else {
            heading *= -1;
            leftTurn = true;

            rightTarget = (heading / 360) * 2 * Math.PI * (radius + Drivetrain.DRIVETRAIN_WIDTH / 2);
            leftTarget = (heading / 360) * 2 * Math.PI * (radius - Drivetrain.DRIVETRAIN_WIDTH / 2);
        }
    }

    @Override
    protected void execute() {
        //double deltaAngle = drivetrain.getGyroAngle() - heading;
        double leftVelocity;
        double rightVelocity;
        double currentPositionLeft = drivetrain.talonPositionLeft() - initPositionLeft;
        double currentPositionRight = drivetrain.talonPositionRight() - initPositionRight;
        double currentInchesLeft = drivetrain.ticksToInches(currentPositionLeft);
        double currentInchesRight = drivetrain.ticksToInches(currentPositionRight);
        StringBuilder sb = new StringBuilder();

        remainingInchesLeft = drivetrain.ticksToInches(leftTarget) - Math.abs(currentInchesLeft);
        remainingInchesRight = drivetrain.ticksToInches(rightTarget) - Math.abs(currentInchesRight);

        //velocity ratios need to be looked at and tweaked

        if (leftTurn) {
            System.out.println("Left Turn");

            leftVelocity = (speed * ((radius - Drivetrain.DRIVETRAIN_WIDTH / 2) / radius));
            System.out.println("Left Velocity: " + leftVelocity);

            rightVelocity = (speed * ((radius + Drivetrain.DRIVETRAIN_WIDTH / 2) / radius));
            System.out.println("Right Velocity: " + rightVelocity);

            if (remainingInchesLeft <= 0) {
                leftVelocity = 0;
                System.out.println("STOPPED LEFT");
            } else if (remainingInchesRight <=0){
                rightVelocity = 0;
                System.out.println("STOPPED RIGHT");
            }
        } else {
            System.out.println("Right Turn");

            rightVelocity = (speed * ((radius - Drivetrain.DRIVETRAIN_WIDTH / 2) / radius));
            System.out.println("Right Velocity: " + rightVelocity);

            leftVelocity = (speed * ((radius + Drivetrain.DRIVETRAIN_WIDTH / 2) / radius));
            System.out.println("Left Velocity: " + leftVelocity);

            if (remainingInchesRight <= 0) {
                rightVelocity = 0;
                System.out.println("STOPPED RIGHT");
            } else if (remainingInchesLeft <= 0) {
                leftVelocity = 0;
                System.out.println("STOPPED LEFT");
            }
        }

        drivetrain.setDrivetrain(leftVelocity, rightVelocity);

        System.out.println("Remaining Inches Left: " + remainingInchesLeft);
        System.out.println("Remaining Inches Right: " + remainingInchesRight);

        sb.append(System.currentTimeMillis());
        sb.append(",");
        sb.append(drivetrain.talonPositionLeft());
        sb.append(",");
        sb.append(drivetrain.talonPositionRight());
        sb.append(leftVelocity);
        sb.append(",");
        sb.append(rightVelocity);
        sb.append(",");
        sb.append(remainingInchesLeft);
        sb.append(",");
        sb.append(remainingInchesRight);

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
            System.out.println("DriveArc Finished");
            return true;
        } else {
            return false;
        }
    }

}
