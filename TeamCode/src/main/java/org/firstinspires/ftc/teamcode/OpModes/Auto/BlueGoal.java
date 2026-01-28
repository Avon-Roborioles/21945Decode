package org.firstinspires.ftc.teamcode.OpModes.Auto;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Commands.Automatic.RunTurretAndLauncherFromHeading;
import org.firstinspires.ftc.teamcode.Commands.Automatic.RunTurretAndLauncherFromPoseAuto;
import org.firstinspires.ftc.teamcode.Commands.Intake.AutoIntake;
import org.firstinspires.ftc.teamcode.Commands.Intake.IntakeToSorterCommand;
import org.firstinspires.ftc.teamcode.Commands.Launch.ForceLaunch;
import org.firstinspires.ftc.teamcode.Commands.Launch.ForceLaunchAuto;
import org.firstinspires.ftc.teamcode.Subsystems.CompVisionSubsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelDeadlineGroup;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.ParallelRaceGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;

@Autonomous
public class BlueGoal extends AutoBase {
    Path DriveToScorePreload, DriveToPickUp1, DrivePickUp1, DriveToScore1, DriveToPickUp2, DrivePickUp2, DriveToScore2, DriveToPickUp3, DrivePickUp3, DriveToScore3, DriveEndDrive;
    Pose startingPos = new Pose(26.75, 130, Math.toRadians(141));
    Pose scorePreload = new Pose(54, 120, Math.toRadians(270));
    Pose toPickUp1 = new Pose(46, 84, Math.toRadians(180));
    Pose pickUp1 = new Pose(22, 84, Math.toRadians(180));
    Pose toScore1 = new Pose(56, 78, Math.toRadians(270));
    Pose toPickUp2 = new Pose( 44, 60, Math.toRadians(180));
    Pose toPickUp2CP = new Pose(57, 58);
    Pose pickUp2 = new Pose(22, 60, Math.toRadians(180));
    Pose toScore2 = new Pose(56, 78, Math.toRadians(270));
    Pose toScore2CP = new Pose(51, 61);
    Pose toPickUp3 = new Pose(44,35.5 , Math.toRadians(180));
    Pose toPickUp3CP = new Pose(50, 33);
    Pose pickUp3 = new Pose(22, 35.5, Math.toRadians(180));
    Pose toScore3 = new Pose(56,78 , Math.toRadians(270));
    Pose toScore3CP = new Pose(50, 38);
    Pose endPos = new Pose(60,64, Math.toRadians(270));
    double maxPower = 1;






    Command runAuto;



    public void buildPaths () {
        DriveToScorePreload = new Path(new BezierLine(startingPos, scorePreload));
        DriveToScorePreload.setLinearHeadingInterpolation(startingPos.getHeading(), scorePreload.getHeading());
        DriveToScorePreload.setTimeoutConstraint(4000);

        DriveToPickUp1 = new Path(new BezierLine(scorePreload, toPickUp1));
        DriveToPickUp1.setLinearHeadingInterpolation(scorePreload.getHeading(), toPickUp1.getHeading());

        DrivePickUp1 = new Path(new BezierLine(toPickUp1, pickUp1));
        DrivePickUp1.setLinearHeadingInterpolation(toPickUp1.getHeading(), pickUp1.getHeading());

        DriveToScore1 = new Path(new BezierLine(pickUp1, toScore1));
        DriveToScore1.setLinearHeadingInterpolation(pickUp1.getHeading(), toScore1.getHeading());
        DriveToScore1.setTimeoutConstraint(1500);


        DriveToPickUp2 = new Path(new BezierCurve(toScore1, toPickUp2CP, toPickUp2));
        DriveToPickUp2.setLinearHeadingInterpolation(toScore1.getHeading(), toPickUp2.getHeading());

        DrivePickUp2 = new Path(new BezierLine(toPickUp2, pickUp2));
        DrivePickUp2.setLinearHeadingInterpolation(toPickUp2.getHeading(), pickUp2.getHeading());

        DriveToScore2 = new Path(new BezierCurve(pickUp2, toScore2CP, toScore2));
        DriveToScore2.setLinearHeadingInterpolation(pickUp2.getHeading(), toScore2.getHeading());
        DriveToScore2.setTimeoutConstraint(1500);

        DriveToPickUp3 = new Path(new BezierCurve(toScore2, toPickUp3CP, toPickUp3));
        DriveToPickUp3.setLinearHeadingInterpolation(toScore2.getHeading(), toPickUp3.getHeading());

        DrivePickUp3 = new Path(new BezierLine(toPickUp3, pickUp3));
        DrivePickUp3.setLinearHeadingInterpolation(toPickUp3.getHeading(), pickUp3.getHeading());

        DriveToScore3 = new Path(new BezierCurve(pickUp3, toScore3CP, toScore3));
        DriveToScore3.setLinearHeadingInterpolation(pickUp3.getHeading(), toScore3.getHeading());
        DriveToScore3.setTimeoutConstraint(1500);

        DriveEndDrive = new Path(new BezierLine(toScore3, endPos));
        DriveEndDrive.setLinearHeadingInterpolation(toScore3.getHeading(), endPos.getHeading());


    }
    @Override
    public void onInit() {

    }
    @Override public void onWaitForStart () {

    }
    @Override public void onStartButtonPressed (){

//
        Command RunLaunchPre = new RunTurretAndLauncherFromPoseAuto(false, scorePreload);
        Command RunLaunch1 = new RunTurretAndLauncherFromPoseAuto(false, toScore1);
        Command RunLaunch2 = new RunTurretAndLauncherFromPoseAuto(false, toScore2);
        Command RunLaunch3 = new RunTurretAndLauncherFromPoseAuto(false, toScore3);

        Command Intake = new AutoIntake();
        Command StopLauncher = new LambdaCommand().setStart(()->{RunLaunchPre.cancel();RunLaunch1.cancel();RunLaunch2.cancel();RunLaunch3.cancel();
        }).setIsDone(()->{ return true;});
        Command LaunchWOSort = new SequentialGroup(new ForceLaunchAuto(), StopLauncher);
        PedroComponent.follower().setPose(new Pose(26.75, 130, Math.toRadians(141)));
        PedroComponent.follower().setMaxPower(maxPower);
        PedroComponent.follower().update();
        buildPaths();
        runAuto = new SequentialGroup(
                new LambdaCommand().setStart(()->{RunLaunchPre.schedule();}).setIsDone(()->{ return true;}),
                new Delay(0.25),
                 new ParallelGroup(
                            new SequentialGroup(
                                    new FollowPath(DriveToScorePreload, false),
                                    new Delay(0.25),
                                    LaunchWOSort
                            )

                 ),
                 new ParallelGroup(
                         new SequentialGroup(
                                 new FollowPath(DriveToPickUp1),
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(0.5);}),
                                 new FollowPath(DrivePickUp1)),
                         Intake
                 ),
                 new ParallelGroup(
                         new SequentialGroup(
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(1);}),
                                 new FollowPath(DriveToScore1),
                                 new Delay(0.25),
                                 LaunchWOSort
                         ),
                         new LambdaCommand().setStart(()->{RunLaunch1.schedule();}).setIsDone(()->{ return true;}),
                         Intake
                 ),
                 new ParallelGroup(
                         new SequentialGroup(
                                 new FollowPath(DriveToPickUp2),
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(0.35);}),
                                 new FollowPath(DrivePickUp2)),
                         Intake
                 ),
                 new ParallelGroup(
                         new SequentialGroup(
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(maxPower);}),
                                 new FollowPath(DriveToScore2),
                                 new Delay(0.25),
                                 LaunchWOSort
                         ),
                         new LambdaCommand().setStart(()->{RunLaunch2.schedule();}).setIsDone(()->{ return true;}),
                         Intake
                 ),
                 new ParallelGroup(
                         new SequentialGroup(
                                 new FollowPath(DriveToPickUp3),
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(0.4);}),
                                 new FollowPath(DrivePickUp3)),
                         Intake
                 ),
                 new ParallelGroup(
                         new SequentialGroup(
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(maxPower);}),
                                 new FollowPath(DriveToScore3),
                                 LaunchWOSort
                         ),
                         new LambdaCommand().setStart(()->{RunLaunch3.schedule();}).setIsDone(()->{ return true;}),
                         Intake
                 ),
                 new ParallelGroup(
                        new FollowPath(DriveEndDrive),
                        StopLauncher
                 )

        );
       runAuto.schedule();
    }

    @Override public void onStop () {}




}






