package org.firstinspires.ftc.teamcode.OpModes.Auto;


import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Commands.AutoIntakeNoTime;
import org.firstinspires.ftc.teamcode.Commands.Automatic.RunTurretAndLauncherFromHeading;
import org.firstinspires.ftc.teamcode.Commands.Automatic.RunTurretAndLauncherFromPoseAuto;
import org.firstinspires.ftc.teamcode.Commands.FollowPathNew;
import org.firstinspires.ftc.teamcode.Commands.Intake.AutoIntakeCheck;
import org.firstinspires.ftc.teamcode.Commands.Launch.ForceLaunchAuto;
import org.firstinspires.ftc.teamcode.Commands.Launch.LaunchWithSort;
import org.firstinspires.ftc.teamcode.Commands.Launch.LaunchWithSortAuto;
import org.firstinspires.ftc.teamcode.Subsystems.VisionSubsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;

@Autonomous (group = "Blue Goal", preselectTeleOp = "BlueTeleOp")
public class BlueSortAuto extends AutoBase {

    /*TODO: Look into Piecewise heading
      TODO: Shoot While Move?
      TODO: Last Drive Cut Power and Coast to save Time
     */

    PathChain MidCycle, CloseCycle, FarCycle;
    Path ToScorePreloads, ToGrabMid, ToScoreMid, ToGrabClose, GrabClose, ToScoreClose, ToGrabFar, ToScoreFar;

    Pose startingPos = new Pose(26.75, 130, Math.toRadians(329));
    Pose scorePreload = new Pose(58.5, 108.5, Math.toRadians(270));
    Pose grabMid = new Pose(23, 65, Math.toRadians(180));
    Pose grabMidCP1 = new Pose(58.5, 88);
    Pose grabMidCP2  = new Pose(59, 66.5);
    Pose grabMidCP3  = new Pose(64, 60);
    Pose scoreMid = new Pose(56.5, 100, Math.toRadians(-110));
    Pose scoreMidCp = new Pose(51, 59.5);

    Pose toGrabClose = new Pose(42, 85, Math.toRadians(180));
    Pose grabClose = new Pose(30, 85, Math.toRadians(180));
    Pose scoreClose = new Pose(60, 89, Math.toRadians(270));
    Pose scoreCloseCP = new Pose(44.5, 82.5);
    Pose grabFar = new Pose(30, 34.5, Math.toRadians(182));
    Pose grabFarCP1 = new Pose(61, 38.5);
    Pose grabFarCP2 = new Pose(57, 37.5);
    Pose grabFarCP3 = new Pose(35, 35);
    Pose scoreFar = new Pose(59, 112, Math.toRadians(270));
    Pose scoreFarCP = new Pose(57, 36);



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
                                HeadingInterpolator.linear(scorePreload.getHeading(), Math.toRadians(180))
                        ),
                        new HeadingInterpolator.PiecewiseNode(
                                .3,
                                1,
                                HeadingInterpolator.constant(Math.toRadians(180))
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
        Command RunLaunchPre = new RunTurretAndLauncherFromPoseAuto(false, new Pose(scorePreload.getX(), scorePreload.getY(), scorePreload.getHeading()));
        Command RunLaunchMid = new RunTurretAndLauncherFromPoseAuto(false, scoreMid);
        Command RunLaunchClose = new RunTurretAndLauncherFromPoseAuto(false, scoreClose);
        Command RunLaunchFar = new RunTurretAndLauncherFromPoseAuto(false, scoreFar);


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
                    new FollowPathNew(MidCycle),
                    new SequentialGroup(
                            new Delay(3),
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
                                new Delay(2),
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
                                new Delay(4),
                                new LambdaCommand().setStart(()->{RunLaunchFar.schedule();}).setIsDone(()->{ return true;}),
                                new InstantCommand(()->{ Intake.cancel();}),
                                IntakeCheck
                        )
                ),
                new Delay(0.0001),
                LaunchWithSort,
                new Delay(0.0001),
                StopLauncher





        );

        runAuto.schedule();


    }

    @Override public void onStop () {}




}
