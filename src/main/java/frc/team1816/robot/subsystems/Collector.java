package frc.team1816.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;


public class Collector extends Subsystem {
    private TalonSRX right;
    private TalonSRX left;
    private TalonSRX clawLift;

    private double outputCurrent, clawEncPos, clawVel;
    private static final double LOW_LIMIT = 1920;
    private static final double HIGH_LIMIT = 10;

    private static final double kP = 5.5;
    private static final double kI = 0;
    private static final double kD = 60;
    private static final double kF = 0;

    private static final int MAX_CLAW_VEL_TICKS_PER_100MS = 600;

    public Collector(int leftTalon, int rightTalon, int clawLift) {
        super();

        this.left = new TalonSRX(leftTalon);
        this.right = new TalonSRX(rightTalon);
        this.clawLift = new TalonSRX(clawLift);

        this.right.setInverted(true);

        this.left.setNeutralMode(NeutralMode.Brake);
        this.right.setNeutralMode(NeutralMode.Brake);
        this.clawLift.setNeutralMode(NeutralMode.Brake);

        this.left.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        this.right.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        this.clawLift.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,10);

        this.clawLift.configNominalOutputForward(0,10);
        this.clawLift.configNominalOutputReverse(0,10);
        this.clawLift.configPeakOutputForward(.6,10);
        this.clawLift.configPeakOutputReverse(-.6,10);

        this.clawLift.config_kP(0, kP, 20);
        this.clawLift.config_kI(0, kI,20);
        this.clawLift.config_kD(0, kD,20);
        this.clawLift.config_kF(0, kF, 20);
        this.clawLift.config_IntegralZone(0, 0, 20);
    }

    public void setCollectorSpeed(double lpower, double rpower) {
        left.set(ControlMode.PercentOutput, lpower);
        right.set(ControlMode.PercentOutput, rpower);
    }

    public void clawLiftUp() {
        if(getClawPosition() < HIGH_LIMIT) {
            setClawSpeed(0);
        } else {
            setClawSpeed(-0.6);
            System.out.println("Claw up");
        }
    }

    public void clawLiftDown() {
        if(getClawPosition() > LOW_LIMIT) {
            setClawSpeed(0);
        } else {
            setClawSpeed(0.6);
            System.out.println("Claw down");
        }
    }

    public void clawLiftStop() {
        setClawSpeed(0);
        System.out.println("Claw stopped");
    }

    public void resetClawEnc() {
        clawLift.getSensorCollection().setQuadraturePosition(0,10);
    }

    public double getClawPosition() {
        return clawEncPos;
    }

    public double getClawVel() {
        return clawVel;
    }

    public void setClawSpeed(double clawPow) {
        double clawVelocity = clawPow * MAX_CLAW_VEL_TICKS_PER_100MS;
        clawLift.set(ControlMode.Velocity, clawVelocity);

//        clawLift.set(ControlMode.PercentOutput, clawPow);
    }

    public void initDefaultCommand() {
    }
    public double getOutputCurrent(){
        return outputCurrent;
    }

    public void periodic(){
       outputCurrent =  clawLift.getOutputCurrent();
       clawEncPos = clawLift.getSelectedSensorPosition(0);
       clawVel = clawLift.getSelectedSensorVelocity(0);
    }

    public void initSendable(SendableBuilder builder){
        super.initSendable(builder);
        builder.addDoubleProperty("Output Current", this::getOutputCurrent, null);
        builder.addDoubleProperty("Claw Position", this::getClawPosition, null);
        builder.addDoubleProperty("Claw Velocity", this::getClawVel, null);
    }

}
