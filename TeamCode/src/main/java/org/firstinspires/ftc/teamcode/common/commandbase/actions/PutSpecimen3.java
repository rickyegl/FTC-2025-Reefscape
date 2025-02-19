package org.firstinspires.ftc.teamcode.common.commandbase.actions;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.SetClawCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeOutCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.ClawPID;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;

public class PutSpecimen3 extends SequentialCommandGroup {
    public PutSpecimen3(Bot b){
        //int clawPos = 66;
        //int extension = 1050;
        addCommands(
                new SetPivotAngleCommand(b.getPivot(),b.getClaw(),Pivot.setpoint_vertical),
                new SetExtensionCommand(b.getExtension(), b.getClaw(), Extension.intakeMaxExtension-500),
                new SetClawCommand(b.getClaw(), ClawPID.ServoPositions.placing),
                new SetExtensionCommand(b.getExtension(), b.getClaw(),Extension.intakeMaxExtension),
                new WaitCommand(2000),
                new IntakeOutCommand(b.getIntake()),
                new SetExtensionCommand(b.getExtension(), b.getClaw(), 0)
        );
    }

}

//new WaitUntilCommand(()->!b.opertator.getButton(GamepadKeys.Button.Y)),
//v2
//new SetClawPIDCommand(claw, ClawPID.ServoPositions.placing),
//new SetExtensionCommand(extension, claw, extension.getSetpointCM()-600),
//new WaitCommand(600),
//new IntakeOutCommand(intake)
//
// /*new SetPivotAngleCommand(b.getPivot(), b.getClaw(), Pivot.setpoint_vertical),
//                new SetExtensionCommand(b.getExtension(), b.getClaw(), extension),
//                new SetClawCommand(b.getClaw(), clawPos)*/