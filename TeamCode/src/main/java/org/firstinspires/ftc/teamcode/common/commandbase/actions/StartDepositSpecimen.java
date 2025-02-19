package org.firstinspires.ftc.teamcode.common.commandbase.actions;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.SetClawCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.ClawPID;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;

public class StartDepositSpecimen extends SequentialCommandGroup {
    public StartDepositSpecimen(Bot b){
        addCommands(
                new SetPivotAngleCommand(b.getPivot(), b.getClaw(), Pivot.setpoint_vertical),
                //new SetExtensionCommand(extension, claw, Extension.specimening),
                //new SetClawPIDCommand(claw, ClawPID.ServoPositions.specimening)
                new SetExtensionCommand(b.getExtension(), b.getClaw(), Extension.highBarTarget),
                new SetClawCommand(b.getClaw(), ClawPID.ServoPositions.placing)
        );
    }

}
