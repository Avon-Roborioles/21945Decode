package org.firstinspires.ftc.teamcode.Commands.Drive;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.Commands.Launch.LaunchWithOutSort;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.LauncherSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.SorterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.StatusSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.Utility.Timing;

import java.util.concurrent.TimeUnit;

import dev.nextftc.core.commands.Command;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;

@Configurable
public class TeleOpRampScoreDriveCommand extends Command {
    double distanceToGoal;
    double turretFieldAngleRad;
    Timing.Timer startDelay = new Timing.Timer(500);
    Pose botPose;
    boolean launch;
    boolean redAlliance;


    static Pose redGoal = new Pose(144,144);
    static Pose RedGoalAngle = new Pose(136, 134);
    static Pose blueGoal = new Pose(0,144);
    static Pose BlueGoalAngle = new Pose(8, 134);
    Pose goal;
    Pose goalAngle;

    Pose ScorePose;
    public static Pose RedScorePose = new Pose(51, 90, Math.toRadians(270)).mirror();
    public static Pose BlueScorePose = new Pose(51, 90,Math.toRadians(270));
    Pose PreScorePose;
    public static Pose BluePreScorePose = new Pose(48, 64);
    public static Pose RedPreScorePose = new Pose(48, 64).mirror();

    Path ScorePath;

    enum Step{
        GetReady,
        WaitForReady,
        Ready,
        LaunchCenter,
        WaitCenter,
        ResetCenter,
        LaunchLeft,
        WaitLeft,
        ResetLeft,
        LaunchRight,
        ResetRight,
        WaitRight,
        Pause,
        CheckForMiss,
        Done

    }
    boolean firstShot = false;

    Step St = Step.LaunchCenter;
    Timing.Timer wait = new Timing.Timer(200, TimeUnit.MILLISECONDS);
    Timing.Timer reset = new Timing.Timer(150, TimeUnit.MILLISECONDS);
    Timing.Timer delay = new Timing.Timer(1000, TimeUnit.MILLISECONDS);
    Timing.Timer ready = new Timing.Timer(500, TimeUnit.MILLISECONDS);

    Timing.Timer slowDown = new Timing.Timer(500, TimeUnit.MILLISECONDS);





    public TeleOpRampScoreDriveCommand(Boolean redAlliance){
        this.redAlliance = redAlliance;


        addRequirements(PedroComponent.follower());
        setInterruptible(true);
    }
    @Override
    public boolean isDone() {
        return St == Step.Done; // whether or not the command is done
    }

    @Override
    public void start() {
        StatusSubsystem.INSTANCE.setPrismToPWM(965);

        ScorePose = redAlliance ? RedScorePose : BlueScorePose;
        PreScorePose = redAlliance ? RedPreScorePose : BluePreScorePose;
        botPose = redAlliance ? RedScorePose : BlueScorePose;
        goal = redAlliance ? redGoal : blueGoal;
        goalAngle = redAlliance ? RedGoalAngle : BlueGoalAngle;
        TurretSubsystem.INSTANCE.turnTurretOn();
        IntakeSubsystem.INSTANCE.stopIntake();
        PedroComponent.follower().breakFollowing();
        ScorePath = new Path(new BezierCurve(PedroComponent.follower().getPose(), PreScorePose, ScorePose));
        ScorePath.setLinearHeadingInterpolation(PedroComponent.follower().getHeading(), ScorePose.getHeading());

        PedroComponent.follower().followPath(ScorePath);
        launch = false;
        St = Step.GetReady;
        SorterSubsystem.INSTANCE.resetSorter();
        firstShot = false;
        startDelay.start();
        slowDown.start();






        // executed when the command begins
    }

    @Override
    public void update() {

        botPose = PedroComponent.follower().getPose();
        if(slowDown.done() && !launch){
            PedroComponent.follower().setMaxPower(0.8);
        }else{
            PedroComponent.follower().setMaxPower(1);
        }
        distanceToGoal = Math.hypot((goal.getX()- botPose.getX()), (goal.getY()-botPose.getY()));
        turretFieldAngleRad = Math.atan2((goalAngle.getY()- botPose.getY()), (goalAngle.getX()- botPose.getX()));
        if(TurretSubsystem.INSTANCE.turretFine() && LauncherSubsystem.INSTANCE.LaunchReady()&& PedroComponent.follower().getCurrentPath().getClosestPointTValue() >0.9){
            launch = true;
            PedroComponent.follower().holdPoint(new Pose(ScorePose.getX(), ScorePose.getY(), ScorePose.getHeading()));
            PedroComponent.follower().setMaxPower(1);
        }
        if(launch){
            switch (St) {
                case GetReady:
                    SorterSubsystem.INSTANCE.resetSorter();
                    ready.start();
                    St = Step.WaitForReady;
                    break;
                case WaitForReady:

                    if(ready.done() && TurretSubsystem.INSTANCE.turretFine() ){
                        St = Step.Ready;
                    }
                    break;
                case Ready:
                    St = Step.LaunchCenter;

                    break;
                case LaunchCenter:
                    if(!(SorterSubsystem.INSTANCE.centerSlot() == SorterSubsystem.SlotDetection.EMPTY)){
                        if(TurretSubsystem.INSTANCE.turretNotInFlip()) {
                            SorterSubsystem.INSTANCE.sendCenter();
                            wait.start();
                            St = Step.WaitCenter;
                        }
                    } else {
                        St = Step.LaunchLeft;
                    }
                    break;
                case WaitCenter:
                    if(wait.done()){
                        SorterSubsystem.INSTANCE.resetSorter();
                        reset.start();
                        St = Step.ResetCenter;
                    }
                    break;
                case ResetCenter:
                    if(reset.done()){
                        St = Step.LaunchLeft;
                    }
                    break;
                case LaunchLeft:
                    if(!(SorterSubsystem.INSTANCE.leftSlot() == SorterSubsystem.SlotDetection.EMPTY)){
                        if(TurretSubsystem.INSTANCE.turretNotInFlip()) {
                            SorterSubsystem.INSTANCE.sendLeft();
                            wait.start();
                            St = Step.WaitLeft;
                        }
                    } else {
                        St = Step.LaunchRight;

                    }
                    break;
                case WaitLeft:
                    if(wait.done()){
                        SorterSubsystem.INSTANCE.resetSorter();
                        reset.start();
                        St = Step.ResetLeft;
                    }
                    break;
                case ResetLeft:
                    if(reset.done()){
                        St = Step.LaunchRight;
                    }
                    break;
                case LaunchRight:
                    if(!(SorterSubsystem.INSTANCE.rightSlot() == SorterSubsystem.SlotDetection.EMPTY)){
                        if(TurretSubsystem.INSTANCE.turretNotInFlip()) {
                            SorterSubsystem.INSTANCE.sendRight();
                            wait.start();
                            St = Step.WaitRight;
                        }
                    } else {
                        St = Step.Pause;
                    }
                    break;
                case WaitRight:
                    if(wait.done()){
                        SorterSubsystem.INSTANCE.resetSorter();
                        reset.start();
                        St = Step.ResetRight;
                    }
                    break;
                case ResetRight:
                    if(reset.done()){
                        St = Step.Pause;
                    }
                    break;
                case Pause:
                    if(!delay.isTimerOn()) {
                        delay.start();
                    }
                    if(delay.done()) {
                        St = Step.CheckForMiss;
                    }
                    break;
                case CheckForMiss:
                    if(!(SorterSubsystem.INSTANCE.leftSlot() == SorterSubsystem.SlotDetection.EMPTY) || !(SorterSubsystem.INSTANCE.centerSlot() == SorterSubsystem.SlotDetection.EMPTY) || !(SorterSubsystem.INSTANCE.rightSlot() == SorterSubsystem.SlotDetection.EMPTY)){
                        St = Step.LaunchCenter;
                    }else {
                        St = Step.Done;
                    }
                    break;
            }

        }

        TurretSubsystem.INSTANCE.turnTurretToFieldAngleAuto(turretFieldAngleRad, ScorePose.getHeading());
        LauncherSubsystem.INSTANCE.RunLauncherFromDistance(distanceToGoal);
        ActiveOpMode.telemetry().addLine("-------------- Ramp Score Drive Telemetry: --------------");
        ActiveOpMode.telemetry().addData("BotPose", botPose);
        ActiveOpMode.telemetry().addData("distanceToGoal", distanceToGoal);
        ActiveOpMode.telemetry().addData("turretFieldAngleDeg", Math.toDegrees(turretFieldAngleRad));
        ActiveOpMode.telemetry().addData("Step", St);

//        -

        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {
        PedroComponent.follower().breakFollowing();
        PedroComponent.follower().startTeleOpDrive();
        LauncherSubsystem.INSTANCE.HoodDown().schedule();
        LauncherSubsystem.INSTANCE.StopLauncher.schedule();
        StatusSubsystem.INSTANCE.setPrismNorm();
        TurretSubsystem.INSTANCE.turnTurretOff();
        SorterSubsystem.INSTANCE.resetSorter();
        PedroComponent.follower().setMaxPower(1);


        // executed when the command ends
    }
}
