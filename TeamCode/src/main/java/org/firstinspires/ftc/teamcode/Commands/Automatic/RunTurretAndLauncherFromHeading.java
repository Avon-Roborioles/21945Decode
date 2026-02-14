package org.firstinspires.ftc.teamcode.Commands.Automatic;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.Subsystems.LauncherSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.StatusSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;

import dev.nextftc.core.commands.Command;
import dev.nextftc.extensions.pedro.PedroComponent;

@Configurable
public class RunTurretAndLauncherFromHeading extends Command {

    Pose botPose;
    double distanceToGoal;
    boolean redAlliance;
    double turretFieldAngleRad;


    static Pose redGoal = new Pose(144,144);
    static Pose RedGoalAngle = new Pose(136, 134);
    static Pose blueGoal = new Pose(0,144);
    static Pose BlueGoalAngle = new Pose(8, 134);
    Pose goal;
    Pose goalAngle;
    public static double shotTime = 0.85;
    Boolean lightsNeedSet = false;
    Boolean happy = false;


    public RunTurretAndLauncherFromHeading(boolean redAlliance) {
        this.redAlliance = redAlliance;
        requires(TurretSubsystem.INSTANCE);
        setInterruptible(true); // this is the default, so you don't need to specify
    }

    @Override
    public boolean isDone() {
        return false;// whether or not the command is done
    }

    @Override
    public void start() {
        TurretSubsystem.INSTANCE.turnTurretOn();
        goal = redAlliance ? redGoal : blueGoal;
        goalAngle = redAlliance ? RedGoalAngle : BlueGoalAngle;

        // executed when the command begins
    }

    @Override
    public void update() {
        botPose = PedroComponent.follower().getPose();
        PedroComponent.follower().getVelocity().getXComponent();
        PedroComponent.follower().getVelocity().getYComponent();
        distanceToGoal = Math.hypot((goal.getX()- botPose.getX()-PedroComponent.follower().getVelocity().getXComponent()*shotTime), (goal.getY()-botPose.getY() - PedroComponent.follower().getVelocity().getYComponent()*shotTime));
        turretFieldAngleRad = Math.atan2((goalAngle.getY()- botPose.getY() - PedroComponent.follower().getVelocity().getYComponent()*shotTime), (goalAngle.getX()- botPose.getX() - PedroComponent.follower().getVelocity().getXComponent()*shotTime) + PedroComponent.follower().getAngularVelocity()* StatusSubsystem.INSTANCE.getLoopTimeSeconds());

        TurretSubsystem.INSTANCE.turnTurretToFieldAngle(turretFieldAngleRad);
        LauncherSubsystem.INSTANCE.RunLauncherFromDistance(distanceToGoal);
        if(TurretSubsystem.INSTANCE.TurretHappy()){
                StatusSubsystem.INSTANCE.setPrismGreen();
        }else {
                StatusSubsystem.INSTANCE.setPrismOrange();

        }
//        ActiveOpMode.telemetry().addLine("-------------- RunTurretAndLauncherFromHeading Telemetry: --------------");
//        ActiveOpMode.telemetry().addData("redAlliance", redAlliance);
//        ActiveOpMode.telemetry().addData("BotPose", botPose);
//        ActiveOpMode.telemetry().addData("distanceToGoal", distanceToGoal);
//        ActiveOpMode.telemetry().addData("turretFieldAngleDeg", Math.toDegrees(turretFieldAngleRad));
//

        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {

        LauncherSubsystem.INSTANCE.HoodDown().schedule();
        LauncherSubsystem.INSTANCE.StopLauncher.schedule();
        StatusSubsystem.INSTANCE.setPrismNorm();
        TurretSubsystem.INSTANCE.turnTurretOff();

        // executed when the command ends
    }
}
