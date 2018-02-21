package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Ramp;

public class ResetRampsCommand extends Command {

    private Ramp ramp;

    public ResetRampsCommand(){
        super("resetrampscommand");
        this.ramp = Components.getInstance().ramp;
        requires(ramp);
    }

    public void execute() {
        ramp.resetRamps();
        System.out.println("Ramps Reset");
    }

    @Override
    protected boolean isFinished() {
        return true;
    }


}
