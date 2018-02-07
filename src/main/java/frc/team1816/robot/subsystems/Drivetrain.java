package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.edinarobotics.utils.subsystems.Subsystem1816;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.I2C;

public class Drivetrain extends Subsystem1816 {
//    COMPETITION ROBOT CONSTANTS
    public static final double TICKS_PER_REV = 1025;
    public static final double TICKS_PER_INCH = 47.73; //52.2028;

//    PRACTICE ROBOT CONSTANTS
//    public static final double TICKS_PER_REV = 9900;
//    public static final double TICKS_PER_INCH = 785;

    public static final double DRIVETRAIN_WIDTH = 24;
    public static final double INCHES_PER_REV = TICKS_PER_REV/TICKS_PER_INCH;
    public static final double MAX_VELOCITY_TICKS_PER_100MS = 750;

    public static final double SLOW_MOD = 0.5;
    private boolean slowMode;

    private TalonSRX rightMain, rightSlaveOne, rightSlaveTwo, leftMain, leftSlaveOne, leftSlaveTwo;
    public double p = 0.05;
    public double i = 0.007;
    public double d = 0;
    public double f = 1.34;
    public int izone = 15;
    private double ramprate = 36;
    private int profile = 0;

    private double leftPower, rightPower, rotation;

    private String prevHeadingTarget;

    private AHRS navx;

    public Drivetrain(int rightMain, int rightSlaveOne, int rightSlaveTwo, int leftMain, int leftSlaveOne, int leftSlaveTwo){
        super();
        this.rightMain = new TalonSRX(rightMain);
        this.rightSlaveOne = new TalonSRX(rightSlaveOne);
        this.rightSlaveTwo = new TalonSRX(rightSlaveTwo);
        this.leftMain = new TalonSRX(leftMain);
        this.leftSlaveOne = new TalonSRX(leftSlaveOne);
        this.leftSlaveTwo = new TalonSRX(leftSlaveTwo);

        navx = new AHRS(I2C.Port.kMXP);

        this.rightMain.setInverted(true);
        this.rightSlaveOne.setInverted(true);
        this.rightSlaveTwo.setInverted(true);

        this.rightMain.setNeutralMode(NeutralMode.Brake);
        this.rightSlaveOne.setNeutralMode(NeutralMode.Brake);
        this.rightSlaveTwo.setNeutralMode(NeutralMode.Brake);
        this.leftMain.setNeutralMode(NeutralMode.Brake);
        this.leftSlaveOne.setNeutralMode(NeutralMode.Brake);
        this.leftSlaveTwo.setNeutralMode(NeutralMode.Brake);

        this.rightSlaveOne.set(ControlMode.Follower, rightMain);
        this.rightSlaveTwo.set(ControlMode.Follower, rightMain);
        this.leftSlaveOne.set(ControlMode.Follower, leftMain);
        this.leftSlaveTwo.set(ControlMode.Follower, leftMain);

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

        this.rightMain.config_kP(0, p, 20);
        this.rightMain.config_kI(0, i, 20);
        this.rightMain.config_kD(0, d, 20);
        this.rightMain.config_kF(0, f, 20);
        this.rightMain.config_IntegralZone(0, izone, 20);
        this.leftMain.config_kP(0, p, 20);
        this.leftMain.config_kI(0, i, 20);
        this.leftMain.config_kD(0, d, 20);
        this.leftMain.config_kF(0, f, 20);
        this.leftMain.config_IntegralZone(0, izone, 20);

        this.rightMain.selectProfileSlot(0,0);
        this.leftMain.selectProfileSlot(0,0);
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

    public void updatePIDValues(double p, double i, double d, double f, int izone) {
        this.p = p;
        this.i = i;
        this.d = d;
        this.f = f;
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

        System.out.println("P: " + p + " I: " + i + " D: " + d + " F: " + f + " izone: " + izone);
    }
}
