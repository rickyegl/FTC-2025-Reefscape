package org.firstinspires.ftc.teamcode.common.commandbase.actions;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.SetClawCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeInCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;

public class GrabSpecimen extends SequentialCommandGroup {
    public GrabSpecimen(Bot b){
        addCommands(
                new SetPivotAngleCommand(b.getPivot(),b.getClaw(),93),
                new SetClawCommand(b.getClaw(),95),
                new IntakeInCommand(b.getIntake())
        );
    }

}
