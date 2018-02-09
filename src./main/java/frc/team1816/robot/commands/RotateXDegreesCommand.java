package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;

public class RotateXDegreesCommand extends Command {
    private Drivetrain drivetrain;
    private double degreesStarted;
    private double degreesToTurn;
    private double target;

    public RotateXDegreesCommand(double degreesToTurn ){
        super("rotatexdegreescommand");
        this.degreesToTurn = degreesToTurn;
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
            //Target angle is between 0 and 180. Therefore, turn right
            drivetrain.setDrivetrain(.25, -0.25);
            System.out.println("Rotating Right");
        } else {
            //Target angle is between 180 and 360. Therefore, turn left
            drivetrain.setDrivetrain(-0.25, .25);
            System.out.println("Rotating Left");
        }

    }

    @Override
    protected boolean isFinished() {
        if (Math.abs(target-drivetrain.getGyroAngle())<=1){
            System.out.println("RotateX Finishing");
            drivetrain.setDrivetrain(0, 0);
            drivetrain.setPrevTargetHeading(Double.toString(target));
            System.out.println("Final gyro angle: " + drivetrain.getGyroAngle());
            System.out.println("Final target angle: " + Double.toString(target));
            return true;
        } else {
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
