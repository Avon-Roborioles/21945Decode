package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.Commands.Automatic.RunTurretAndLauncherFromHeading;
import org.firstinspires.ftc.teamcode.Commands.Intake.AutoIntake;
import org.firstinspires.ftc.teamcode.Commands.Launch.ForceLaunch;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;

public class BlueGoalTwo extends AutoBase {
    PathChain CollectBalls, DriveToLine, CollectBalls2, DriveToLine2, CollectBalls3, DriveToLine3;
    Command runAuto;

    Pose StartingPoint = new Pose(56, 8);
    Pose toCollect1 = new Pose(15, 35);
    Pose toCollect1CP = new Pose(51, 40);
    Pose toDriveLine1 = new Pose(66, 75);
    Pose toDriveLine1CP = new Pose(51, 57);
    Pose toCollect2 = new Pose(15, 60);
    Pose toCollect2CP = new Pose(38, 58);
    Pose toDriveLine2 = new Pose(62, 80);
    Pose toDriveLine2CP = new Pose(49, 69);
    Pose toCollect3 = new Pose(15, 84);
    Pose toCollect3CP = new Pose(31, 86);
    Pose toDriveLine3 = new Pose(58, 86);
    Pose toDriveLine3CP = new Pose(35, 89);

    public void buildPaths() {
        Follower follower = PedroComponent.follower();
        {
            CollectBalls = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    StartingPoint,
                                    toCollect1CP,
                                    toCollect1
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))

                    .build();

            DriveToLine = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    toCollect1,
                                    toDriveLine1CP,
                                    toDriveLine1
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))

                    .build();

            CollectBalls2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    toDriveLine1,
                                    toCollect2CP,
                                    toCollect2
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))

                    .build();

            DriveToLine2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    toCollect2,
                                    toDriveLine2CP,
                                    toDriveLine2
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))

                    .build();

            CollectBalls3 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    toDriveLine2,
                                    toCollect3CP,
                                    toCollect3
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))

                    .build();

            DriveToLine3 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    toCollect3,
                                    toDriveLine3CP,
                                    toDriveLine3
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))

                    .build();
        }


    }
    @Override
    public void onInit() {
        Command RunLaunch = new RunTurretAndLauncherFromHeading(false);
        Command Intake = new AutoIntake(3500);
        Command StopLauncher = new LambdaCommand().setStart(()->{RunLaunch.cancel();}).setIsDone(()->{ return true;});
        Command LaunchWOSort = new SequentialGroup(new ForceLaunch(), StopLauncher);
        PedroComponent.follower().setPose(StartingPoint);
        PedroComponent.follower().setMaxPower(0.75);
        buildPaths();
        runAuto = new SequentialGroup(
                new ParallelGroup(
                        new SequentialGroup(
                                new FollowPath(CollectBalls),
                        new AutoIntake(3500)
                ),
                new ParallelGroup(
                        new SequentialGroup(
                                new FollowPath(DriveToLine),
                                LaunchWOSort
                        ),
                        new InstantCommand(()->{
                            RunLaunch.schedule();
                        })
                ),
                new ParallelGroup(new SequentialGroup(
                        new FollowPath(CollectBalls2),
                        new AutoIntake(3500)
                ), new ParallelGroup(
                        new SequentialGroup(
                                new FollowPath(DriveToLine2),
                                LaunchWOSort
                        ),
                        new InstantCommand(()->{
                            RunLaunch.schedule();
                        })
                ), new ParallelGroup(
                        new SequentialGroup(
                                new FollowPath(CollectBalls3),
                                new AutoIntake(2500)
                        ),
                        new ParallelGroup(
                                new SequentialGroup(
                                        new FollowPath(DriveToLine3),
                                        LaunchWOSort
                                ),
                                new InstantCommand(()->{
                                    RunLaunch.schedule();
                                })
                        )

                )
                )
                )
        )
        ;}
    @Override public void onWaitForStart () {}
    @Override public void onStartButtonPressed (){
        runAuto.schedule();
    }
    @Override public void onUpdate () {
        telemetry.update();

    }
    @Override public void onStop () {}




}













