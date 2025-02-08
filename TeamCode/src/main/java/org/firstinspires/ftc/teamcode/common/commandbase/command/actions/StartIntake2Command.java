package org.firstinspires.ftc.teamcode.common.commandbase.command.actions;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.SetClawPIDCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeCustomCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeInCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.ClawPID;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;

public class StartIntake2Command extends SequentialCommandGroup {
    public StartIntake2Command(Bot b){
        addCommands(
                new SetPivotAngleCommand(b.getPivot(), b.getClaw(), ,false),
                new SetClawPIDCommand(b.getClaw(), ),
                new IntakeInCommand(b.getIntake()),

                new WaitUntilCommand(()->!b.opertator.getButton(GamepadKeys.Button.Y)),
                new SetClawPIDCommand(b.getClaw(), ClawPID.ServoPositions.preintaking),
                new IntakeCustomCommand(b.getIntake(),0.2)
                ///new SetPivotAngleCommand(pivot, claw, Pivot.setpoint_horizontal,true)

        );
    }

}
