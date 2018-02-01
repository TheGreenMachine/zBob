package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
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
    private Waypoint endPoints;
    private Trajectory trajectory;
    private EncoderFollower left;
    private EncoderFollower right;
    private double initAngle;

    public DrivePathFindCommand(Waypoint startPoints, Waypoint endPoints) {
        super("drivepathfindcommand");
        drivetrain = Components.getInstance().drivetrain;
        this.startPoints = startPoints;
        this.endPoints = endPoints;
    }

    @Override
    protected void initialize() {
        System.out.println("Init started");

        Waypoint[] waypoints = new Waypoint[] {
                startPoints,
                endPoints
        };

        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.0, 2.0, 60);
        System.out.println("Trajectory configured");
        trajectory = Pathfinder.generate(waypoints, config);

        TankModifier modifier = new TankModifier(trajectory).modify(.5);

        System.out.println(trajectory.length() + " Trajectories calculated");

        left = new EncoderFollower(modifier.getLeftTrajectory());
        right = new EncoderFollower(modifier.getRightTrajectory());

        left.configureEncoder((int)drivetrain.talonPositionLeft(),(int) Drivetrain.TICKS_PER_REV, .1524);
        left.configurePIDVA(1.0, 0.0, 0.0, 1/1.7,0);

        right.configureEncoder((int)drivetrain.talonPositionLeft(),(int) Drivetrain.TICKS_PER_REV, .1524);
        right.configurePIDVA(1.0, 0.0, 0.0, 1/1.7,0);

        if (drivetrain.getPrevTargetHeading() != null) {
            initAngle = Double.parseDouble(drivetrain.getPrevTargetHeading()); //gets the heading it should be at after rotateX
            drivetrain.setPrevTargetHeading(null);
            System.out.println("init target Angle: " + initAngle);
            System.out.println("initial gyro angle: " + drivetrain.getGyroAngle());
        } else {
            initAngle = drivetrain.getGyroAngle();
        }

        File save = new File("trajectory.csv");
        Pathfinder.writeToCSV(save, trajectory);

        System.out.println("Init completed");
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

        drivetrain.setDrivetrain(l + turn, r - turn);
    }

    @Override
    protected boolean isFinished() {
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
