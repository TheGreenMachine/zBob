package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
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

    public static final double SLOW_MOD = 0.5;
    private boolean slowMode;

    private TalonSRX rightMain, rightSlaveOne, leftMain, leftSlaveOne;

    public static double p_L = 0.035;
    public static double i_L = 0.007;
    public static double d_L = 0;
    public static double f_L = 1.34;
    public static int izone_L = 15;

    public static double p_R = 0.07;
    public static double i_R = 0.007;
    public static double d_R = 0;
    public static double f_R = 1.34;
    public static int izone_R = 15;

    private double leftPower, rightPower, rotation;

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

        this.rightMain.setNeutralMode(NeutralMode.Brake);
        this.rightSlaveOne.setNeutralMode(NeutralMode.Brake);
        this.leftMain.setNeutralMode(NeutralMode.Brake);
        this.leftSlaveOne.setNeutralMode(NeutralMode.Brake);

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

        this.leftMain.config_kP(0, p_L, 20);
        this.leftMain.config_kI(0, i_L, 20);
        this.leftMain.config_kD(0, d_L, 20);
        this.leftMain.config_kF(0, f_L, 20);
        this.leftMain.config_IntegralZone(0, izone_L, 20);

        this.rightMain.config_kP(0, p_R, 20);
        this.rightMain.config_kI(0, i_R, 20);
        this.rightMain.config_kD(0, d_R, 20);
        this.rightMain.config_kF(0, f_R, 20);
        this.rightMain.config_IntegralZone(0, izone_R, 20);

        this.leftMain.selectProfileSlot(0,0);
        this.rightMain.selectProfileSlot(0,0);
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
        update();
    }

    public void setDrivetrain(double leftPower, double rightPower) {
        this.leftPower = leftPower;
        this.rightPower = rightPower;
        rotation = 0;
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
        leftSlaveOne.getSensorCollection().setQuadraturePosition(0,10); //cimcoder
    }

    @Override
    public void update() {

        if(slowMode) {
            leftPower *= SLOW_MOD;
            rightPower *= SLOW_MOD;
            rotation *= SLOW_MOD;
        }

        double rightVelocity = rightPower /*FOR PID:*/ * MAX_VELOCITY_TICKS_PER_100MS;
        double leftVelocity = leftPower /*FOR PID:*/ * MAX_VELOCITY_TICKS_PER_100MS;

        rightVelocity -= rotation * .55 /*FOR PID:*/ * MAX_VELOCITY_TICKS_PER_100MS;
        leftVelocity += rotation * .55 /*FOR PID:*/ * MAX_VELOCITY_TICKS_PER_100MS;

         rightMain.set(ControlMode.Velocity, rightVelocity);
         leftMain.set(ControlMode.Velocity, leftVelocity);

//       rightMain.set(ControlMode.PercentOutput, rightVelocity);
//       leftMain.set(ControlMode.PercentOutput, leftVelocity);

        // System.out.println("----------------------");
        // System.out.println("L Power: " + leftPower);
        // System.out.println("R Power: " + rightPower);
        // System.out.println("L Velocity In: " + leftVelocity);
        // System.out.println("R Velocity In: " + rightVelocity);
        // System.out.println("L Velocity Out: " + leftMain.getSelectedSensorVelocity(0));
        // System.out.println("R Velocity Out: " + rightMain.getSelectedSensorVelocity(0));
        // System.out.println("----------------------");
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

    public void updatePIDValuesL(double p, double i, double d, double f, int izone) {
        this.p_L = p;
        this.i_L = i;
        this.d_L = d;
        this.f_L = f;
        this.izone_L = izone;

        this.leftMain.config_kP(0, p, 20);
        this.leftMain.config_kI(0, i, 20);
        this.leftMain.config_kD(0, d, 20);
        this.leftMain.config_kF(0, f, 20);
        this.leftMain.config_IntegralZone(0, izone, 20);
    }

    public void updatePIDValuesR(double p, double i, double d, double f, int izone) {
        this.p_R = p;
        this.i_R = i;
        this.d_R = d;
        this.f_R = f;
        this.izone_R = izone;

        this.rightMain.config_kP(0, p, 20);
        this.rightMain.config_kI(0, i, 20);
        this.rightMain.config_kD(0, d, 20);
        this.rightMain.config_kF(0, f, 20);
        this.rightMain.config_IntegralZone(0, izone, 20);
    }
}
