package frc.team1816.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;
import frc.team1816.robot.subsystems.Elevator;

public class FullPowerDriveCommand extends Command {

    private TalonSRX testTalon, testTalon2;
    private int talonid1, talonid2;
    private double power = 1;

    public FullPowerDriveCommand(int talonid1, int talonid2) {
        super("talontestcommand");
        this.talonid1 = talonid1;
        this.talonid2 = talonid2;
    }

    protected void initialize() {
        testTalon = new TalonSRX(talonid1);
        testTalon2 = new TalonSRX(talonid2);
    }

    protected void execute() {
        testTalon.set(ControlMode.PercentOutput, power);
        testTalon2.set(ControlMode.PercentOutput, power);
        System.out.println("ID: " + talonid1 + "\tSet Pow: " + power + "\tOut Pow: " + testTalon.getSelectedSensorVelocity(0));
        System.out.println("ID: " + talonid2 + "\tSet Pow: " + power + "\tOut Pow: " + testTalon2.getSelectedSensorVelocity(0));

    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    protected void end() {
        testTalon.set(ControlMode.PercentOutput, 0);
        testTalon2.set(ControlMode.PercentOutput,0);
    }

    protected void interrupted() {
        end();
    }
}
