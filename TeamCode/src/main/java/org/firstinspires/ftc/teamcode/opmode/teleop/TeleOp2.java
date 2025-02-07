package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.ProfiledPIDCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.command.button.Button;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.matrices.GeneralMatrixF;
import org.firstinspires.ftc.teamcode.BotState;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.SetClawCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.TeleOpDriveCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.ManualExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeInCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeOutCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeStopCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.ManualPivotCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.SetBotStateCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Claw;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Intake;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;
import org.firstinspires.ftc.teamcode.Direction;
import org.firstinspires.ftc.teamcode.opmode.auto.GeneratedPath;

import java.awt.font.TextAttribute;
import java.io.SequenceInputStream;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp2", group = "TeleOp")
public class TeleOp2 extends CommandOpMode {

    private Bot bot;
    private Pivot pivot;
    private Extension extension;
    private Intake intake;
    private Claw claw;
    private MecanumDrivetrain drivetrain;
    private MultipleTelemetry tele;
    private boolean enableDrive = true;

    private GamepadEx driverG;
    private GamepadEx mechanismG;

    public void initialize(){

        //region Initialize
        CommandScheduler.getInstance().reset();

        tele = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        driverG = new GamepadEx(gamepad1);
        mechanismG = new GamepadEx(gamepad2);

        bot = new Bot(tele, hardwareMap, gamepad1, enableDrive);
        TeleOpDriveCommand driveCommand = new TeleOpDriveCommand(
                drivetrain,
                () -> driverG.getRightX(),
                () -> driverG.getLeftY(),
                () -> driverG.getLeftX(),
                () -> bot.speed
        );

        register(drivetrain);
        drivetrain.setDefaultCommand(driveCommand);

        claw = bot.getClaw();
        register(claw);

        //endregion

        //region Pivot
        pivot = bot.getPivot();

        Button movePivot = (new GamepadButton(mechanismG, GamepadKeys.Button.A))
                .whenPressed(
                        new InstantCommand(() ->{
                            if (pivot.setpointDEG == Pivot.setpoint_horizontal){
                                new SetPivotAngleCommand(pivot, claw, Pivot.setpoint_vertical);
                            } else {
                                new SetPivotAngleCommand(pivot, claw, Pivot.setpoint_horizontal);
                            }
                        })
                );

        register(pivot);

        //endregion

        //region Extension
        extension = bot.getExtension();

        ManualExtensionCommand extensionCommand = new ManualExtensionCommand(
                extension,
                () -> mechanismG.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER),
                () -> mechanismG.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)
        );

        Button lowCamber = (new GamepadButton(mechanismG, GamepadKeys.Button.DPAD_DOWN))
                .whenPressed(
                        new SequentialCommandGroup(
                                new SetPivotAngleCommand(pivot, claw, Pivot.setpoint_horizontal),
                                new SetExtensionCommand(extension, claw, Extension.lowBarTarget)
                        )
                )
                .whenReleased(
                        new SequentialCommandGroup(
                                new SetPivotAngleCommand(pivot, claw, Pivot.setpoint_horizontal),
                                new IntakeOutCommand(intake),
                                new SetExtensionCommand(extension, claw, Extension.minExtension)
                        )
                );

        Button highChamber = (new GamepadButton(mechanismG, GamepadKeys.Button.DPAD_RIGHT))
                .whenPressed(
                        new SequentialCommandGroup(
                                new SetExtensionCommand(extension, claw, Extension.minExtension),
                                new SetPivotAngleCommand(pivot, claw, Pivot.setpoint_vertical),
                                new SetExtensionCommand(extension, claw, Extension.highBarTarget)
                        )
                )
                .whenReleased(
                        new SequentialCommandGroup(
                                new IntakeOutCommand(intake),

                                new SetExtensionCommand(extension, claw, Extension.minExtension),
                                new SetPivotAngleCommand(pivot, claw, Pivot.setpoint_horizontal)
                        )
                );

        //endregion

    }


}