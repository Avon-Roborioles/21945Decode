package org.firstinspires.ftc.teamcode.OpModes.Auto.RedGoal;


import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Commands.Intake.AutoIntakeNoTime;
import org.firstinspires.ftc.teamcode.Commands.Automatic.RunTurretAndLauncherFromPoseAuto;
import org.firstinspires.ftc.teamcode.Commands.Drive.FollowPathNew;
import org.firstinspires.ftc.teamcode.Commands.Intake.AutoIntakeCheck;
import org.firstinspires.ftc.teamcode.Commands.Launch.ForceLaunchAuto;
import org.firstinspires.ftc.teamcode.OpModes.Auto.AutoBase;
import org.firstinspires.ftc.teamcode.Subsystems.SorterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.VisionSubsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;

@Autonomous (group = "Red Goal", preselectTeleOp = "RedTeleOp")
@Configurable
public class RedGoalRamp12Ball_Out extends AutoBase {

    /*TODO: Look into Piecewise heading
      TODO: Shoot While Move?
      TODO: Last Drive Cut Power and Coast to save Time
     */

    PathChain MidCycle, CloseCycle, FarCycle, RampCycle;
    Path ToScorePreloads, ToGrabMid, ToScoreMid, ToGrabClose, GrabClose, ToScoreClose, ToGrabFar, ToScoreFar, ToRampCycle, ToScoreRampCycle;

    public static Pose startingPos = new Pose(26.75, 130, Math.toRadians(329)).mirror();
    public static Pose scorePreload = new Pose(58.5, 108.5, Math.toRadians(270)).mirror();
    public static Pose grabMid = new Pose(31, 58, Math.toRadians(180)).mirror();
    public static Pose grabMidCP1 = new Pose(58.5, 88).mirror();
    public static Pose grabMidCP2  = new Pose(59, 66.5).mirror();
    public static Pose grabMidCP3  = new Pose(64, 60).mirror();
    public static Pose scoreMid = new Pose(56.5, 85, Math.toRadians(-110)).mirror();
    public static Pose scoreMidCp = new Pose(51, 59.5).mirror();

    public static Pose toGrabClose = new Pose(42, 85, Math.toRadians(180)).mirror();
    public static Pose grabClose = new Pose(30, 85, Math.toRadians(180)).mirror();
    public static Pose scoreClose = new Pose(60, 112, Math.toRadians(270)).mirror();
    public static Pose scoreCloseCP = new Pose(44.5, 82.5).mirror();



    public static Pose grabRampCycle = new Pose(22, 59, Math.toRadians(155)).mirror();
    public static Pose grabRampCycleCP1 = new Pose(58.5, 88).mirror();
    public static Pose grabRampCycleCP2  = new Pose(59, 66.5).mirror();
    public static Pose grabRampCycleCP3  = new Pose(64, 60).mirror();
    public static Pose scoreRampCycle = new Pose(56.5, 100, Math.toRadians(-110)).mirror();
    public static Pose scoreRampCycleCp = new Pose(51, 59.5).mirror();


    Command runAuto;


    public void buildPaths() {
        ToScorePreloads = new Path(new BezierLine(startingPos, scorePreload));
        ToScorePreloads.setLinearHeadingInterpolation(startingPos.getHeading(), scorePreload.getHeading());
        ToScorePreloads.setTimeoutConstraint(1000);

        ToGrabMid = new Path(new BezierCurve(scorePreload, grabMidCP1, grabMidCP2, grabMidCP3, grabMid));
//        ToGrabMid.setHeadingInterpolation(
//                HeadingInterpolator.piecewise(
//                        new HeadingInterpolator.PiecewiseNode(
//                                0,
//                                .9,
//                                HeadingInterpolator.linear(scorePreload.getHeading(), Math.toRadians(180))
//                        ),
//                        new HeadingInterpolator.PiecewiseNode(
//                                .9,
//                                1,
//                                HeadingInterpolator.tangent
//                        )
//                ));
        ToGrabMid.setLinearHeadingInterpolation(scorePreload.getHeading(), grabMid.getHeading());
        ToGrabMid.setTimeoutConstraint(4000);

        ToScoreMid = new Path(new BezierCurve(grabMid, scoreMidCp, scoreMid));
        ToScoreMid.setLinearHeadingInterpolation(grabMid.getHeading(), scoreMid.getHeading());
        ToScoreMid.setTimeoutConstraint(1000);

        MidCycle = new PathChain(ToGrabMid, ToScoreMid);




        ToGrabClose = new Path(new BezierLine(scoreRampCycle, toGrabClose));
        ToGrabClose.setLinearHeadingInterpolation(scoreMid.getHeading(), toGrabClose.getHeading());
        ToGrabClose.setTimeoutConstraint(1000);

        GrabClose = new Path(new BezierLine(toGrabClose, grabClose));
        GrabClose.setConstantHeadingInterpolation(grabClose.getHeading());
        GrabClose.setTimeoutConstraint(1000);

        ToScoreClose = new Path(new BezierCurve(grabClose, scoreCloseCP, scoreClose));
        ToScoreClose.setLinearHeadingInterpolation(grabClose.getHeading(), scoreClose.getHeading());
        ToScoreClose.setTimeoutConstraint(2000);

        CloseCycle = new PathChain(ToGrabClose, GrabClose, ToScoreClose);


        ToRampCycle = new Path(new BezierCurve(scoreMid, grabRampCycleCP1, grabRampCycleCP2, grabRampCycleCP3, grabRampCycle));
//        ToGrabMid.setHeadingInterpolation(
//                HeadingInterpolator.piecewise(
//                        new HeadingInterpolator.PiecewiseNode(
//                                0,
//                                .9,
//                                HeadingInterpolator.linear(scorePreload.getHeading(), Math.toRadians(180))
//                        ),
//                        new HeadingInterpolator.PiecewiseNode(
//                                .9,
//                                1,
//                                HeadingInterpolator.tangent
//                        )
//                ));
        ToRampCycle.setLinearHeadingInterpolation(scoreMid.getHeading(), grabRampCycle.getHeading());
        ToRampCycle.setTimeoutConstraint(4000);

        ToScoreRampCycle = new Path(new BezierCurve(grabRampCycle, scoreRampCycleCp, scoreRampCycle));
        ToScoreRampCycle.setLinearHeadingInterpolation(grabRampCycle.getHeading(), scoreRampCycle.getHeading());
        ToScoreRampCycle.setTimeoutConstraint(1000);

    }

    @Override
    public void onInit() {


    }
    @Override public void onWaitForStart () {

    }
    @Override public void onStartButtonPressed () {
        Command RunLaunchPre = new RunTurretAndLauncherFromPoseAuto(true, new Pose(scorePreload.getX(), scorePreload.getY(), scorePreload.getHeading()));
        Command RunLaunchMid = new RunTurretAndLauncherFromPoseAuto(true, scoreMid);
        Command RunLaunchClose = new RunTurretAndLauncherFromPoseAuto(true, scoreClose);
        Command RunLaunchRamp = new RunTurretAndLauncherFromPoseAuto(true, scoreRampCycle);


        Command Intake = new AutoIntakeNoTime();
        Command IntakeCheck = new AutoIntakeCheck();
        Command StopLauncher = new LambdaCommand().setStart(() -> {
            RunLaunchPre.cancel();
            RunLaunchMid.cancel();
            RunLaunchClose.cancel();
        });

        Command LaunchWOSort = new SequentialGroup(new ForceLaunchAuto(), StopLauncher);
        VisionSubsystem.INSTANCE.setLLToOB();

        PedroComponent.follower().setPose(startingPos);
        PedroComponent.follower().setMaxPower(1.0);
        PedroComponent.follower().update();

        buildPaths();

        runAuto = new SequentialGroup(
                new LambdaCommand().setStart(() -> {
                    SorterSubsystem.INSTANCE.sortHug();
                }).setIsDone(() -> {return true;}),
                new LambdaCommand().setStart(() -> {RunLaunchPre.schedule();}).setIsDone(() -> {return true;}),
                new Delay(0.125),
                new FollowPath(ToScorePreloads),
                new Delay(0.125),
                LaunchWOSort,
                new LambdaCommand().setStart(() -> {Intake.schedule();}).setIsDone(() -> {return true;}),
                new ParallelGroup(
                        new FollowPathNew(MidCycle),
                        new SequentialGroup(
                                new Delay(4),
                                new LambdaCommand().setStart(()->{RunLaunchMid.schedule();}).setIsDone(()->{ return true;}),
                                new InstantCommand(()->{ Intake.cancel();}),
                                IntakeCheck
                        )
                ),
                new Delay(0.5),
                LaunchWOSort,
                new LambdaCommand().setStart(() -> {Intake.schedule();}).setIsDone(() -> {return true;}),
                new ParallelGroup(
                        new SequentialGroup(
                                new FollowPath(ToRampCycle),
                                new LambdaCommand().setStart(()->{PedroComponent.follower().setMaxPower(1);}).setIsDone(()->{ return true;}),
                                new Delay(3),
                                new FollowPath(ToScoreRampCycle)
                        ),
                        new SequentialGroup(
                                new Delay(1.5),
                                new LambdaCommand().setStart(()->{PedroComponent.follower().setMaxPower(0.5);}).setIsDone(()->{ return true;}),
                                new Delay(4),
                                new LambdaCommand().setStart(()->{RunLaunchRamp.schedule();}).setIsDone(()->{ return true;}),
                                new InstantCommand(()->{ Intake.cancel();}),
                                new LambdaCommand().setStart(()->{PedroComponent.follower().setMaxPower(1);}).setIsDone(()->{ return true;}),
                                IntakeCheck
                        )

                ),
                new Delay(0.5),
                LaunchWOSort,
                new LambdaCommand().setStart(() -> {Intake.schedule();}).setIsDone(() -> {return true;}),
                new ParallelGroup(
                        new FollowPathNew(CloseCycle),
                        new SequentialGroup(
                                new Delay(3),
                                new LambdaCommand().setStart(()->{RunLaunchClose.schedule();}).setIsDone(()->{ return true;}),
                                new InstantCommand(()->{ Intake.cancel();}),
                                IntakeCheck
                        )
                ),
                new Delay(0.0001),
                LaunchWOSort,
                StopLauncher





        );

        runAuto.schedule();


    }

    @Override public void onStop () {}




}
