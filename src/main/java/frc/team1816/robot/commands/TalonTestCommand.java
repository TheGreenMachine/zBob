package frc.team1816.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;
import frc.team1816.robot.subsystems.Elevator;

public class TalonTestCommand extends Command {

    private TalonSRX testTalon, testTalon2;
    private int talonid1, talonid2;
    private double power;
    private boolean secondTalon;

    public TalonTestCommand(int talonid1, int talonid2, double power) {
        super("talontestcommand");
        this.talonid1 = talonid1;
        this.talonid2 = talonid2;
        secondTalon = true;
        this.power = power;
    }

    public TalonTestCommand(int talonid1, double power) {
        super("talontestcommand");
        this.talonid1 = talonid1;
        secondTalon = false;
        this.power = power;
    }

    protected void initialize() {
        testTalon = new TalonSRX(talonid1);
        if(secondTalon) {
            testTalon2 = new TalonSRX(talonid2);
        }
    }

    protected void execute() {
        testTalon.set(ControlMode.PercentOutput, power);
        System.out.println("ID: " + talonid1 + "\tSet Pow: " + power + "\tOut Pow: " + testTalon.getSelectedSensorVelocity(0));

        if(secondTalon) {
            testTalon2.set(ControlMode.PercentOutput, power);
            System.out.println("ID: " + talonid2 + "\tSet Pow: " + power + "\tOut Pow: " + testTalon2.getSelectedSensorVelocity(0));
        }
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    protected void end() {}

    protected void interrupted() { }
}
