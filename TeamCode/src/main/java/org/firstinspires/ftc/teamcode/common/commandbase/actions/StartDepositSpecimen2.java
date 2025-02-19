package org.firstinspires.ftc.teamcode.common.commandbase.actions;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.SetClawCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;

public class StartDepositSpecimen2 extends SequentialCommandGroup {
    public StartDepositSpecimen2(Bot b){
        addCommands(
                new SetPivotAngleCommand(b.getPivot(), b.getClaw(), Pivot.setpoint_vertical),
                new SetExtensionCommand(b.getExtension(), b.getClaw(), 342),
                new SetClawCommand(b.getClaw(), 80)
                //114

        );
    }

}
