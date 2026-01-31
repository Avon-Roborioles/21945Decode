package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.Commands.Automatic.RunTurretAndLauncherFromHeading;
import org.firstinspires.ftc.teamcode.Commands.Intake.AutoIntake;
import org.firstinspires.ftc.teamcode.Commands.Launch.ForceLaunch;

import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;

public class SimpleBlueGoal extends AutoBase {

    PathChain DriveToTriangle, DriveToBase;
    SequentialGroup runAuto;

  Pose StartingPoint = new Pose (56,8);
  Pose toDrive = new Pose (60, 89);
  Pose toBase = new Pose (103,33);

    public void buildPaths() {
        Follower follower = PedroComponent.follower();
        {
            DriveToTriangle = follower.pathBuilder().addPath(
                            new BezierLine(
                                    StartingPoint,
                                    toDrive)
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))

                    .build();

            DriveToBase = follower.pathBuilder().addPath(
                            new BezierLine(
                                   toDrive,
                                    toBase)

                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))

                    .build();
        }
    }

    @Override
    public void onInit() {
        dev.nextftc.core.commands.Command RunLaunch = new RunTurretAndLauncherFromHeading(false);
        dev.nextftc.core.commands.Command StopLauncher = new LambdaCommand().setStart(()->{RunLaunch.cancel();}).setIsDone(()->{ return true;});
        dev.nextftc.core.commands.Command LaunchWOSort = new SequentialGroup(new ForceLaunch(), StopLauncher);
        PedroComponent.follower().setPose(StartingPoint);
        PedroComponent.follower().setMaxPower(0.75);
        buildPaths();
        runAuto = new SequentialGroup(
                new ParallelGroup(
                        new SequentialGroup(
                                new FollowPath(DriveToTriangle),
                                new AutoIntake(2500)
                        ),
                        new ParallelGroup(
                                new SequentialGroup(
                                        new FollowPath(DriveToBase),
                                        LaunchWOSort
                                ),
                                new InstantCommand(()-> {
                                    RunLaunch.schedule();




                                }







        ))))

         ;}

@Override public void onWaitForStart () {}
@Override public void onStartButtonPressed (){ 
    runAuto.schedule();;
}
@Override public void onUpdate () {
    telemetry.update();

}
@Override public void onStop () {}




}