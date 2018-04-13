package frc.team1816.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.subsystems.Collector;
import frc.team1816.robot.Components;

public class ResetClawEncoderCommand extends Command {
    private Collector collector;

    public ResetClawEncoderCommand (){
        super("ResetClawEncoderCommand");
        collector = Components.getInstance().collector;
    }
    @Override
    protected void execute() {
       collector.resetClawEnc();
       System.out.println("Collector up/down encoder manually reset");
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
