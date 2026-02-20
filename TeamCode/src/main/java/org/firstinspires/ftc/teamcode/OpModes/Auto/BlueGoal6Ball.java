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
import org.firstinspires.ftc.teamcode.Subsystems.SorterSubsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;

@Autonomous (group = "Blue Goal", preselectTeleOp = "BlueTeleOp")
public class BlueGoal6Ball extends AutoBase {
    Path DriveToScorePreload, DriveToPickUp1, DrivePickUp1, DriveToScore1, DriveToPickUp2, DrivePickUp2, DriveToScore2, DriveToPickUp3, DrivePickUp3, DriveToScore3, DriveEndDrive;
    Pose startingPos = new Pose(26.75, 130, Math.toRadians(141));
    Pose scorePreload = new Pose(54, 114, Math.toRadians(270));
    Pose toPickUp1 = new Pose(46, 84, Math.toRadians(180));
    Pose pickUp1 = new Pose(21, 78, Math.toRadians(170));
    Pose toScore1 = new Pose(56, 110, Math.toRadians(270));

    double maxPower = 1;






    Command runAuto;



    public void buildPaths () {
        DriveToScorePreload = new Path(new BezierLine(startingPos, scorePreload));
        DriveToScorePreload.setLinearHeadingInterpolation(startingPos.getHeading(), scorePreload.getHeading());
        DriveToScorePreload.setTimeoutConstraint(2000);

        DriveToPickUp1 = new Path(new BezierLine(scorePreload, toPickUp1));
        DriveToPickUp1.setLinearHeadingInterpolation(scorePreload.getHeading(), toPickUp1.getHeading());
        DriveToPickUp1.setTimeoutConstraint(2000);



        DrivePickUp1 = new Path(new BezierLine(toPickUp1, pickUp1));
        DrivePickUp1.setLinearHeadingInterpolation(toPickUp1.getHeading(), pickUp1.getHeading());
        DrivePickUp1.setTimeoutConstraint(3000);

        DriveToScore1 = new Path(new BezierLine(pickUp1, toScore1));
        DriveToScore1.setLinearHeadingInterpolation(pickUp1.getHeading(), toScore1.getHeading());
        DriveToScore1.setTimeoutConstraint(1750);



    }
    @Override
    public void onInit() {

    }
    @Override public void onWaitForStart () {

    }
    @Override public void onStartButtonPressed (){

//
        Command RunLaunchPre = new RunTurretAndLauncherFromPoseAuto(false, new Pose(scorePreload.getX()+4, scorePreload.getY()+4, Math.toRadians(290)));
        Command RunLaunch1 = new RunTurretAndLauncherFromPoseAuto(false, toScore1);

        Command Intake1 = new AutoIntake(6000);
        Command Intake2 = new AutoIntake(5000);

        Command IntakeCheck = new AutoIntakeCheck();
        Command StopLauncher = new LambdaCommand().setStart(()->{RunLaunchPre.cancel();RunLaunch1.cancel();
        }).setIsDone(()->{ return true;});
        Command LaunchWOSort = new SequentialGroup(new ForceLaunchAuto(), StopLauncher);
        PedroComponent.follower().setPose(startingPos);
        PedroComponent.follower().setMaxPower(maxPower);
        PedroComponent.follower().update();
        buildPaths();
        runAuto = new SequentialGroup(
                new LambdaCommand().setStart(() -> {
                    SorterSubsystem.INSTANCE.sortHug();
                }).setIsDone(() -> {return true;}),
                new LambdaCommand().setStart(()->{RunLaunchPre.schedule();}).setIsDone(()->{ return true;}),
                new Delay(0.125),
                 new ParallelGroup(
                            new SequentialGroup(
                                    new FollowPath(DriveToScorePreload, false),
                                    new Delay(0.025)

                            )

                 ),
                LaunchWOSort,
                 new ParallelGroup(
                         new SequentialGroup(
                                 new FollowPath(DriveToPickUp1),
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(0.35);}),
                                 new FollowPath(DrivePickUp1)),
                         Intake1
                 ),
                 new ParallelGroup(
                         new SequentialGroup(
                                 new Delay(2),
                                 new InstantCommand(()->{ PedroComponent.follower().setMaxPower(1);}),
                                 new FollowPath(DriveToScore1),
                                 new Delay(0.25)

                         ),
                         new LambdaCommand().setStart(()->{RunLaunch1.schedule();}).setIsDone(()->{ return true;}),
                         IntakeCheck
                 ),
                LaunchWOSort,
                StopLauncher

        );
       runAuto.schedule();
    }

    @Override public void onStop () {}




}






