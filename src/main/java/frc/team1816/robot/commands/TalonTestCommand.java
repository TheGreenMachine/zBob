package frc.team1816.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;
import frc.team1816.robot.subsystems.Elevator;

public class TalonTestCommand extends Command {

    private Elevator elevator;
    private TalonSRX testTalon;
    private int talonid;
    private double power;

    public TalonTestCommand(int talonid, double power) {
        super("toggleslowmodecommand");
        elevator = Components.getInstance().elevator;
        this.talonid = talonid;
        this.power = power;
        requires(elevator);
    }

    protected void initialize() {
        testTalon = new TalonSRX(talonid);
    }

    protected void execute() {
        testTalon.set(ControlMode.PercentOutput, power);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    protected void end() {}

    protected void interrupted() { }
}
