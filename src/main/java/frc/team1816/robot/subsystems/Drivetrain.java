package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.edinarobotics.utils.subsystems.Subsystem1816;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.PIDController;

public class Drivetrain extends Subsystem1816 {
    public static final double TICKS_PER_REV = 1024.6;
    public static final double TICKS_PER_INCH = 54.32;
    public static final double INCHES_PER_REV = TICKS_PER_REV/TICKS_PER_INCH;
    public static final double TICKS_PER_100MS = 853;
    //TODO constants need to be re-measured and updated for competition bot

    public static final double SLOW_MOD = 0.5;
    private boolean slowMode;

    private TalonSRX rightMain, rightSlaveOne, rightSlaveTwo, leftMain, leftSlaveOne, leftSlaveTwo;
    private double p = 0.2;
    private double i = 0;
    private double d = 0;
    private double f = 0.34;
    private int izone = 100;
    private double ramprate = 36;
    private int profile = 0;

    private double leftSpeed, rightSpeed;

    private double previousHeading;

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

        this.leftMain.setInverted(true);
        this.leftSlaveOne.setInverted(true);
        this.leftSlaveTwo.setInverted(true);

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

        this.rightMain.set(ControlMode.Velocity, rightMain);
        this.leftMain.set(ControlMode.Velocity, leftMain);

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

    public void setDrivetrain(double leftSpeed, double rightSpeed) {
        this.leftSpeed = leftSpeed; //* TICKS_PER_100MS;
        this.rightSpeed = rightSpeed; //* TICKS_PER_100MS;

        update();
    }

    public double talonPositionRight() {
        return rightMain.getSelectedSensorPosition(0);
    }

    public double talonPositionLeft() {
        return leftMain.getSelectedSensorPosition(0) * -1;
    }

    public void resetEncoders() {
        rightMain.getSensorCollection().setQuadraturePosition(0, 10); //grayhill encoder
        leftMain.getSensorCollection().setQuadraturePosition(0, 10); // grayhill encoder
        leftSlaveOne.getSensorCollection().setQuadraturePosition(0,10); //cimcoder
    }

    @Override
    public void update() {

        if(slowMode) {
            leftSpeed *= SLOW_MOD;
            rightSpeed *= SLOW_MOD;
        }

//        System.out.println("Slow mode = " + slowMode);
//        System.out.println("L Velocity: " + leftSpeed + "\tR Velocity: " + rightSpeed);

        //rightMain.set(ControlMode.Velocity, rightSpeed);
        //leftMain.set(ControlMode.Velocity, leftSpeed);
        rightMain.set(ControlMode.PercentOutput, rightSpeed);
        leftMain.set(ControlMode.PercentOutput, leftSpeed);

//        Print Encoder Values
//        System.out.println("Talon 1 Encoder: " + leftMain.getSelectedSensorPosition(0) + "\tTalon 2 Encoder: " + leftSlaveOne.getSelectedSensorPosition(0) +
//                "\tTalon 7 Encoder: " + rightMain.getSelectedSensorPosition(0));

    }

    public void setSlowMode(boolean slowModeToggle) {
        this.slowMode = slowModeToggle;
        update();
    }

    public void setPrevHeading(double heading) {
        this.previousHeading = heading;
    }
}
