package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;

public class ArcDriveGyroCommand extends Command {

    private Drivetrain drivetrain;

    private double radius;
    private double speed;
    private boolean leftTurn = false;

    private double leftVelocity, rightVelocity;

    private double heading;
    private double initAngle, currentAngle, deltaAngle, targetAngle;

    StringBuilder sb;

    public ArcDriveGyroCommand(double radius, double speed, double heading) {
        super("arcdrivegyrocommand");
        this.radius = radius;
        this.speed = speed;
        this.heading = heading;
        drivetrain = Components.getInstance().drivetrain;
    }

    @Override
    protected void initialize() {
        System.out.println("ArcDrive Gyro-based Init");

        initAngle = drivetrain.getGyroAngle();
        targetAngle = initAngle + heading;

//        Maximum base speed s.t v(inner) and v(outer) are bound between 1 and -1

        if((speed * radius / (radius - (drivetrain.DRIVETRAIN_WIDTH / 2)) > 1)) {
            speed = (radius - drivetrain.DRIVETRAIN_WIDTH / 2) / radius;
        }

        if (Math.signum(heading) == 1) {
            leftTurn = false;
        } else {
            heading *= -1;
            leftTurn = true;
        }

        if (leftTurn) {
            System.out.println("Left Turn");

            //ratio of velocities:
            //v(outer):v(inner) -- (r+w/2):(r-w/2)
            //multiply our base speed by the ratios to produce a scaled speed, capped at 1

            leftVelocity = (speed * (radius / (radius + Drivetrain.DRIVETRAIN_WIDTH / 2)));
            System.out.println("Left Velocity: " + leftVelocity);

            rightVelocity = (speed * (radius / (radius - Drivetrain.DRIVETRAIN_WIDTH / 2)));
            System.out.println("Right Velocity: " + rightVelocity);
        } else {
            System.out.println("Right Turn");

            //ratio of velocities:
            //v(outer):v(inner) -- (r+w/2):(r-w/2)
            //multiply our base speed by the ratios to produce a scaled speed, capped at 1
            rightVelocity = (speed * (radius / (radius + Drivetrain.DRIVETRAIN_WIDTH / 2)));
            System.out.println("Right Velocity: " + rightVelocity);

            leftVelocity = (speed * (radius / (radius - Drivetrain.DRIVETRAIN_WIDTH / 2)));
            System.out.println("Left Velocity: " + leftVelocity);
        }
    }

    @Override
    protected void execute() {
        currentAngle = drivetrain.getGyroAngle();

        drivetrain.setDrivetrain(leftVelocity, rightVelocity);

        System.out.println("Degrees To Turn: " + (Math.abs(targetAngle) - Math.abs(currentAngle)));

    }

    @Override
    protected void end () {
        drivetrain.setDrivetrain(0, 0);
    }

    @Override
    protected void interrupted () {
        end();
    }

    @Override
    protected boolean isFinished () {
        if (currentAngle > targetAngle) {
            System.out.println("DriveArc Finished");
            drivetrain.setDrivetrain(0,0);
            drivetrain.setPrevTargetHeading(Double.toString(targetAngle));
            return true;
        } else {
            return false;
        }
    }

}
