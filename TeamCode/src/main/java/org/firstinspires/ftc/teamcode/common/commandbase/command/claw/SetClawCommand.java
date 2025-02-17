package org.firstinspires.ftc.teamcode.common.commandbase.command.claw;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Claw;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Intake;


/**
 * ManualExtensionCommand is a command that allows the driver to manually control the extension
 * Positive speed extends the extension, negative speed retracts the extension
 */
public class SetClawCommand extends CommandBase {

    private final Claw claw;
    private final double position;

    public SetClawCommand(Claw claw, double position) {
        this.claw = claw;
        this.position = position;
    }

    @Override
    public void initialize() {
        claw.setPosition(position);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
