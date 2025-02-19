package org.firstinspires.ftc.teamcode.common.commandbase.actions;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.SetClawCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;

@Config
public class StartDepositSpecimen2v2 extends SequentialCommandGroup {
    public static int clawPos = 66;
    public static int extension = 800;
    public StartDepositSpecimen2v2(Bot b){

        addCommands(
                new SetPivotAngleCommand(b.getPivot(), b.getClaw(), Pivot.setpoint_vertical),
                new SetExtensionCommand(b.getExtension(), b.getClaw(), extension),
                new SetClawCommand(b.getClaw(), clawPos)
        );
    }

}
