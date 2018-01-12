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
    private static final int TICKS_PER_REV = 1;
    private static final int TICKS_PER_INCH = 1;
    private static final int INCHES_PER_REV = TICKS_PER_REV/TICKS_PER_INCH;

    private TalonSRX rightMain, rightSlaveOne, rightSlaveTwo, leftMain, leftSlaveOne, leftSlaveTwo;
    private double p = 0.2;
    private double i = 0;
    private double d = 0;
    private double f = 0;
    private int izone = 100;
    private double ramprate = 36;
    private int profile = 0;

    private double leftPower, rightPower;

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
        this.leftSlaveOne.set(ControlMode.Follower, leftMain);

        this.rightMain.set(ControlMode.Velocity, rightMain);
        this.leftMain.set(ControlMode.Velocity, leftMain);

        //TODO set Feedback Device right
        //TODO set Feedback Device left

        //TODO set PID right
        //TODO set PID left
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

    public void setDrivetrain(double leftPower, double rightPower) {
        this.leftPower = leftPower;
        this.rightPower = rightPower;

        update();
    }

//    public double talonPositionRight() {
//        return rightMain.getSelectedSensorPosition();
//    }
//
//    public double talonPositionLeft() {
//        return leftMain.getSelectedSensorPosition() * -1;
//    }

    @Override
    public void update() {

    }
}
