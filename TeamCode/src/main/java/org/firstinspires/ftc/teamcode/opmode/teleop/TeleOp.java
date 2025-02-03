package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
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

import org.firstinspires.ftc.teamcode.BotState;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.TeleOpDriveCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.ManualExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.ManualPivotCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.SetBotStateCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;
import org.firstinspires.ftc.teamcode.Direction;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "TeleOp")
public class TeleOp extends CommandOpMode {

    private Bot bot;
    private Pivot pivot;
    private Extension extension;
    private MecanumDrivetrain drivetrain;

    private boolean enableDrive = false;

    private GamepadEx driverGamepad;
    private GamepadEx operatorGamepad;

    private MultipleTelemetry telem;

    // Gamepad layout
    // https://www.padcrafter.com/?templates=Gamepad+1%2FDriver+Gamepad&plat=1&col=%23242424%2C%23606A6E%2C%23FFFFFF&rightStick=Yaw%2FRotation&leftStick=Translation&dpadUp=Wrist+Up&dpadRight=Wrist+Clockwise&dpadLeft=Wrist+Counter-Clockwise&dpadDown=Wrist+Down&aButton=Toggle+Claw&yButton=Sample%2FSpecimen+Auto+Deposit&xButton=Robot+State+Toggle&bButton=Sample%2FSpecimen+State+Toggle&rightTrigger=Extension+Out&leftTrigger=Extension+In&leftBumper=Pivot+Down&rightBumper=Pivot+Up&backButton=Red+%3D+Sample%2C+Blue+%3D+Specimen%2C+Green+%3D+Ascent&startButton=Options+%3D+Ascent    @Override
    public void initialize() {

        CommandScheduler.getInstance().reset();

        telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        driverGamepad = new GamepadEx(gamepad1);
        operatorGamepad = driverGamepad;
        //operatorGamepad = new GamepadEx(gamepad2);

        //gamepad1.setLedColor(255, 255, 0, Gamepad.LED_DURATION_CONTINUOUS);

        bot = new Bot(telem, hardwareMap, gamepad1, enableDrive);

        //region Drivetrain
        drivetrain = bot.getDrivetrain();

        TeleOpDriveCommand driveCommand = new TeleOpDriveCommand(
                drivetrain,
                () -> -driverGamepad.getRightX(),
                () -> driverGamepad.getLeftY(),
                () -> -driverGamepad.getLeftX(),
                () -> 1.0
        );


        //Button fieldCentricToggle = (new GamepadButton(driverGamepad, GamepadKeys.Button.BACK))
        //        .whenPressed(
        //                new ToggleFieldCentricCommand(bot.getDrivetrain())
        //        );



        //register(drivetrain);
        //drivetrain.setDefaultCommand(driveCommand);

        //region HLock
        new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_UP)
                .whileHeld(
                        new InstantCommand(()->{
                            drivetrain.setTargetHeadingDEG(0);
                            drivetrain.setHeadingLock(true);
                        })
                ).whenReleased(
                        new  InstantCommand(()->{
                            drivetrain.setHeadingLock(false);
                        })
                );

        new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_RIGHT)
                .whileHeld(
                        new InstantCommand(()->{
                            drivetrain.setTargetHeadingDEG(90);
                            drivetrain.setHeadingLock(true);
                        })
                ).whenReleased(
                        new  InstantCommand(()->{
                            drivetrain.setHeadingLock(false);
                        })
                );

        new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_DOWN)
                .whileHeld(
                        new InstantCommand(()->{
                            drivetrain.setTargetHeadingDEG(180);
                            drivetrain.setHeadingLock(true);
                        })
                ).whenReleased(
                        new  InstantCommand(()->{
                            drivetrain.setHeadingLock(false);
                        })
                );

        new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_LEFT)
                .whileHeld(
                        new InstantCommand(()->{
                            drivetrain.setTargetHeadingDEG(270);
                            drivetrain.setHeadingLock(true);
                        })
                ).whenReleased(
                        new  InstantCommand(()->{
                            drivetrain.setHeadingLock(false);
                        })
                );

        //endregion

        //region Pivot
        pivot = bot.getPivot();

        Button pivotDownButton = (new GamepadButton(operatorGamepad, GamepadKeys.Button.DPAD_LEFT))
                .whenPressed(
                        new ManualPivotCommand(pivot, Direction.DOWN)
                );
        Button pivotUpButton = (new GamepadButton(operatorGamepad, GamepadKeys.Button.DPAD_RIGHT))
                .whenPressed(
                        new ManualPivotCommand(pivot, Direction.UP)
                );

        register(pivot);
        //endregion

        //region Extension
        extension = bot.getExtension();

        ManualExtensionCommand extensionCommand = new ManualExtensionCommand(
                extension,
                () -> operatorGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER),
                () -> operatorGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)
        );

        //Button toggleElevator = (new GamepadButton(driverGamepad, GamepadKeys.Button.A))
        //        .whenPressed(
        //                new InstantCommand(()->{
        //                            if(pivot.setpointDEG==0){
        //                                pivot.setSetpointHorizontal();
        //                            }else {
        //                                pivot.setSetpointVertical();
        //                            }
        //                        }
        //                )
        //        );

        Button togglePivot = (new GamepadButton(operatorGamepad, GamepadKeys.Button.A))
                .whenPressed(
                        new InstantCommand(()->{
                            if(pivot.setpointDEG==Pivot.setpoint_vertical){
                                pivot.setSetpointHorizontal();
                            }else {
                                pivot.setSetpointVertical();
                            }
                        }
                        )
                );

        register(extension);
        extension.setDefaultCommand(extensionCommand);
        //endregion

        //region Actions

        new GamepadButton(operatorGamepad, GamepadKeys.Button.B)
                .whileActiveOnce(
                        new SequentialCommandGroup(
                                new SetPivotAngleCommand(pivot, Pivot.setpoint_horizontal),
                                new SetExtensionCommand(extension, Extension.intakeMaxExtension)
                        )
                );

        new GamepadButton(driverGamepad, GamepadKeys.Button.B)
                .whenReleased(
                    new SequentialCommandGroup(
                            new SetExtensionCommand(extension, 0)
                    )/*.interruptOn(() -> driverGamepad.getButton(GamepadKeys.Button.B))*/
                );



        new GamepadButton(driverGamepad, GamepadKeys.Button.Y)
                .whenPressed(
                        new ConditionalCommand(
                                //if intaking
                                new SequentialCommandGroup(
                                        new SetPivotAngleCommand(pivot, Pivot.setpoint_intaking),
                                        new WaitCommand(500),
                                        new SetPivotAngleCommand(pivot, Pivot.setpoint_horizontal)
                                ),
                                new ConditionalCommand(
                                        //if depositing
                                        new SequentialCommandGroup(
                                                new WaitCommand(0)
                                        ),
                                        //if not intaking or depositing
                                        new WaitCommand(0),
                                        () -> driverGamepad.getButton(GamepadKeys.Button.X)
                                ),
                                () -> driverGamepad.getButton(GamepadKeys.Button.B)
                        )

                );

        //deposit sample/specimen
        new GamepadButton(driverGamepad, GamepadKeys.Button.X)
                .whileActiveOnce(
                        new ConditionalCommand(
                                //Specimens
                                new SequentialCommandGroup(
                                        new SetPivotAngleCommand(pivot, Pivot.setpoint_vertical),
                                        new SetExtensionCommand(extension, extension.getChamberTarget())
                                ),
                                //Samples
                                new SequentialCommandGroup(
                                        new SetExtensionCommand(extension, extension.getSamplesTarget())
                                ),
                                () -> bot.getMode() == Bot.Modes.SPECIMENS
                        )

                );

        new GamepadButton(driverGamepad, GamepadKeys.Button.X)
                .whenReleased(
                        new SequentialCommandGroup(
                                new SetExtensionCommand(extension, 0)
                        )/*.interruptOn(() -> driverGamepad.getButton(GamepadKeys.Button.X))*/
                );


        //endregion

        //region Settings

        new GamepadButton(operatorGamepad, GamepadKeys.Button.BACK).whenPressed(
                new InstantCommand(()->{
                    bot.toggleMode();
                })
        );

        new GamepadButton(operatorGamepad, GamepadKeys.Button.DPAD_UP).whenPressed(
                new InstantCommand(()->{
                    bot.setLevel(Bot.Levels.Up);
                })
        );

        new GamepadButton(operatorGamepad, GamepadKeys.Button.DPAD_DOWN).whenPressed(
                new InstantCommand(()->{
                    bot.setLevel(Bot.Levels.Down);
                })
        );

        //endregion

        //region Automation


        schedule(
                new ParallelCommandGroup(
                        new SetBotStateCommand(bot, BotState.INTAKE),
                        new SetExtensionCommand(bot.getExtension(), 0),
                        new SetPivotAngleCommand(bot.getPivot(), 90)
                )
        );

        while (opModeInInit()){
            bot.telem.addData("Pivot Angle", pivot.getPositionDEG());
            //bot.telem.addData("Pivot AngleT", pivot.getPositionT());
            //bot.telem.addData("Pivot LPosition", pivot.pivotMotorL.getCurrentPosition());
            //bot.telem.addData("Pivot RPosition", pivot.pivotMotorR.getCurrentPosition());
            bot.telem.addData("ViperL", extension.bottomExtensionMotor.getCurrentPosition());
            bot.telem.addData("ViperR", extension.topExtensionMotor.getCurrentPosition());
            bot.telem.update();
        }

        //endregion

        //region Climb
        new GamepadButton(operatorGamepad, GamepadKeys.Button.LEFT_STICK_BUTTON).whileActiveOnce(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                new SetExtensionCommand(extension,1),
                                new SetPivotAngleCommand(pivot,1)
                        ),
                        new WaitUntilCommand(()->operatorGamepad.getButton(GamepadKeys.Button.RIGHT_STICK_BUTTON)),
                        new InstantCommand(()->{bot.climbing = true;}),
                        new SetPivotAngleCommand(pivot,2),
                        new SetExtensionCommand(extension,2),
                        new SetPivotAngleCommand(pivot,3)
                )
        );

        new GamepadButton(operatorGamepad, GamepadKeys.Button.LEFT_STICK_BUTTON).whenReleased(
                new ConditionalCommand(
                        new WaitCommand(0),
                        new ParallelCommandGroup(
                                new SetExtensionCommand(extension,1),
                                new SetPivotAngleCommand(pivot,1)
                        ),
                        () -> bot.climbing
                )
        );

        //endregion

    }

}