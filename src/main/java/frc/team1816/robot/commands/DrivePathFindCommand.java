package frc.team1816.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.Robot;
import frc.team1816.robot.subsystems.Drivetrain;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

import java.io.File;

public class DrivePathFindCommand extends Command {
    private Drivetrain drivetrain;
    private Waypoint startPoints;
    private Waypoint midPoint;
    private Waypoint endPoints;
    private Trajectory trajectory;
    private Trajectory rightTrajectory;
    private Trajectory leftTrajectory;
    private EncoderFollower left;
    private EncoderFollower right;
    private double initAngle;
    private int iteration = 0;

    public DrivePathFindCommand(Waypoint startPoints, Waypoint midPoint, Waypoint endPoints) {
        super("drivepathfindcommand");
        drivetrain = Components.getInstance().drivetrain;
        this.startPoints = startPoints;
        this.midPoint = midPoint;
        this.endPoints = endPoints;
    }

    @Override
    protected void initialize() {
        System.out.println("Init started");

        Waypoint[] waypoints = new Waypoint[]{
                startPoints,
                midPoint,
                endPoints
        };

        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60);
        System.out.println("Trajectory configured");
//        trajectory = Pathfinder.generate(waypoints, config);

        System.out.println("Reading file");
        File rightCsv = new File("/home/lvuser/pathFinder/right_detailed.csv");
        File leftCsv = new File("/home/lvuser/pathFinder/left_detailed.csv");

        rightTrajectory = Pathfinder.readFromCSV(rightCsv);
        leftTrajectory = Pathfinder.readFromCSV(leftCsv);
        System.out.println("Read CSV");

//        TankModifier modifier = new TankModifier(trajectory).modify(Drivetrain.DRIVETRAIN_WIDTH_METERS);

//        System.out.println(trajectory.length() + " Trajectories calculated");

        left = new EncoderFollower(leftTrajectory);
        right = new EncoderFollower(rightTrajectory);

        left.configureEncoder((int) drivetrain.talonPositionLeft(), (int) Drivetrain.TICKS_PER_REV, .3333);
        left.configurePIDVA(.05, 0.005, 0.0, 1 / 4, 0);

        right.configureEncoder((int) drivetrain.talonPositionLeft(), (int) Drivetrain.TICKS_PER_REV, .3333);
        right.configurePIDVA(.05, 0.005, 0.0, 1 / 4, 0);

        initAngle = drivetrain.getGyroAngle();

//        File save = new File("trajectory.csv");
//        Pathfinder.writeToCSV(save, trajectory);
//
//        System.out.println("File Path: " + save.getAbsolutePath());
        System.out.println("Init completed");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Gyro Heading");
        stringBuilder.append(",");
        stringBuilder.append("Correction");
        stringBuilder.append(",");
        stringBuilder.append("Left Power");
        stringBuilder.append(",");
        stringBuilder.append("Left Power + Turn");
        stringBuilder.append(",");
        stringBuilder.append("Right Power");
        stringBuilder.append(",");
        stringBuilder.append("Right Power + Turn");
        stringBuilder.append(",");
        Robot.logger.log(stringBuilder.toString());
    }

    @Override
    protected void execute() {
        iteration++;
        double l = left.calculate((int) drivetrain.talonPositionLeft());
        double r = right.calculate((int) drivetrain.talonPositionRight());
//        System.out.println("Left ticks: " + l);
//        System.out.println("Right ticks: " + r);

        double gyroHeading = drivetrain.getGyroAngle();
        double desiredHeading = Pathfinder.r2d(left.getHeading());

        System.out.println("Gyro Heading: " + gyroHeading + "\t Desired Heading: " + desiredHeading);

        double angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);
        double turn = 0.03 * angleDifference;

        if (turn > 0) {
            turn = Math.min(turn, 0.4);
        } else if (turn < 0) {
            turn = Math.max(turn, -0.4);
        } else {
            turn = 0;
        }

        System.out.println("Angle Difference: " + angleDifference);

//        System.out.println("Left Speed: " + (l + turn));
//        System.out.println("Right Speed: " + (r + turn));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(gyroHeading).append(",").append(angleDifference).append(",").append(l).append(",").append((l + turn)).append(",").append(r).append(",").append((r + turn));
        Robot.logger.log(stringBuilder.toString());
        System.out.println("Left Talon Ticks: " + drivetrain.talonPositionLeft());
        System.out.println("Right Talon Ticks: " + drivetrain.talonPositionRight());

//        Trajectory.Segment testSegmentLeft = leftTrajectory.get(iteration);
//        double testLeft = testSegmentLeft.velocity;
//        Trajectory.Segment testSegmentRight = rightTrajectory.get(iteration);
//        double testRight = testSegmentRight.velocity;

        drivetrain.setDrivetrain(l, r);
    }

    @Override
    protected boolean isFinished() {
//        return rightTrajectory.length() < iteration;
        return right.isFinished() && left.isFinished();
    }

    @Override
    protected void end() {
        drivetrain.setDrivetrain(0, 0);
    }

    @Override
    protected void interrupted() {
        end();
    }
}
