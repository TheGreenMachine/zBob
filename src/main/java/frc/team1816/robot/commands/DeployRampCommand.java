package frc.team1816.robot.commands;

        import edu.wpi.first.wpilibj.command.Command;
        import frc.team1816.robot.Components;
        import frc.team1816.robot.subsystems.Ramp;

public class DeployRampCommand extends Command {

    private Ramp ramp;

    public DeployRampCommand(){
        super("deployrampcommand");
        this.ramp = Components.getInstance().ramp;
        requires(ramp);
    }

    public void execute() {
        ramp.deployRamps();
        System.out.println("Deploying Ramps");
    }

    @Override
    protected boolean isFinished() {
        return true;
    }


}
