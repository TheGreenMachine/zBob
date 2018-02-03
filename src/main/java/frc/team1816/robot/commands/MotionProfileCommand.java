package frc.team1816.robot.commands;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.Robot;
import frc.team1816.robot.subsystems.Drivetrain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MotionProfileCommand extends Command {
    private Drivetrain drivetrain;
    private SetValueMotionProfile status;
    private TalonSRX right;
    private TalonSRX left;
    private TrajectoryPoint trajectoryPoint;
    private String csvLocationRight;
    private String csvLocationLeft;
    private String line = "";
    private String cvsSplitBy = ",";
    private List<double[]> pointsRight;
    private List<double[]> pointsLeft;
    private int numPointsRight;
    private int numPointsLeft;

    public MotionProfileCommand(String csvLocationRight, String csvLocationLeft) {
        super("motionprofilecommand");
        this.csvLocationRight = csvLocationRight;
        this.csvLocationLeft = csvLocationLeft;
        drivetrain = Components.getInstance().drivetrain;
        requires(drivetrain);
    }

    @Override
    protected void initialize() {
        pointsRight = new ArrayList<>();
        pointsLeft = new ArrayList<>();
        right = drivetrain.getRightMain();
        left = drivetrain.getLeftMain();
        status = SetValueMotionProfile.Disable;

        right.set(ControlMode.MotionProfile, status.value);
        left.set(ControlMode.MotionProfile, status.value);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvLocationRight))) {
            while ((line = bufferedReader.readLine()) != null) {
                String[] parsedPoints = line.split(cvsSplitBy);
                double[] points = new double[] {
                        Double.valueOf(parsedPoints[0]),
                        Double.valueOf(parsedPoints[1]),
                        Double.valueOf(parsedPoints[2])
                };

                pointsRight.add(points);
            }

            numPointsRight = pointsRight.size();
        } catch (IOException e) {
            System.out.println("ERROR: " + e.fillInStackTrace());
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvLocationLeft))) {
            while ((line = bufferedReader.readLine()) != null) {
                String[] parsedPoints = line.split(cvsSplitBy);
                double[] points = new double[] {
                        Double.valueOf(parsedPoints[0]),
                        Double.valueOf(parsedPoints[1]),
                        Double.valueOf(parsedPoints[2])
                };

                pointsLeft.add(points);
            }

            numPointsLeft = pointsLeft.size();
        } catch (IOException e) {
            System.out.println("ERROR: " + e.fillInStackTrace());
        }

        right.clearMotionProfileTrajectories();
        left.clearMotionProfileTrajectories();

        right.configMotionProfileTrajectoryPeriod(0, 10);
        left.configMotionProfileTrajectoryPeriod(0, 10);

        trajectoryPoint = new TrajectoryPoint();
        TrajectoryPoint.TrajectoryDuration trajectoryDuration = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_0ms;
        trajectoryDuration = trajectoryDuration.valueOf(50);
        if (trajectoryDuration.value != 50) {
            System.out.println("TRAJECTORY ERROR");
        }

        System.out.println("right point size: " + numPointsRight);
        System.out.println("left point size: " + numPointsLeft);

        for (int i = 0; i < numPointsRight; i++) {
            double positionRot = pointsRight.get(i)[0];
            double velocityRPM = pointsRight.get(i)[1];

            trajectoryPoint.position = positionRot * 11029;
            trajectoryPoint.velocity = velocityRPM * 11029 / 600.0;
            trajectoryPoint.profileSlotSelect0 = 0;
            trajectoryPoint.profileSlotSelect1 = 0;
            trajectoryPoint.timeDur = trajectoryDuration;
            trajectoryPoint.zeroPos = i == 0;
            trajectoryPoint.isLastPoint = (i + 1) == numPointsRight;

            right.pushMotionProfileTrajectory(trajectoryPoint);
        }

        for (int i = 0; i < numPointsLeft; i++) {
            double positionRot = pointsLeft.get(i)[0];
            double velocityRPM = pointsLeft.get(i)[1];

            trajectoryPoint.position = positionRot * 11029;
            trajectoryPoint.velocity = velocityRPM * 11029 / 600.0;
            trajectoryPoint.profileSlotSelect0 = 0;
            trajectoryPoint.profileSlotSelect1 = 0;
            trajectoryPoint.timeDur = trajectoryDuration;
            trajectoryPoint.zeroPos = i == 0;
            trajectoryPoint.isLastPoint = (i + 1) == numPointsLeft;

            left.pushMotionProfileTrajectory(trajectoryPoint);
        }

        status = SetValueMotionProfile.Enable;
        right.set(ControlMode.MotionProfile, status.value);
        left.set(ControlMode.MotionProfile, status.value);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Right Ticks");
        stringBuilder.append(",");
        stringBuilder.append("Left Ticks");
        stringBuilder.append(",");
        stringBuilder.append("Right Velocity");
        stringBuilder.append(",");
        stringBuilder.append("Left Velocity");

        Robot.logger.log(stringBuilder.toString());
    }

    @Override
    protected void execute() {
        right.processMotionProfileBuffer();
        left.processMotionProfileBuffer();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(drivetrain.talonPositionRight());
        stringBuilder.append(",");
        stringBuilder.append(drivetrain.talonPositionLeft());
        stringBuilder.append(",");
        stringBuilder.append(right.getActiveTrajectoryVelocity());
        stringBuilder.append(",");
        stringBuilder.append(left.getActiveTrajectoryVelocity());

        Robot.logger.log(stringBuilder.toString());

        System.out.println("Right Position: " + right.getActiveTrajectoryPosition() + "\t Left Position: " + drivetrain.talonPositionLeft());
        System.out.println("Right Velocity: " + right.getSelectedSensorVelocity(0) + "\t Left Velocity: " + left.getSelectedSensorVelocity(0));
    }

    @Override
    protected boolean isFinished() {
        MotionProfileStatus statusRight = new MotionProfileStatus();
        MotionProfileStatus statusLeft = new MotionProfileStatus();

        right.getMotionProfileStatus(statusRight);
        left.getMotionProfileStatus(statusLeft);

        return statusRight.activePointValid && statusRight.isLast && statusLeft.activePointValid && statusLeft.isLast;
    }

    @Override
    protected void end() {
        right.clearMotionProfileTrajectories();
        drivetrain.setDrivetrain(0, 0);
    }

    @Override
    protected void interrupted() {
        end();
    }
}
