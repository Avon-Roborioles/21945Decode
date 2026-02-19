package org.firstinspires.ftc.teamcode.OpModes.Auto;



import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Commands.Automatic.RunTurretAndLauncherFromPoseAuto;
import org.firstinspires.ftc.teamcode.Commands.Intake.AutoIntake;
import org.firstinspires.ftc.teamcode.Commands.Intake.AutoIntakeCheck;
import org.firstinspires.ftc.teamcode.Commands.Launch.ForceLaunchAuto;



import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;

@Autonomous
public class Blue18BallAuto extends AutoBase {
    PathChain MidCycle;
    Path ToScorePreloads, ToGrabMid, ToScoreMid, ToRampCycle, ToScoreRampCycle, ToGrabClose, GrabClose, ToScoreClose, ToGrabFar, ToScoreFar;

    Pose startingPos = new Pose(26.75, 130, Math.toRadians(329));
    Pose scorePreload = new Pose(45.2, 119, Math.toRadians(329));
    Pose grabMid = new Pose(13.5, 58.5);
    Pose grabMidCP1 = new Pose(69, 104.5);
    Pose grabMidCP2  = new Pose(59, 66.5);
    Pose grabMidCP3  = new Pose(64, 60);
    Pose scoreMid = new Pose(56.5, 73, Math.toRadians(-110));
    Pose scoreMidCp = new Pose(51, 59.5);
    Pose grabRampCycle = new Pose(12.6,60.3 , Math.toRadians(160));
    Pose grabRampCycleCP = new Pose(38, 57);
    Pose scoreRampCycle = new Pose(56.5, 73, Math.toRadians(-110));
    Pose scoreRampCycleCP = new Pose(51,60);
    Pose toGrabClose = new Pose(37, 85, Math.toRadians(180));
    Pose toGrabCloseCP1 = new Pose(60, 83.6);
    Pose toGrabCloseCP2 = new Pose(47.5, 86.5);
    Pose grabClose = new Pose(16.5, 85, Math.toRadians(180));
    Pose scoreClose = new Pose(58, 89, Math.toRadians(270));
    Pose scoreCloseCP = new Pose(44.5, 82.5);
    Pose grabFar = new Pose(18, 34.5, Math.toRadians(182));
    Pose grabFarCP1 = new Pose(61, 38.5);
    Pose grabFarCP2 = new Pose(57, 37.5);
    Pose grabFarCP3 = new Pose(35, 35);
    Pose scoreFar = new Pose(59, 101.5, Math.toRadians(270));
    Pose scoreFarCP = new Pose(57, 36);



    Command runAuto;


    public void buildPaths() {
        ToScorePreloads = new Path(new BezierLine(startingPos, scorePreload));
        ToScorePreloads.setConstantHeadingInterpolation(scoreCloseCP.getHeading());
        ToScorePreloads.setTimeoutConstraint(1000);

        ToGrabMid = new Path(new BezierCurve(scorePreload, grabMidCP1, grabMidCP2, grabMidCP3, grabMid));
        ToGrabMid.setTangentHeadingInterpolation();
        ToGrabMid.setTimeoutConstraint(1000);

        ToScoreMid = new Path(new BezierCurve(grabMid, scoreMidCp, scoreMid));
        ToScoreMid.setTangentHeadingInterpolation();
        ToScoreMid.reverseHeadingInterpolation();
        ToScoreMid.setTimeoutConstraint(1000);

        ToRampCycle = new Path(new BezierCurve(scoreMid, grabRampCycleCP, grabRampCycle));
        ToRampCycle.setLinearHeadingInterpolation(scoreMid.getHeading(), grabRampCycle.getHeading());
        ToRampCycle.setTimeoutConstraint(1000);

        ToScoreRampCycle = new Path(new BezierCurve(grabRampCycle, scoreRampCycleCP, scoreRampCycle));
        ToScoreRampCycle.setLinearHeadingInterpolation(grabRampCycle.getHeading(), scoreRampCycle.getHeading());
        ToScoreRampCycle.setTimeoutConstraint(1000);

        ToGrabClose = new Path(new BezierCurve(scoreRampCycle, toGrabCloseCP1, toGrabCloseCP2, toGrabClose));
        ToGrabClose.setLinearHeadingInterpolation(scoreRampCycle.getHeading(), toGrabClose.getHeading());
        ToGrabClose.setTimeoutConstraint(1000);

        GrabClose = new Path(new BezierLine(toGrabClose, grabClose));
        GrabClose.setConstantHeadingInterpolation(grabClose.getHeading());
        GrabClose.setTimeoutConstraint(1000);

        ToScoreClose = new Path(new BezierCurve(grabClose, scoreCloseCP, scoreClose));
        ToScoreClose.setLinearHeadingInterpolation(grabClose.getHeading(), scoreClose.getHeading());
        ToScoreClose.setTimeoutConstraint(1000);

        ToGrabFar = new Path(new BezierCurve(scoreClose, grabFarCP1, grabFarCP2, grabFarCP3, grabFar));
        ToGrabFar.setTangentHeadingInterpolation();
        ToGrabFar.setTimeoutConstraint(1000);

        ToScoreFar = new Path(new BezierCurve(grabFar, scoreFarCP, scoreFar));
        ToScoreFar.setLinearHeadingInterpolation(grabFar.getHeading(), scoreFar.getHeading());
        ToScoreFar.setTimeoutConstraint(1000);















    }

    @Override
    public void onInit() {

    }
    @Override public void onWaitForStart () {

    }
    @Override public void onStartButtonPressed () {
        Command RunLaunchPre = new RunTurretAndLauncherFromPoseAuto(false, new Pose(scorePreload.getX(), scorePreload.getY(), scorePreload.getHeading()));
        Command RunLaunchMid = new RunTurretAndLauncherFromPoseAuto(false, scoreMid);
        Command RunLaunchRamp = new RunTurretAndLauncherFromPoseAuto(false, scoreRampCycle);
        Command RunLaunchClose = new RunTurretAndLauncherFromPoseAuto(false, scoreClose);
        Command RunLaunchFar = new RunTurretAndLauncherFromPoseAuto(false, scoreFar);


        Command Intake1 = new AutoIntake(5000);
        Command Intake2 = new AutoIntake(3500);
        Command Intake3 = new AutoIntake(3000);
        Command Intake4 = new AutoIntake(3000);
        Command Intake5 = new AutoIntake(3000);
        Command IntakeCheck = new AutoIntakeCheck();
        Command StopLauncher = new LambdaCommand().setStart(() -> {
            RunLaunchPre.cancel();
            RunLaunchMid.cancel();
            RunLaunchRamp.cancel();
            RunLaunchClose.cancel();
            RunLaunchFar.cancel();
        });

        Command LaunchWOSort = new SequentialGroup(new ForceLaunchAuto(), StopLauncher);

        PedroComponent.follower().setPose(StartPoint);
        PedroComponent.follower().setMaxPower(1.0);
        PedroComponent.follower().update();

        buildPaths();

        runAuto = new SequentialGroup(
                new LambdaCommand().setStart(() -> {
                    RunLaunchPre.schedule();
                }).setIsDone(() -> true), new Delay(0.125), new ParallelGroup(
                new SequentialGroup(
                        new FollowPath(ShootPreloads, false),
                        new Delay(0.005)
                )
        ), LaunchWOSort, new ParallelGroup(
                new SequentialGroup(
                        new FollowPath(DriveToCollectOne),
                        new InstantCommand(() -> {
                            PedroComponent.follower().setMaxPower(0.4);
                        }),
                        new FollowPath(DriveToShootOne)
                ),
                Intake1
        ), new ParallelGroup(
                new SequentialGroup(
                        new InstantCommand(() -> {
                            PedroComponent.follower().setMaxPower(1.0);
                        }),
                        new FollowPath(DriveToShootOne),
                        new Delay(0.25)
                ),
                new LambdaCommand().setStart(() -> {
                    RunLaunch1.schedule();
                }).setIsDone(() -> true),
                IntakeCheck
        ), LaunchWOSort, new ParallelGroup(
                new SequentialGroup(
                        new FollowPath(DriveToCollectTwo),
                        new InstantCommand(() -> {
                            PedroComponent.follower().setMaxPower(0.35);
                        }),
                        new FollowPath(DriveToShootTwo)
                ),
                Intake2
        ), new ParallelGroup(
                new SequentialGroup(
                        new InstantCommand(() -> {
                            PedroComponent.follower().setMaxPower(1.0);
                        }),
                        new FollowPath(DriveToShootTwo),
                        new Delay(0.25)
                ),
                new LambdaCommand().setStart(() -> {
                    RunLaunch2.schedule();
                }).setIsDone(() -> true),
                IntakeCheck
        ), LaunchWOSort, new ParallelGroup(
                new SequentialGroup(
                        new FollowPath(DriveToCollectThree),
                        new InstantCommand(() -> {
                            PedroComponent.follower().setMaxPower(0.35);
                        }),
                        new FollowPath(DriveToShootThree)
                ),
                Intake3
        ), new ParallelGroup(
                new SequentialGroup(
                        new InstantCommand(() -> {
                            PedroComponent.follower().setMaxPower(1.0);
                        }),
                        new FollowPath(DriveToShootThree),
                        new Delay(0.25)
                ),
                new LambdaCommand().setStart(() -> {
                    RunLaunch3.schedule();
                }).setIsDone(() -> true),
                IntakeCheck
        ), LaunchWOSort, new ParallelGroup(
                new SequentialGroup(
                        new FollowPath(DriveToCollectFour),
                        new InstantCommand(() -> {
                            PedroComponent.follower().setMaxPower(0.35);
                        }),
                        new FollowPath(DriveToShootFour)
                ),
                Intake4
        ), new ParallelGroup(
                new SequentialGroup(
                        new InstantCommand(() -> {
                            PedroComponent.follower().setMaxPower(1.0);
                        }),
                        new FollowPath(DriveToShootFour),
                        new Delay(0.25)
                ),
                new LambdaCommand().setStart(() -> {
                    RunLaunch4.schedule();
                }).setIsDone(() -> true),
                IntakeCheck
        ), LaunchWOSort, new ParallelGroup(
                new SequentialGroup(
                        new FollowPath(DriveToCollectFive),
                        new InstantCommand(() -> {
                            PedroComponent.follower().setMaxPower(0.35);
                        }),
                        new FollowPath(DriveToShootFive)
                ),
                Intake5
        ), new ParallelGroup(
                new SequentialGroup(
                        new InstantCommand(() -> {
                            PedroComponent.follower().setMaxPower(1.0);
                        }),
                        new FollowPath(DriveToShootFive),
                        new Delay(0.25)
                ),
                new LambdaCommand().setStart(() -> {
                    RunLaunch5.schedule();
                }).setIsDone(() -> true),
                IntakeCheck
        ),
                LaunchWOSort,
                StopLauncher
        );

        runAuto.schedule();


    }

    @Override public void onStop () {}




}
