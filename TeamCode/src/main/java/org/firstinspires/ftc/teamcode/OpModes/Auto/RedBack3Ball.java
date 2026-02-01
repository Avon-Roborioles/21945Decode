package org.firstinspires.ftc.teamcode.OpModes.Auto;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Commands.Automatic.RunTurretAndLauncherFromPoseAuto;
import org.firstinspires.ftc.teamcode.Commands.Launch.ForceLaunchAutoSlow;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;

@Autonomous (group = "Red Back", preselectTeleOp = "RedTeleOp")
public class RedBack3Ball extends AutoBase {
    Path EndDrive;
    Pose startingPos = new Pose(56.65, 10.25, Math.toRadians(270)).mirror();
    Pose endPos = new Pose(30, 12, Math.toRadians(270)).mirror();
    double maxPower = 1;






    Command runAuto;



    public void buildPaths () {


        EndDrive = new Path(new BezierLine(startingPos, endPos));
        EndDrive.setLinearHeadingInterpolation(startingPos.getHeading(), endPos.getHeading());
        EndDrive.setTimeoutConstraint(2000);















    }
    @Override
    public void onInit() {

    }
    @Override public void onWaitForStart () {

    }
    @Override public void onStartButtonPressed (){

//
        Command RunLaunchPre = new RunTurretAndLauncherFromPoseAuto(true, new Pose(startingPos.getX()-5, startingPos.getY()-5, startingPos.getHeading()+Math.toRadians(-1)));

        Command StopLauncher = new LambdaCommand().setStart(()->{RunLaunchPre.cancel();
        }).setIsDone(()->{ return true;});
        Command LaunchWOSort = new SequentialGroup(new ForceLaunchAutoSlow(), StopLauncher);
        PedroComponent.follower().setPose(startingPos);
        PedroComponent.follower().setMaxPower(maxPower);
        PedroComponent.follower().update();
        buildPaths();
        runAuto = new SequentialGroup(
                new LambdaCommand().setStart(()->{RunLaunchPre.schedule();}).setIsDone(()->{ return true;}),
                new Delay(3),
                LaunchWOSort,
                StopLauncher,
                new FollowPath(EndDrive)

        );
       runAuto.schedule();
    }

    @Override public void onStop () {}




}






