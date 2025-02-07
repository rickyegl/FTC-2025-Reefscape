package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.SetClawPIDCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.ClawPID;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

/**
 * SetExtensionCommand is a command that sets the extension to a specific setpoint
 * Setpoint is in centimeters and the extension is bound to extension limits
 */
public class SetExtensionCommand extends CommandBase {

    private final Extension extension;
    private final double setpoint;
    private final ClawPID claw;

    public SetExtensionCommand(Extension e, ClawPID claw, double setpointcm) {
        extension = e;
        this.setpoint = setpointcm;
        this.claw = claw;
        extension.setSetpointCM(setpoint);
        addRequirements(extension);
    }

    @Override
    public void initialize() {
        new SequentialCommandGroup(
                new SetClawPIDCommand(claw, ClawPID.ServoPositions.safeE),
                new InstantCommand(()->{
                    extension.setSetpointCM(setpoint);
                })
        ).schedule();
    }

    @Override
    public boolean isFinished() {
        return extension.inTolerance();
    }
}
