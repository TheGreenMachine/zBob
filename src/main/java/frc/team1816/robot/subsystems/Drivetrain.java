package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.edinarobotics.utils.subsystems.Subsystem1816;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.I2C;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class Drivetrain extends Subsystem1816{
    public static double TICKS_PER_REV;
    public static double TICKS_PER_INCH;

    public static double DRIVETRAIN_WIDTH;
    public static double INCHES_PER_REV;
    public static double MAX_VELOCITY_TICKS_PER_100MS;
    private static final double SET_SPEED_DIFF_MAX = 0.01; //limit acceleration to 50% per second, 0-->max in 2s

    public static final double SLOW_MOD = 0.5;
    private boolean slowMode, isPercentOut;

    private TalonSRX rightMain, rightSlaveOne, leftMain, leftSlaveOne;

    //PID values set in properties file;
    public static double kP = 0;
    public static double kI = 0;
    public static double kD = 0;
    public static double kF = 0;
    public static int izone = 0;

    private double leftPower, rightPower, rotation;
    private double prevPowerL = 0, prevPowerR;

    private String prevHeadingTarget;

    private AHRS navx;

    public Drivetrain(int rightMain, int rightSlaveOne, int leftMain, int leftSlaveOne){
        super();
        Properties properties = new Properties();
        try {
            FileInputStream in = new FileInputStream("/home/lvuser/drivetrain.properties");
            properties.load(in);
            in.close();
        } catch (Exception e){
            System.out.println("properties file not found");
        }

        TICKS_PER_REV = Double.valueOf(properties.getProperty("TICKS_PER_REV"));
        TICKS_PER_INCH = Double.valueOf(properties.getProperty("TICKS_PER_INCH"));
        DRIVETRAIN_WIDTH = Double.valueOf(properties.getProperty("DRIVETRAIN_WIDTH"));
        MAX_VELOCITY_TICKS_PER_100MS = Double.valueOf(properties.getProperty("MAX_VELOCITY_TICKS_PER_100MS"));
        INCHES_PER_REV = TICKS_PER_REV/TICKS_PER_INCH;

        kP = Double.valueOf(properties.getProperty("kP"));
        kI = Double.valueOf(properties.getProperty("kI"));
        kD = Double.valueOf(properties.getProperty("kD"));
        kF = Double.valueOf(properties.getProperty("kF"));
        izone = Integer.valueOf(properties.getProperty("izone"));

        System.out.println("Ticks per inch:" + TICKS_PER_INCH +
                "\nTicks per rev " + TICKS_PER_REV +
                "\nDrivetrain width " + DRIVETRAIN_WIDTH +
                "\nMax velocity " + MAX_VELOCITY_TICKS_PER_100MS);
        System.out.println("properties loaded");


        this.rightMain = new TalonSRX(rightMain);
        this.rightSlaveOne = new TalonSRX(rightSlaveOne);
        this.leftMain = new TalonSRX(leftMain);
        this.leftSlaveOne = new TalonSRX(leftSlaveOne);

        navx = new AHRS(I2C.Port.kMXP);

        this.rightMain.setInverted(true);
        this.rightSlaveOne.setInverted(true);

        this.rightMain.setNeutralMode(NeutralMode.Coast);
        this.rightSlaveOne.setNeutralMode(NeutralMode.Coast);
        this.leftMain.setNeutralMode(NeutralMode.Coast);
        this.leftSlaveOne.setNeutralMode(NeutralMode.Coast);

        this.rightSlaveOne.set(ControlMode.Follower, rightMain);
        this.leftSlaveOne.set(ControlMode.Follower, leftMain);

        this.rightMain.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        this.leftMain.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);

        this.rightMain.configNominalOutputForward(0, 10);
        this.rightMain.configNominalOutputReverse(0, 10);
        this.rightMain.configPeakOutputForward(1, 10);
        this.rightMain.configPeakOutputReverse(-1, 10);

        this.leftMain.configNominalOutputForward(0, 10);
        this.leftMain.configNominalOutputReverse(0, 10);
        this.leftMain.configPeakOutputForward(1, 10);
        this.leftMain.configPeakOutputReverse(-1, 10);

        this.leftMain.config_kP(0, kP, 20);
        this.leftMain.config_kI(0, kI, 20);
        this.leftMain.config_kD(0, kD, 20);
        this.leftMain.config_kF(0, kF, 20);
        this.leftMain.config_IntegralZone(0, izone, 20);

        this.rightMain.config_kP(0, kP, 20);
        this.rightMain.config_kI(0, kI, 20);
        this.rightMain.config_kD(0, kD, 20);
        this.rightMain.config_kF(0, kF, 20);
        this.rightMain.config_IntegralZone(0, izone, 20);

        this.leftMain.selectProfileSlot(0,0);
        this.rightMain.selectProfileSlot(0,0);

        //TODO || code is to config practice bot
//        this.leftMain.setSensorPhase(true);
//        this.rightMain.setSensorPhase(true);

        this.rightMain.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_20Ms,0);
        this.leftMain.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_20Ms,0);

        this.rightMain.configVelocityMeasurementWindow(8,0);
        this.leftMain.configVelocityMeasurementWindow(8,0);

        System.out.println("NavX Active: " + gyroActiveCheck());
    }

    public TalonSRX getRightMain() {
        return rightMain;
    }

    public TalonSRX getLeftMain() {
        return leftMain;
    }

    public double getGyroAngle() {
        return navx.getAngle();
    }

    public boolean gyroActiveCheck() {
        return navx.isConnected();
    }

    public void setDrivetrain(double leftPower, double rightPower, double rotation) {
        this.leftPower = leftPower;
        this.rightPower = rightPower;
        this.rotation = rotation;
        isPercentOut = false;

        update();
    }

    public void setDrivetrain(double leftPower, double rightPower) {
        this.leftPower = leftPower;
        this.rightPower = rightPower;
        this.rotation = 0;
        isPercentOut = false;

        update();
    }

    public void setDrivetrainPercent(double percentOutLeft, double percentOutRight, double rotation) {
        this.leftPower = percentOutLeft;
        this.rightPower = percentOutRight;
        this.rotation = rotation;
        isPercentOut = true;

        update();
    }

    public double talonPositionRight() {
        return rightMain.getSelectedSensorPosition(0);
    }

    public double talonPositionLeft() {
        return leftMain.getSelectedSensorPosition(0);
    }

    public double getLeftTalonInches() {
        return ticksToInches(leftMain.getSelectedSensorPosition(0));
    }

    public double getRightTalonInches() {
        return ticksToInches(rightMain.getSelectedSensorPosition(0));
    }

    public void resetEncoders() {
        rightMain.getSensorCollection().setQuadraturePosition(0, 10); //grayhill encoder
        leftMain.getSensorCollection().setQuadraturePosition(0, 10); // grayhill encoder
    }

    public static double leftSetV, rightSetV;

    public double getLeftSetV() {
        return leftSetV;
    }

    public double getRightSetV() {
        return rightSetV;
    }

    public void setDrivetrainCoastMode() {
        this.rightMain.setNeutralMode(NeutralMode.Coast);
        this.rightSlaveOne.setNeutralMode(NeutralMode.Coast);
        this.leftMain.setNeutralMode(NeutralMode.Coast);
        this.leftSlaveOne.setNeutralMode(NeutralMode.Coast);
    }

    public void setDrivetrainBrakeMode() {
        this.rightMain.setNeutralMode(NeutralMode.Brake);
        this.rightSlaveOne.setNeutralMode(NeutralMode.Brake);
        this.leftMain.setNeutralMode(NeutralMode.Brake);
        this.leftSlaveOne.setNeutralMode(NeutralMode.Brake);
    }

    @Override
    public void update() {

        rightPower += rotation * .55;
        leftPower += rotation * .55;

        if(Math.abs(leftPower - prevPowerL) > SET_SPEED_DIFF_MAX && leftPower != prevPowerL) {
            if(leftPower > prevPowerL) {
                leftPower += SET_SPEED_DIFF_MAX;
            } else if (leftPower < prevPowerL) {
                leftPower -= SET_SPEED_DIFF_MAX;
            }
        }

        if(Math.abs(rightPower - prevPowerR) > SET_SPEED_DIFF_MAX && rightPower != prevPowerR) {
            if(rightPower > prevPowerR) {
                rightPower += SET_SPEED_DIFF_MAX;
            } else if (rightPower < prevPowerR) {
                rightPower -= SET_SPEED_DIFF_MAX;
            }
        }

        prevPowerL = leftPower;
        prevPowerR = rightPower;

        if(slowMode) {
            leftPower *= SLOW_MOD;
            rightPower *= SLOW_MOD;
            rotation *= 0.8;
        }

        double rightVelocity = rightPower /*FOR PID:*/ * MAX_VELOCITY_TICKS_PER_100MS;
        double leftVelocity = leftPower /*FOR PID:*/ * MAX_VELOCITY_TICKS_PER_100MS;

//        leftVelocity += rotation * .55 /*FOR PID:*/ * MAX_VELOCITY_TICKS_PER_100MS;
//        rightVelocity -= rotation * .55 /*FOR PID:*/ * MAX_VELOCITY_TICKS_PER_100MS;

        if(isPercentOut) {
            System.out.println("setting percent output");
            leftMain.set(ControlMode.PercentOutput, leftPower);
            rightMain.set(ControlMode.PercentOutput, rightPower);
        } else {
            rightMain.set(ControlMode.Velocity, rightVelocity);
            leftMain.set(ControlMode.Velocity, leftVelocity);
        }

        leftSetV = leftVelocity;
        rightSetV = rightVelocity;

//         System.out.println("----------------------");
//         System.out.println("L Power: " + leftPower);
//         System.out.println("R Power: " + rightPower);
//         System.out.print("L Velocity In: " + leftVelocity);
//         System.out.println("\tR Velocity In: " + rightVelocity);
//         System.out.println("L Velocity Out: " + leftMain.getSelectedSensorVelocity(0));
//         System.out.println("R Velocity Out: " + rightMain.getSelectedSensorVelocity(0));
//         System.out.println("L Ticks: " + talonPositionLeft());
//         System.out.println("R Ticks: " + talonPositionRight());
//         System.out.println("----------------------");
    }

    public void setSlowMode(boolean slowModeToggle) {
        this.slowMode = slowModeToggle;
        update();
    }

    public void setPrevTargetHeading(String heading) {
        this.prevHeadingTarget = heading;
    }

    public String getPrevTargetHeading() {
        return prevHeadingTarget;
    }

    public double getLeftTalonVelocity() {
        return leftMain.getSelectedSensorVelocity(0);
    }

    public double getRightTalonVelocity() {
        return rightMain.getSelectedSensorVelocity(0);
    }

    public double inchesToTicks(double inches) {
        return (inches / (6.25 * Math.PI)) * TICKS_PER_REV;
    }

    public double ticksToInches(double ticks) {
        return ticks * (1 / TICKS_PER_REV) * INCHES_PER_REV;
    }

    public void updatePIDValues(double p, double i, double d, double f, int izone) {
        this.kP = p;
        this.kI = i;
        this.kD = d;
        this.kF = f;
        this.izone = izone;

        this.leftMain.config_kP(0, p, 20);
        this.leftMain.config_kI(0, i, 20);
        this.leftMain.config_kD(0, d, 20);
        this.leftMain.config_kF(0, f, 20);
        this.leftMain.config_IntegralZone(0, izone, 20);

        this.rightMain.config_kP(0, p, 20);
        this.rightMain.config_kI(0, i, 20);
        this.rightMain.config_kD(0, d, 20);
        this.rightMain.config_kF(0, f, 20);
        this.rightMain.config_IntegralZone(0, izone, 20);
    }

    public String getLogString() {
        return ""  + System.currentTimeMillis() + "," + getLeftTalonInches() + "," + getRightTalonInches()
                + "," +getLeftTalonVelocity() + "," +getRightTalonVelocity() + "," + leftPower + "," + rightPower
                + "," + rotation + "," + getGyroAngle();
    }

    public String getPIDTuningString() {
        return "" + getLeftTalonVelocity() + "," + getRightTalonVelocity();
    }
}

