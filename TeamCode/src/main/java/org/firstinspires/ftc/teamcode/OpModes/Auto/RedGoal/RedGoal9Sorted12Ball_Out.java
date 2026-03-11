package org.firstinspires.ftc.teamcode.OpModes.Auto.RedGoal;


import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Commands.Intake.AutoIntakeNoTime;
import org.firstinspires.ftc.teamcode.Commands.Automatic.RunTurretAndLauncherFromPoseAuto;
import org.firstinspires.ftc.teamcode.Commands.Drive.FollowPathNew;
import org.firstinspires.ftc.teamcode.Commands.Intake.AutoIntakeCheck;
import org.firstinspires.ftc.teamcode.Commands.Launch.ForceLaunchAuto;
import org.firstinspires.ftc.teamcode.Commands.Launch.LaunchWithSortAuto;
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
public class RedGoal9Sorted12Ball_Out extends AutoBase {

    /*TODO: Look into Piecewise heading
      TODO: Shoot While Move?
      TODO: Last Drive Cut Power and Coast to save Time
     */

    PathChain MidCycle, CloseCycle, FarCycle;
    Path ToScorePreloads, ToGrabMid, ToScoreMid, ToGrabClose, GrabClose, ToScoreClose, ToGrabFar, ToScoreFar;

    public static Pose startingPos = new Pose(26.75, 130, Math.toRadians(329)).mirror();
    public static Pose scorePreload = new Pose(58.5, 108.5, Math.toRadians(270)).mirror();
    public static Pose grabMid = new Pose(23, 65, Math.toRadians(185)).mirror();
    public static Pose grabMidCP1 = new Pose(58.5, 88).mirror();
    public static Pose grabMidCP2  = new Pose(59, 66.5).mirror();
    public static Pose grabMidCP3  = new Pose(64, 60).mirror();
    public static Pose scoreMid = new Pose(56.5, 100, Math.toRadians(-110)).mirror();
    public static Pose scoreMidCp = new Pose(51, 59.5).mirror();

    public static Pose toGrabClose = new Pose(42, 85, Math.toRadians(180)).mirror();
    public static Pose grabClose = new Pose(30, 85, Math.toRadians(180)).mirror();
    public static Pose scoreClose = new Pose(60, 89, Math.toRadians(270)).mirror();
    public static Pose scoreCloseCP = new Pose(44.5, 82.5).mirror();
    public static Pose grabFar = new Pose(30, 34.5, Math.toRadians(182)).mirror();
    public static Pose grabFarCP1 = new Pose(61, 38.5).mirror();
    public static Pose grabFarCP2 = new Pose(57, 37.5).mirror();
    public static Pose grabFarCP3 = new Pose(45, 30).mirror();
    public static Pose scoreFar = new Pose(59, 116, Math.toRadians(270)).mirror();
    public static Pose scoreFarCP = new Pose(57, 36).mirror();



    Command runAuto;


    public void buildPaths() {
        ToScorePreloads = new Path(new BezierLine(startingPos, scorePreload));
        ToScorePreloads.setLinearHeadingInterpolation(startingPos.getHeading(), scorePreload.getHeading());
        ToScorePreloads.setTimeoutConstraint(1000);

        ToGrabMid = new Path(new BezierCurve(scorePreload, grabMidCP1, grabMidCP2, grabMidCP3, grabMid));
        ToGrabMid.setLinearHeadingInterpolation(scorePreload.getHeading(), grabMid.getHeading());
        ToGrabMid.setTimeoutConstraint(6000);

        ToScoreMid = new Path(new BezierCurve(grabMid, scoreMidCp, scoreMid));
        ToScoreMid.setLinearHeadingInterpolation(grabMid.getHeading(), scoreMid.getHeading());
        ToScoreMid.setTimeoutConstraint(1000);

        MidCycle = new PathChain(ToGrabMid, ToScoreMid);




        ToGrabClose = new Path(new BezierLine(scoreMid, toGrabClose));
        ToGrabClose.setLinearHeadingInterpolation(scoreMid.getHeading(), toGrabClose.getHeading());
        ToGrabClose.setTimeoutConstraint(1000);

        GrabClose = new Path(new BezierLine(toGrabClose, grabClose));
        GrabClose.setConstantHeadingInterpolation(grabClose.getHeading());
        GrabClose.setTimeoutConstraint(1000);

        ToScoreClose = new Path(new BezierCurve(grabClose, scoreCloseCP, scoreClose));
        ToScoreClose.setLinearHeadingInterpolation(grabClose.getHeading(), scoreClose.getHeading());
        ToScoreClose.setTimeoutConstraint(1000);

        CloseCycle = new PathChain(ToGrabClose, GrabClose, ToScoreClose);


        ToGrabFar = new Path(new BezierCurve(scoreClose, grabFarCP1, grabFarCP2, grabFarCP3, grabFar));
        ToGrabFar.setHeadingInterpolation(
                HeadingInterpolator.piecewise(
                        new HeadingInterpolator.PiecewiseNode(
                                0,
                                .3,
                                HeadingInterpolator.linear(scorePreload.getHeading(), Math.toRadians(0))
                        ),
                        new HeadingInterpolator.PiecewiseNode(
                                .3,
                                1,
                                HeadingInterpolator.constant(Math.toRadians(0))
                        )
                ));
        ToGrabFar.setTimeoutConstraint(1000);

        ToScoreFar = new Path(new BezierCurve(grabFar, scoreFarCP, scoreFar));
        ToScoreFar.setLinearHeadingInterpolation(grabFar.getHeading(), scoreFar.getHeading());
        ToScoreFar.setTimeoutConstraint(1000);
        FarCycle = new PathChain(ToGrabFar, ToScoreFar);

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
        Command RunLaunchFar = new RunTurretAndLauncherFromPoseAuto(true, scoreFar);


        Command Intake = new AutoIntakeNoTime();
        Command IntakeCheck = new AutoIntakeCheck();
        Command StopLauncher = new LambdaCommand().setStart(() -> {
            RunLaunchPre.cancel();
            RunLaunchMid.cancel();
            RunLaunchClose.cancel();
            RunLaunchFar.cancel();
        });

        Command LaunchWOSort = new SequentialGroup(new ForceLaunchAuto(), StopLauncher);
        Command LaunchWithSort = new SequentialGroup(new LaunchWithSortAuto(), StopLauncher);
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
                new LambdaCommand().setStart(() -> {
                    VisionSubsystem.INSTANCE.setLLToOB();}).setUpdate(() -> {
                    VisionSubsystem.INSTANCE.SearchForOb();}).setIsDone(() -> true).setStop((Interrupted)-> {
                    VisionSubsystem.INSTANCE.stopLL();}),
                new Delay(0.0001),
                LaunchWOSort,
                new LambdaCommand().setStart(() -> {Intake.schedule();}).setIsDone(() -> {return true;}),

                new ParallelGroup(
                    new SequentialGroup(
                            new FollowPathNew(ToGrabMid),
                            new Delay(0.5),
                            new FollowPath(ToScoreMid)
                    ),
                    new SequentialGroup(
                            new Delay(0.5),
                            new LambdaCommand().setStart(()->{PedroComponent.follower().setMaxPower(0.8);}).setIsDone(()->{ return true;}),
                            new Delay(3.5),
                            new LambdaCommand().setStart(()->{PedroComponent.follower().setMaxPower(1);}).setIsDone(()->{ return true;}),
                            new LambdaCommand().setStart(()->{RunLaunchMid.schedule();}).setIsDone(()->{ return true;}),
                            new InstantCommand(()->{ Intake.cancel();}),
                            IntakeCheck
                    )
                ),
                new Delay(0.5),
                LaunchWithSort,
                new LambdaCommand().setStart(() -> {Intake.schedule();}).setIsDone(() -> {return true;}),
                new ParallelGroup(
                        new FollowPathNew(CloseCycle),
                        new SequentialGroup(
                                new Delay(3.5),
                                new LambdaCommand().setStart(()->{RunLaunchClose.schedule();}).setIsDone(()->{ return true;}),
                                new InstantCommand(()->{ Intake.cancel();}),
                                IntakeCheck
                        )
                ),
                new Delay(0.0001),
                LaunchWithSort,
                new LambdaCommand().setStart(() -> {Intake.schedule();}).setIsDone(() -> {return true;}),
                new ParallelGroup(
                        new FollowPathNew(FarCycle),
                        new SequentialGroup(
                                new Delay(4.5),
                                new LambdaCommand().setStart(()->{RunLaunchFar.schedule();}).setIsDone(()->{ return true;}),
                                new InstantCommand(()->{ Intake.cancel();}),
                                IntakeCheck
                        )
                ),
                new Delay(0.25),
                LaunchWithSort,
                new Delay(0.0001),
                StopLauncher





        );

        runAuto.schedule();


    }

    @Override public void onStop () {}




}
