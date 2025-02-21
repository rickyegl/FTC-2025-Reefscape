package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.SetClawCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.FollowPathCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeInCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeOutCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.IntakeStopCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.ClawServo;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Intake;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;


import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;


@Autonomous
public class SampleAuto extends LinearOpMode {

    //region Poses
    public static Pose startingPose = new Pose(10.2, 105, 0);

    public static Pose startingPoseIntermediate = new Pose(34, 105, 0);


    public static Pose score1 = new Pose( 16   , 127, Math.toRadians(315));

    public static Pose score2 = new Pose(20, 133.1034679809761, Math.toRadians(345.599123));

    public static Pose score3 = new Pose(20, 133.1034679809761, Math.toRadians(0));

    public static Pose score4 = new Pose(20, 133.1034679809761, Math.toRadians(13));
    //endregion

    //region Subsystems
    private Bot bot;
    private Pivot pivot;
    private Extension extension;
    private Intake intake;
    private ClawServo claw;
    //endregion


    @Override
    public void runOpMode() {

        //region Initialize
        Telemetry telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        GamepadEx driverGamepad = new GamepadEx(gamepad1);
        //operatorGamepad = driverGamepad;
        GamepadEx operatorGamepad = new GamepadEx(gamepad2);
        boolean enableDrive = false;

        VoltageSensor vs = hardwareMap.voltageSensor.iterator().next();

        Constants.setConstants(FConstants.class, LConstants.class);
        Follower f = new Follower(hardwareMap);

        f.setPose(startingPose);
        f.setMaxPower(0.75);

        bot = new Bot(telem, hardwareMap, driverGamepad, operatorGamepad, enableDrive);
        pivot = bot.getPivot();
        extension = bot.getExtension();
        claw = bot.getClaw();
        intake = bot.getIntake();

        //endregion

        SequentialCommandGroup auto = new SequentialCommandGroup(

                //region Intermediate
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(startingPose),
                                                new Point(startingPoseIntermediate)
                                        )
                                )
                                .setLinearHeadingInterpolation(startingPose.getHeading(), startingPoseIntermediate.getHeading())
                                .build()
                        ),
                        new ParallelCommandGroup(
                                new SetPivotAngleCommand(pivot, claw, Pivot.setpoint_horizontal),
                                new SetClawCommand(claw, ClawServo.ServoPositions.safe),
                                new SetExtensionCommand(extension, claw, Extension.minExtension)
                        )
                ),
                //endregion
                new WaitCommand(1000),

                //region Score 1
                new SequentialCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(startingPoseIntermediate),
                                                new Point(score1)
                                        )
                                )
                                .setLinearHeadingInterpolation(
                                        startingPoseIntermediate.getHeading(), score1.getHeading())
                                .build()
                        ),

                        new SequentialCommandGroup(
                                new SetPivotAngleCommand(pivot,claw, Pivot.setpoint_vertical),
                                new SetClawCommand(claw, ClawServo.ServoPositions.safe2),
                                new SetExtensionCommand(extension, claw, Extension.highBasketTarget),
                                new IntakeOutCommand(intake)
                        )

                ),
                //endregion

                new WaitCommand(1000),

                //region Pick 2
                new SequentialCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(score1),
                                                new Point(score2)
                                        )
                                )
                                .setLinearHeadingInterpolation(score1.getHeading(), score2.getHeading())
                                .build()
                        ),
                        new SequentialCommandGroup(
                                new SetExtensionCommand(extension, claw, 0),
                                new SetPivotAngleCommand(pivot, claw, Pivot.setpoint_intaking),
                                new SetClawCommand(claw, ClawServo.ServoPositions.safe),
                                new SetExtensionCommand(extension, claw, Extension.intakeMaxExtension),
                                new SetClawCommand(claw, ClawServo.ServoPositions.intaking),
                                new IntakeInCommand(intake)
                        )

                ),
                // endregion

                new WaitCommand(1000),

                //region Score 2
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(score2),
                                                new Point(score1)
                                        )
                                )
                                .setLinearHeadingInterpolation(
                                        score2.getHeading(), score1.getHeading())
                                .build()
                        ),
                        new SequentialCommandGroup(
                                new SetClawCommand(claw, ClawServo.ServoPositions.safe),
                                new SetExtensionCommand(extension, claw, Extension.minExtension),
                                new SetPivotAngleCommand(pivot,claw, Pivot.setpoint_vertical),
                                new SetClawCommand(claw, ClawServo.ServoPositions.safe2),
                                new SetExtensionCommand(extension, claw, Extension.highBasketTarget),
                                new IntakeOutCommand(intake)
                        )

                ),
                //endregion

                new WaitCommand(1000),

                //region Pick 3
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(score1),
                                                new Point(score3)
                                        )
                                )
                                .setLinearHeadingInterpolation(
                                        score1.getHeading(), score3.getHeading())
                                .build()
                        ),
                        new SequentialCommandGroup(
                                new SetExtensionCommand(extension, claw, 0),

                                new SetPivotAngleCommand(pivot, claw, Pivot.setpoint_intaking),
                                new SetClawCommand(claw, ClawServo.ServoPositions.safe),

                                new SetExtensionCommand(extension, claw, Extension.intakeMaxExtension),

                                new SetClawCommand(claw, ClawServo.ServoPositions.intaking),
                                new IntakeInCommand(intake)
                        )
                ),
                //endregion

                new WaitCommand(1000),

                //region Score 3
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(score3),
                                                new Point(score1)
                                        )
                                )
                                .setLinearHeadingInterpolation(
                                        score3.getHeading(), score1.getHeading())
                                .build()
                        ),
                        new SequentialCommandGroup(
                                new SetClawCommand(claw, ClawServo.ServoPositions.safe),
                                new SetExtensionCommand(extension, claw, Extension.minExtension),

                                new SetPivotAngleCommand(pivot,claw, Pivot.setpoint_vertical),
                                new SetClawCommand(claw, ClawServo.ServoPositions.safe2),

                                new SetExtensionCommand(extension, claw, Extension.highBasketTarget),
                                new IntakeOutCommand(intake)
                        )
                ),
                //endregion

                new WaitCommand(1000),

                //region Pick 4
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(score1),
                                                new Point(score4)
                                        )
                                )
                                .setLinearHeadingInterpolation(
                                        score1.getHeading(), score4.getHeading())
                                .build()
                        ),
                        new SequentialCommandGroup(
                                new SetExtensionCommand(extension, claw, 0),

                                new SetPivotAngleCommand(pivot, claw, Pivot.setpoint_intaking),
                                new SetClawCommand(claw, ClawServo.ServoPositions.safe),

                                new SetExtensionCommand(extension, claw, Extension.intakeMaxExtension),

                                new SetClawCommand(claw, ClawServo.ServoPositions.intaking),
                                new IntakeInCommand(intake)
                        )
                ),
                //endregion

                new WaitCommand(1000),

                //region Score 4
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(score4),
                                                new Point(score1)
                                        )
                                )
                                .setLinearHeadingInterpolation(
                                        score4.getHeading(), score1.getHeading())
                                .build()
                        ),
                        new SequentialCommandGroup(
                                new SetClawCommand(claw, ClawServo.ServoPositions.safe),
                                new SetExtensionCommand(extension, claw, Extension.minExtension),

                                new SetPivotAngleCommand(pivot,claw, Pivot.setpoint_vertical),
                                new SetClawCommand(claw, ClawServo.ServoPositions.safe2),

                                new SetExtensionCommand(extension, claw, 1000),
                                new IntakeOutCommand(intake)
                        )
                ),
                //endregion

                new WaitCommand(1000),

                //region Park
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(score1),
                                                new Point(score3)
                                        )
                                )
                                .setLinearHeadingInterpolation(
                                        score1.getHeading(), score3.getHeading())
                                .build()
                        ),
                        new ParallelCommandGroup(
                                new SetClawCommand(claw, ClawServo.ServoPositions.safe),
                                new SetExtensionCommand(extension, claw,Extension.minExtension),
                                new SetPivotAngleCommand(pivot, claw, Pivot.setpoint_horizontal),
                                new IntakeStopCommand(intake)
                        )
                )
                //endregion
        );


        waitForStart();
        CommandScheduler.getInstance().schedule(auto);

        // Opmode loop
        while (opModeIsActive()) {
            f.setMaxPower(10.0 / vs.getVoltage());
            CommandScheduler.getInstance().run();
            f.update();
            f.telemetryDebug(telem);
        }
    }
}