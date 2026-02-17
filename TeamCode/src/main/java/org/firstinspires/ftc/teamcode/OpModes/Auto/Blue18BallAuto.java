package org.firstinspires.ftc.teamcode.OpModes.Auto;



import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
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
    Path ShootPreloads, DriveToCollectOne, DriveToShootOne, DriveToCollectTwo, DriveToShootTwo, DriveToCollectThree, DriveToShootThree, DriveToCollectFour, DriveToShootFour, DriveToCollectFive, DriveToShootFive;

    Pose StartPoint = new Pose(25, 124, Math.toRadians(141)).mirror();;
    Pose DriveDownShoot = new Pose(56, 87, Math.toRadians(180)).mirror();;
    Pose CollectSecondLine = new Pose(15, 60, Math.toRadians(180)).mirror();;
    Pose CollectSecondLineCP = new Pose(45, 56).mirror();;
    Pose DriveUpOne = new Pose(56, 87, Math.toRadians(180)).mirror();;
    Pose DriveDownTwo = new Pose(14, 64, Math.toRadians(180)).mirror();
    Pose DriveUpTwo = new Pose(56, 87, Math.toRadians(180)).mirror();;
    Pose DriveDownThree = new Pose(14, 64, Math.toRadians(180)).mirror();;
    Pose DriveDownThreeCP = new Pose(42, 49).mirror();;
    Pose DriveUpThree = new Pose(55, 86, Math.toRadians(180)).mirror();;
    Pose DriveDownFour = new Pose(18, 34, Math.toRadians(180)).mirror();;
    Pose DriveDownFourCP = new Pose(51, 32).mirror();;
    Pose DriveUpFour = new Pose(54, 87, Math.toRadians(180)).mirror();;
    Pose DriveDownFive = new Pose(16, 83, Math.toRadians(180)).mirror();;
    Pose DriveUpFive = new Pose(47, 93, Math.toRadians(180)).mirror();;
    Command runAuto;


    public void buildPaths() {


        ShootPreloads = new Path(new BezierLine(StartPoint, DriveDownShoot));
        ShootPreloads.setLinearHeadingInterpolation((StartPoint.getHeading()), DriveDownShoot.getHeading());


        DriveToCollectOne = new Path(new BezierCurve(DriveDownShoot, CollectSecondLineCP, CollectSecondLine));
        DriveToCollectOne.setLinearHeadingInterpolation((DriveDownShoot.getHeading()), CollectSecondLine.getHeading());


        DriveToShootOne = new Path(new BezierLine(CollectSecondLine, DriveUpOne));
        DriveToShootOne.setLinearHeadingInterpolation((CollectSecondLine.getHeading()), DriveUpOne.getHeading());


        DriveToCollectTwo = new Path(new BezierLine(DriveUpOne, DriveDownTwo));
        DriveToCollectTwo.setLinearHeadingInterpolation((DriveUpOne.getHeading()), DriveDownTwo.getHeading());


        DriveToShootTwo = new Path(new BezierLine(DriveDownTwo, DriveUpTwo));
        DriveToShootTwo.setLinearHeadingInterpolation((DriveDownTwo.getHeading()), DriveUpTwo.getHeading());


        DriveToCollectThree = new Path(new BezierCurve(DriveUpTwo, DriveDownThreeCP, DriveDownThree));
        DriveToCollectThree.setLinearHeadingInterpolation((DriveUpTwo.getHeading()), DriveDownThree.getHeading());


        DriveToShootThree = new Path(new BezierLine(DriveDownThree, DriveUpThree));
        DriveToShootThree.setLinearHeadingInterpolation((DriveDownThree.getHeading()), DriveUpThree.getHeading());


        DriveToCollectFour = new Path(new BezierCurve(DriveUpThree, DriveDownFourCP, DriveDownFour));
        DriveToCollectFour.setLinearHeadingInterpolation((DriveUpThree.getHeading()), DriveDownFour.getHeading());


        DriveToShootFour = new Path(new BezierLine(DriveDownFour, DriveUpFour));
        DriveToShootFour.setLinearHeadingInterpolation((DriveDownFour.getHeading()), DriveUpFour.getHeading());


        DriveToCollectFive = new Path(new BezierLine(DriveUpFour, DriveDownFive));
        DriveToCollectFive.setLinearHeadingInterpolation((DriveUpFour.getHeading()), DriveDownFive.getHeading());


        DriveToShootFive = new Path(new BezierLine(DriveDownFive, DriveUpFive));
        DriveToShootFive.setLinearHeadingInterpolation((DriveDownFive.getHeading()), DriveUpFive.getHeading());

    }

    @Override
    public void onInit() {

    }
    @Override public void onWaitForStart () {

    }
    @Override public void onStartButtonPressed () {
        Command RunLaunchPre = new RunTurretAndLauncherFromPoseAuto(true, new Pose(DriveDownShoot.getX() - 4, DriveDownShoot.getY() - 4, Math.toRadians(250)));
        Command RunLaunch1 = new RunTurretAndLauncherFromPoseAuto(true, DriveUpOne);
        Command RunLaunch2 = new RunTurretAndLauncherFromPoseAuto(true, DriveUpTwo);
        Command RunLaunch3 = new RunTurretAndLauncherFromPoseAuto(true, DriveUpThree);
        Command RunLaunch4 = new RunTurretAndLauncherFromPoseAuto(true, DriveUpFour);
        Command RunLaunch5 = new RunTurretAndLauncherFromPoseAuto(true, DriveUpFive);

        Command Intake1 = new AutoIntake(5000);
        Command Intake2 = new AutoIntake(3500);
        Command Intake3 = new AutoIntake(3000);
        Command Intake4 = new AutoIntake(3000);
        Command Intake5 = new AutoIntake(3000);
        Command IntakeCheck = new AutoIntakeCheck();
        Command StopLauncher = new LambdaCommand().setStart(() -> {
            RunLaunchPre.cancel();
            RunLaunch1.cancel();
            RunLaunch2.cancel();
            RunLaunch3.cancel();
            RunLaunch4.cancel();
            RunLaunch5.cancel();
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
