package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.Robot;
import frc.team1816.robot.subsystems.Drivetrain;

public class DriveAndRotateCommand extends Command {
	
    private Drivetrain drivetrain;
    private AnalogInput analogInput;
    private double initPositionLeft;
    private double initPositionRight;
    private double inches;
    private double speed;
    private double ticks;
    private double rightTarget;
    private double leftTarget;
    private double remainingInchesRight;
    private double remainingInchesLeft;
    private double heading;
    private boolean leftTurn = false;
    private boolean tripped = false;
    private static final double ROTATION_OFFSET_P = 0.03;
    
    class Velocity { double left, right; };

    public DriveAndRotateCommand(double inches, double speed, double heading) {
        super("drivexinchescommand");
        this.inches = inches;
        this.speed = speed;
        ticks = (int) (inches * Drivetrain.TICKS_PER_INCH);
        this.heading = heading;
        //drivetrain.getRightMain().setSelectedSensorPosition(0,0,10);
        //drivetrain.getLeftMain().setSelectedSensorPosition(0,0,10);
    }

    @Override
    protected void initialize() {
        System.out.println("DriveX Init");
        drivetrain = Components.getInstance().drivetrain;
//        analogInput = Components.getInstance().ai;
//        analogInput.setOversampleBits(4);
//        analogInput.setAverageBits(2);
//        AnalogInput.setGlobalSampleRate(62500);

        
        initPositionLeft = drivetrain.talonPositionLeft();
        initPositionRight = drivetrain.talonPositionRight();

        initCalulations();
    }

    void initCalulations() {
        if (Math.signum(heading) == 1) {
            rightTarget = (heading / 360) * 2 * Math.PI * (inches - Drivetrain.WHEEL_THICKNESS / 2);
            leftTarget = (heading / 360) * 2 * Math.PI * (inches + Drivetrain.WHEEL_THICKNESS / 2);
        } else {
            heading *= -1;
            leftTurn = true;

            rightTarget = (heading / 360) * 2 * Math.PI * (inches + Drivetrain.WHEEL_THICKNESS / 2);
            leftTarget = (heading / 360) * 2 * Math.PI * (inches - Drivetrain.WHEEL_THICKNESS / 2);
        }
    }
    
    @Override
    protected void execute() {
    	double gyroAngle = drivetrain.getGyroAngle();
    	double leftPos = drivetrain.talonPositionLeft();
    	double rightPos = drivetrain.talonPositionRight();
    	
    	Velocity velocity = calculate(gyroAngle, leftPos, rightPos);
    	
        drivetrain.setDrivetrain(velocity.left, velocity.right);

        System.out.println("Remaining Inches Left: " + remainingInchesLeft);
        System.out.println("Remaining Inches Right: " + remainingInchesRight);

        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());
        sb.append(",");
        sb.append(drivetrain.talonPositionLeft());
        sb.append(",");
        sb.append(velocity.left);
        sb.append(",");
        sb.append(velocity.right);

        Robot.logger.log(sb.toString());

    }
    
    public Velocity calculate(double gyroAngle, double leftPos, double rightPos) {
        double deltaAngle = gyroAngle - heading;
        double leftVelocity;
        double rightVelocity;
        double currentPositionLeft = leftPos - initPositionLeft;
        double currentPositionRight = rightPos - initPositionRight;
        double currentInchesLeft = currentPositionLeft / Drivetrain.TICKS_PER_INCH;
        double currentInchesRight = currentPositionRight / Drivetrain.TICKS_PER_INCH;

        remainingInchesLeft = inches - Math.abs(currentInchesLeft);
        remainingInchesRight = inches - Math.abs(currentInchesRight);

        if (leftTurn) {
            if (remainingInchesLeft <= 0) {
                leftVelocity = 0;
//                System.out.println("STOPPED LEFT");
            } else {
                leftVelocity = (speed * (inches + Drivetrain.WHEEL_THICKNESS / 2)) / 100;
//                System.out.println("Left Velocity: " + leftVelocity);
            }

            rightVelocity = (speed * (inches - Drivetrain.WHEEL_THICKNESS / 2)) / 100;
//            System.out.println("Right Velocity: " + rightVelocity);
        } else {
            if (remainingInchesRight <= 0) {
                rightVelocity = 0;
//                System.out.println("STOPPED RIGHT");
            } else {
                rightVelocity = (speed * (inches + Drivetrain.WHEEL_THICKNESS / 2)) / 100;
//                System.out.println("Right Velocity: " + rightVelocity);
            }
            leftVelocity = (speed * (inches - Drivetrain.WHEEL_THICKNESS / 2)) / 100;
//            System.out.println("Left Velocity: " + leftVelocity);
        }


//        if (remainingInches < 6) {
//            if (speed > 0) {
//                if ((leftVelocity * (remainingInches / 6)) > .15) {
//
//                    leftVelocity = leftVelocity * (remainingInches / 6);
//                } else {
//                    leftVelocity = .15;
//                }
//            } else {
//                if ((leftVelocity * (remainingInches / 6)) < -.15) {
//
//                    leftVelocity = leftVelocity * (remainingInches / 6);
//                } else {
//                    leftVelocity = -.15;
//                }
//            }
//
//            rightVelocity = leftVelocity;
//            Robot.logger.log("Ticks:" + "," + drivetrain.talonPositionRight());
//            drivetrain.setDrivetrain(leftVelocity, rightVelocity);
//
//        } else if (deltaAngle < 0) {
//            System.out.println("DriveX Correcting Right\t delta angle: " + deltaAngle);
//            rightVelocity = rightVelocity - Math.abs(deltaAngle * ROTATION_OFFSET_P);
//            drivetrain.setDrivetrain(leftVelocity, rightVelocity);
//            System.out.println("L Velocity: " + leftVelocity + " R Velocity: " + rightVelocity);
//            System.out.println("---");
//        } else if (deltaAngle > 0) {
//            System.out.println("DriveX Correcting Left\t delta angle: " + deltaAngle);
//            drivetrain.setDrivetrain(leftVelocity, rightVelocity);
//            leftVelocity = leftVelocity - Math.abs(deltaAngle * ROTATION_OFFSET_P);
//            System.out.println("L Velocity: " + leftVelocity + " R Velocity: " + rightVelocity);
//            System.out.println("---");
//        } else {
//            System.out.println("DriveX Straight\t delta angle: " + deltaAngle);
//            drivetrain.setDrivetrain(leftVelocity, rightVelocity);
//            System.out.println("R + L Velocity: " + leftVelocity);
//            System.out.println("---");
//        }

        Velocity velocity = new Velocity();
        velocity.left = leftVelocity;
        velocity.right = rightVelocity;
        return velocity;
    }

    @Override
    protected void end() {
        drivetrain.setDrivetrain(0, 0);
        drivetrain.resetEncoders();
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected boolean isFinished() {
        if (remainingInchesLeft <= 0 && remainingInchesRight <= 0) {
            System.out.println("DriveX Finished");
            return true;
        } else {
            return false;
        }
    }

    
    // ==== TEST CODE ===========================
    public static void main(String args[]) {
    	/*
    	 * Radius=12, heading=90, distance = 12 * 2 * PI * (90 / 360) = 18.85 inches (center)
    	 *   right radius = 0.0
    	 *   left radius = (12 + 12) * 2 * PI * (90 / 360) = 37.7 inches
    	 *   
    	 * V_center = dist_center / t
    	 *   so  t = dist_center / V_center
    	 *   and t = dist_left / V_left
    	 *   so  dist_center / V_center = dist_left / V_left
    	 *   so  V_left = dist_left * V_center / dist_center
    	 *              = 37.7 * 1.0 / 18.85
    	 *              = 2.0
    	 *   and V_right = 0
    	 */
    	for (double radius = 12.0; radius <=  25.0; radius += 10.0) {
	    	for (double heading = 45.0; heading <= 90.1; heading += 45.0) {
	    		test(radius, heading, 1.0);
	    	}
    	}
    	
    	// should have left.vel a little less than 10.0, and right.vel a little bigger than 10.0
    	test(20.0, -90.0, 10.0);
    	
    	testIsFinished();
    }
    
    public static void test(double radius, double heading, double speed) {
    	System.out.printf("==== Radius=%3.0f   Heading=%3.0f   Speed=%3.0f\n", radius, heading, speed);
    	DriveAndRotateCommand cmd = new DriveAndRotateCommand(radius, speed, heading);
    	cmd.initPositionLeft = 0.0;
    	cmd.initPositionRight = 0.0;
    	cmd.initCalulations();
    	System.out.printf("Radius=%4.1f, Heading=%4.1f,  rightTarget=%6.3f, leftTarget=%6.3f, leftTurn=%b\n", radius, cmd.heading, cmd.rightTarget, cmd.leftTarget, cmd.leftTurn);
    	
    	double leftPos = 0.0;
    	double rightPos = 0.0;
    	double gyroAngle = 0.0;
    	Velocity vel = cmd.calculate(gyroAngle, leftPos, rightPos);
    	System.out.printf("  vel.left=%6.3f, vel.right=%6.3f\n", vel.left, vel.right);
    	System.out.println();
    }
    
    public static void testIsFinished() {
    	double radius = 12.0;
    	double speed = 1.0;
    	double heading = 90.0;
    	DriveAndRotateCommand cmd = new DriveAndRotateCommand(radius, speed, heading);
    	cmd.initPositionLeft = 0.0;
    	cmd.initPositionRight = 0.0;
    	cmd.initCalulations();
    	
    	for (double angle = 0.0; angle <= 111.1; angle += 20.0) {
    		double leftDist = (radius + 12.0) * 2 * Math.PI * (angle / 360.0);
    		double rightDist = (radius - 12.0) * 2 * Math.PI * (angle / 360.0);
    		
    		Velocity vel = cmd.calculate(angle, leftDist, rightDist);
    		boolean done = cmd.isFinished();
    		System.out.printf("angle=%3.0f   leftDist=%6.3f  rightDist=%6.3f vel(%6.3f, %6.3f)   done=%b\n", angle, leftDist, rightDist, vel.left, vel.right, done);
    	}
    }
}
