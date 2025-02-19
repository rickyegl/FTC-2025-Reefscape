package org.firstinspires.ftc.teamcode.common.commandbase.actions;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeOutCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeStopCommand;

public class PutSample extends SequentialCommandGroup {
    public PutSample(Bot b){
        addCommands(
                new IntakeOutCommand(b.getIntake()),
                new WaitUntilCommand(()->!b.opertator.getButton(GamepadKeys.Button.Y)),
                new IntakeStopCommand(b.getIntake())
        );
    }

}
