package frc.team1816.robot.commands;

import com.edinarobotics.utils.log.Logging;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.Robot;
import frc.team1816.robot.subsystems.Drivetrain;

public class ArcDriveCommand extends Command {

    private Drivetrain drivetrain;
    private double initPositionLeft;
    private double initPositionRight;
    private double radius;
    private double speed;
    private double rightTarget;
    private double leftTarget;
    private double remainingInchesRight;
    private double remainingInchesLeft;
    private double heading;
    private boolean leftTurn = false;

    private double leftVelocity, rightVelocity;

    StringBuilder sb;

    public ArcDriveCommand(double radius, double speed, double heading) {
        super("arcdrivecommand");
        this.radius = radius;
        this.speed = speed;
        this.heading = heading;
        drivetrain = Components.getInstance().drivetrain;
    }

    @Override
    protected void initialize() {
        System.out.println("ArcDrive Init");
        initPositionLeft = drivetrain.talonPositionLeft();
        initPositionRight = drivetrain.talonPositionRight();

//        Maximum base speed s.t v(inner) and v(outer) are bound between 1 and -1

        if((speed * radius / (radius - (drivetrain.DRIVETRAIN_WIDTH / 2)) > 1)) {
            speed = (radius - drivetrain.DRIVETRAIN_WIDTH / 2) / radius;
        }
//
//        if (speed > radius / (radius - drivetrain.DRIVETRAIN_WIDTH / 2)) {
//            System.out.println("Speed Overwritten; Base Speed = " + speed);
//            speed = radius / (radius - drivetrain.DRIVETRAIN_WIDTH / 2);
//        }

        if (Math.signum(heading) == 1) {

            //Target distance in inches for each motor
            rightTarget = drivetrain.inchesToTicks((heading / 360) * 2 * Math.PI * (radius - Drivetrain.DRIVETRAIN_WIDTH / 2));
            leftTarget = drivetrain.inchesToTicks((heading / 360) * 2 * Math.PI * (radius + Drivetrain.DRIVETRAIN_WIDTH / 2));
        } else {
            heading *= -1;
            leftTurn = true;

            //Target distance in inches for each motor
            rightTarget = drivetrain.inchesToTicks((heading / 360) * 2 * Math.PI * (radius + Drivetrain.DRIVETRAIN_WIDTH / 2));
            leftTarget = drivetrain.inchesToTicks((heading / 360) * 2 * Math.PI * (radius - Drivetrain.DRIVETRAIN_WIDTH / 2));
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

        sb = new StringBuilder();
        sb.append("System Time");
        sb.append(",");
        sb.append("Talon Pos L");
        sb.append(",");
        sb.append("Talon Pos R");
        sb.append(",");
        sb.append("Talon V L");
        sb.append(",");
        sb.append("Talon V R");
        sb.append(",");
        sb.append("Remaining In L");
        sb.append(",");
        sb.append("Remaining In R");

        Robot.logger.log(sb.toString());
    }

    @Override
    protected void execute() {

        double currentPositionLeft = drivetrain.talonPositionLeft() - initPositionLeft;
        double currentPositionRight = drivetrain.talonPositionRight() - initPositionRight;
        double currentInchesLeft = drivetrain.ticksToInches(currentPositionLeft);
        double currentInchesRight = drivetrain.ticksToInches(currentPositionRight);

        //Calculate remaining inches based on target inches and current inches
        remainingInchesLeft = drivetrain.ticksToInches(leftTarget) - Math.abs(currentInchesLeft);
        remainingInchesRight = drivetrain.ticksToInches(rightTarget) - Math.abs(currentInchesRight);

        drivetrain.setDrivetrain(leftVelocity, rightVelocity);

        System.out.println("Remaining Inches Left: " + remainingInchesLeft + "\t L Inches Traveled: " + drivetrain.ticksToInches(drivetrain.talonPositionLeft() - initPositionLeft));
        System.out.println("Remaining Inches Right: " + remainingInchesRight + "\t R Inches Traveled: " + drivetrain.ticksToInches(drivetrain.talonPositionRight() - initPositionRight));

        //Logging
        sb.append(System.currentTimeMillis());
        sb.append(",");
        sb.append(drivetrain.talonPositionLeft());
        sb.append(",");
        sb.append(drivetrain.talonPositionRight());
        sb.append(",");
        sb.append(leftVelocity);
        sb.append(",");
        sb.append(rightVelocity);
        sb.append(",");
        sb.append(remainingInchesLeft);
        sb.append(",");
        sb.append(remainingInchesRight);
        sb.append(",");
        sb.append(System.currentTimeMillis());

        Robot.logger.log(sb.toString());
        System.out.println(System.currentTimeMillis());
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
        if (remainingInchesLeft <= 0 || remainingInchesRight <= 0) {
            System.out.println("DriveArc Finished");
            return true;
        } else {
            return false;
        }
    }

}
