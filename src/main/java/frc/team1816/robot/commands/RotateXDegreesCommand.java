package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;

public class RotateXDegreesCommand extends Command {
    private Drivetrain drivetrain;
    private double degreesStarted;
    private double degreesToTurn;
    private boolean dimeTurn = false;
    private double target;

    private double velocity = 0.7;

    public RotateXDegreesCommand(double degreesToTurn ){
        super("rotatexdegreescommand");
        this.degreesToTurn = degreesToTurn;
        drivetrain = Components.getInstance().drivetrain;
    }

    public RotateXDegreesCommand(double degreesToTurn, boolean dimeTurn) {
        super("rotatexdegreescommand");
        this.degreesToTurn = degreesToTurn;
        this.dimeTurn = dimeTurn;
        drivetrain = Components.getInstance().drivetrain;
    }

    public RotateXDegreesCommand(double degreesToTurn, boolean dimeTurn, double speed) {
        super("rotatexdegrees");
        this.degreesToTurn = degreesToTurn;
        this.dimeTurn = dimeTurn;
        this.velocity = speed;
        drivetrain = Components.getInstance().drivetrain;
    }

    @Override
    protected void initialize() {
    	System.out.println("Init RotateX");
        degreesStarted = drivetrain.getGyroAngle();
        target = degreesStarted + degreesToTurn;

        System.out.println("degreesStarted = " + degreesStarted + " target = " + target);
    }

    @Override
    protected void execute() {
        System.out.println("Current Angle: " + drivetrain.getGyroAngle());

        if(target - drivetrain.getGyroAngle() > 0){
            if (dimeTurn)
                drivetrain.setDrivetrain(velocity, -velocity);
            else
                drivetrain.setDrivetrain(velocity, 0);
            System.out.println("Rotating Right at " + velocity + " velocity");
        } else {
            if (dimeTurn)
                drivetrain.setDrivetrain(-velocity, velocity);
            else
                drivetrain.setDrivetrain(0, velocity);
            System.out.println("Rotating Left at " + velocity + " velocity");
        }

        double currentDelta = Math.abs(target - drivetrain.getGyroAngle());
        if(currentDelta < 20 && velocity >= 0.3)
            velocity *= currentDelta / 30;

    }

    @Override
    protected boolean isFinished() {

        //Right turns are overshooting and becoming left turns, mess with end condition
        System.out.println("deltaAngle: " + (target - drivetrain.getGyroAngle()));

        if ((Math.abs(target - drivetrain.getGyroAngle()) <= 2)){
            System.out.println("RotateX Finishing");
            drivetrain.setDrivetrain(0, 0);
            drivetrain.setPrevTargetHeading(Double.toString(target));
            System.out.println("Final gyro angle: " + drivetrain.getGyroAngle());
            System.out.println("Final target angle: " + Double.toString(target));
            return true;
        } else{
            System.out.println("Current Angle: " + drivetrain.getGyroAngle() + ", Target: " + target);
            return false;
        }
    }

    @Override
    protected void end() {
        //End command by stopping robot
        System.out.println("RotateX Ended");
        drivetrain.setDrivetrain(0, 0);
        //System.out.println("Current Angle: "+drivetrain.getGyroAngle()+ "\n Target: " + target +"\n Initial Angle: "+ degreesStarted);
    }

    @Override
    protected void interrupted() {
        //End if command is interrupted
        System.out.println("RotateX Interrupted");
        end();
    }
}
