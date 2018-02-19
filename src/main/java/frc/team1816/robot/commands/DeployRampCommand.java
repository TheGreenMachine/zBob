package frc.team1816.robot.commands;

import com.edinarobotics.utils.gamepad.Gamepad;
import edu.wpi.first.wpilibj.command.Command;
import frc.team1816.robot.Components;
import frc.team1816.robot.subsystems.Drivetrain;
import frc.team1816.robot.subsystems.Ramp;

public class DeployRampCommand extends Command {

    private Ramp ramp;
    private Gamepad gamepad;
    private Drivetrain drivetrain;

    public DeployRampCommand(Gamepad gamepad){
      super("deployrampcommand");
      this.ramp = Components.getInstance().ramp;
      this.drivetrain = Components.getInstance().drivetrain;
      this.gamepad = gamepad;
      requires(ramp);
    }

    public void execute() {
            ramp.deployRamps();
    }
    @Override
    protected boolean isFinished() {
        return true;
    }


}
