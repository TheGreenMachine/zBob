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

        left.configureEncoder((int) drivetrain.talonPositionLeft(), (int) Drivetrain.TICKS_PER_REV, .1524);
        left.configurePIDVA(.02, 0.0, 0.0, 1 / 1.7, 0);

        right.configureEncoder((int) drivetrain.talonPositionLeft(), (int) Drivetrain.TICKS_PER_REV, .1524);
        right.configurePIDVA(.02, 0.0, 0.0, 1 / 1.7, 0);

        if (drivetrain.getPrevTargetHeading() != null) {
            initAngle = Double.parseDouble(drivetrain.getPrevTargetHeading()); //gets the heading it should be at after rotateX
            drivetrain.setPrevTargetHeading(null);
            System.out.println("init target Angle: " + initAngle);
            System.out.println("initial gyro angle: " + drivetrain.getGyroAngle());
        } else {
            initAngle = drivetrain.getGyroAngle();
        }

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
        double l = left.calculate((int) drivetrain.talonPositionLeft());
        double r = right.calculate((int) drivetrain.talonPositionRight());
//        System.out.println("Left ticks: " + l);
//        System.out.println("Right ticks: " + r);

        double gyroHeading = drivetrain.getGyroAngle();
        double desiredHeading = Pathfinder.r2d(left.getHeading());

        System.out.println("Gyro Heading: " + gyroHeading + "\t Desired Heading: " + desiredHeading);

        double angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);
        double turn = 0.8 * (-1.0 / 80.0) * angleDifference;

        System.out.println("Angle Difference: " + angleDifference);

//        System.out.println("Left Speed: " + (l + turn));
//        System.out.println("Right Speed: " + (r + turn));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(gyroHeading).append(",").append(angleDifference).append(",").append(l).append(",").append((l + turn)).append(",").append(r).append(",").append((r + turn));
        Robot.logger.log(stringBuilder.toString());

        drivetrain.tank(l + turn, r - turn);
    }

    @Override
    protected boolean isFinished() {
        return right.isFinished() && left.isFinished();
    }

    @Override
    protected void end() {
        drivetrain.stop();
    }

    @Override
    protected void interrupted() {
        end();
    }
}
