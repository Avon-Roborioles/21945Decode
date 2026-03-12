package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.Commands.Automatic.RunTurretAndLauncherFromHeading;
import org.firstinspires.ftc.teamcode.Commands.Automatic.RunTurretAndLauncherFromHeadingNickMode;
import org.firstinspires.ftc.teamcode.Commands.Drive.TeleOpDriveCommand;
import org.firstinspires.ftc.teamcode.Commands.Drive.TeleOpRampDriveCommand;
import org.firstinspires.ftc.teamcode.Commands.Drive.HumanPlayerReset;
import org.firstinspires.ftc.teamcode.Commands.Drive.TeleOpRampScoreDriveCommand;
import org.firstinspires.ftc.teamcode.Commands.Intake.IntakeToSorterCommand;
import org.firstinspires.ftc.teamcode.Commands.Launch.ForceLaunch;
import org.firstinspires.ftc.teamcode.Commands.Launch.LaunchGreen;
import org.firstinspires.ftc.teamcode.Commands.Launch.LaunchPurple;
import org.firstinspires.ftc.teamcode.Commands.Launch.LaunchWithOutSort;
import org.firstinspires.ftc.teamcode.Commands.Launch.LaunchWithSort;
import org.firstinspires.ftc.teamcode.Commands.Tilt.PTOJoystickCommand;
import org.firstinspires.ftc.teamcode.Commands.Tilt.TiltCommand;
import org.firstinspires.ftc.teamcode.Commands.Turret.TurretJoystickCommand;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.LauncherSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.PTOSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.SorterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.StatusSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.VisionSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.LauncherSubsystemGroup;
import org.firstinspires.ftc.teamcode.Utility.PosStorage;
import org.firstinspires.ftc.teamcode.Utility.Storage;
import org.firstinspires.ftc.teamcode.Utility.Timing;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.concurrent.TimeUnit;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.components.BulkReadComponent;

public abstract class TeleOpBase extends Storage {
    Pose Botpose;
    TeleOpDriveCommand driverControlled;
    TeleOpRampDriveCommand rampDriveCommand;
    TeleOpRampScoreDriveCommand rampScoreDriveCommand;
    PTOJoystickCommand joyCommand;
    TiltCommand tiltCommand;
    Boolean inLift = false;

    Timing.Timer waitTimer = new Timing.Timer(100, TimeUnit.MILLISECONDS);
    Timing.Timer deBounce = new Timing.Timer(1000, TimeUnit.MILLISECONDS);


    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    {

        addComponents(
                new SubsystemComponent(LauncherSubsystem.INSTANCE, IntakeSubsystem.INSTANCE, SorterSubsystem.INSTANCE, PTOSubsystem.INSTANCE, TurretSubsystem.INSTANCE, VisionSubsystem.INSTANCE, StatusSubsystem.INSTANCE, LauncherSubsystemGroup.INSTANCE),
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {
        ActiveOpMode.telemetry().clear();


        initPoseSelect();



    }
    public abstract TeleOpDriveCommand driveCommand();
    public abstract TeleOpRampDriveCommand rampDriveCommand();
    public abstract TeleOpRampScoreDriveCommand rampScoreDriveCommand();
    public abstract Boolean RedAlliance();


    @Override
    public void onWaitForStart() {

        waitTimer.start();
        runPoseSelect();
        while (!waitTimer.done()&& !ActiveOpMode.isStopRequested()){
            telemetry.update();
            telemetry.update();
        }






    }


    @Override
    public void onStartButtonPressed() {
        deBounce.start();






        Command launchWithoutSort = new LaunchWithOutSort();
        Command launchWithSort = new LaunchWithSort();
        Command intakeToSorter = new IntakeToSorterCommand();
        Command runTurretAndLauncherFromHeading = new RunTurretAndLauncherFromHeading(RedAlliance());
        Command runTurretAndLauncherFromHeadingNickMode = new RunTurretAndLauncherFromHeadingNickMode(RedAlliance());
        Command runTurretFromJoystick = new TurretJoystickCommand(Gamepads.gamepad2().rightStickX(), Gamepads.gamepad2().leftStickX());
        Command forceLaunch = new ForceLaunch();

        Command eStop = new SequentialGroup(LauncherSubsystem.INSTANCE.StopLauncher, IntakeSubsystem.INSTANCE.StopIntake, LauncherSubsystem.INSTANCE.HoodDown(), new LambdaCommand().setStart(intakeToSorter::cancel).setIsDone(() -> true),
                new LambdaCommand().setStart(forceLaunch::cancel).setIsDone(() -> true),
                new LambdaCommand().setStart(runTurretFromJoystick::cancel).setIsDone(() -> true),
                new LambdaCommand().setStart(runTurretAndLauncherFromHeading::cancel).setIsDone(() -> true),
                new LambdaCommand().setStart(launchWithSort::cancel).setIsDone(() -> true));
        runTurretFromJoystick.schedule();
        PedroComponent.follower().setPose( PosStorage.memory.lastPose);
        PedroComponent.follower().setHeading( PosStorage.memory.lastPose.getHeading());

        joyCommand = new PTOJoystickCommand(Gamepads.gamepad2().leftStickY());
        tiltCommand = new TiltCommand();

        driverControlled = driveCommand();
        rampDriveCommand = rampDriveCommand();
        rampScoreDriveCommand = rampScoreDriveCommand();



        driverControlled.schedule();

        Gamepads.gamepad1().circle().whenBecomesTrue(launchWithoutSort).whenBecomesFalse(new LambdaCommand().setStart(launchWithoutSort::cancel).setIsDone(() -> true));
        Gamepads.gamepad1().square().whenBecomesTrue(new HumanPlayerReset(RedAlliance()));

        Gamepads.gamepad1().cross().whenBecomesTrue(new ParallelGroup(runTurretAndLauncherFromHeading,new LambdaCommand().setStart(()->{intakeToSorter.cancel();}).setIsDone(()->true))).whenBecomesFalse(new LambdaCommand().setStart(() -> {runTurretAndLauncherFromHeading.cancel();runTurretFromJoystick.schedule();}).setIsDone(() -> true));;
        Gamepads.gamepad1().dpadUp().whenBecomesTrue(PTOSubsystem.INSTANCE.disengage);
        Gamepads.gamepad1().dpadDown().whenBecomesTrue(PTOSubsystem.INSTANCE.engage);
        Gamepads.gamepad1().dpadLeft();
        Gamepads.gamepad1().dpadRight();
        Gamepads.gamepad1().leftStickButton().whenBecomesTrue(new ParallelGroup(new SequentialGroup(new Delay(.015),rampScoreDriveCommand),intakeToSorter))
                .whenBecomesFalse(new SequentialGroup(new Delay(.015),new LambdaCommand().setStart(() -> {rampDriveCommand.cancel();}),new Delay(0.015),new LambdaCommand().setStart(() -> {driverControlled.schedule(); })));;
        Gamepads.gamepad1().rightStickButton().whenBecomesTrue(new ParallelGroup(new SequentialGroup(new Delay(.015),rampDriveCommand),intakeToSorter))
                .whenBecomesFalse(new SequentialGroup(new Delay(.015),new LambdaCommand().setStart(() -> {rampDriveCommand.cancel();}),new Delay(0.015),new LambdaCommand().setStart(() -> {driverControlled.schedule(); }), intakeToSorter));
        Gamepads.gamepad1().leftTrigger().atLeast(0.75).whenBecomesTrue(new LambdaCommand().setStart(()->{; IntakeSubsystem.INSTANCE.Outtake.cancel(); IntakeSubsystem.INSTANCE.stopIntake();} ).setIsDone(() -> true));;
        Gamepads.gamepad1().rightTrigger().atLeast(0.75).whenBecomesTrue(new LambdaCommand().setStart(()->{PedroComponent.follower().setMaxPower(0.5);}).setIsDone(() -> true)).whenBecomesFalse(new LambdaCommand().setStart(()->{PedroComponent.follower().setMaxPower(1);}));
        Gamepads.gamepad1().leftBumper().whenBecomesTrue(intakeToSorter);
        Gamepads.gamepad1().rightBumper().whenTrue(IntakeSubsystem.INSTANCE.Outtake).whenBecomesFalse(IntakeSubsystem.INSTANCE.StopIntake);
        Gamepads.gamepad1().options();
        Gamepads.gamepad1().share().whenBecomesTrue(new LambdaCommand().setStart(() -> {StatusSubsystem.INSTANCE.setPrismNorm();}));
        Gamepads.gamepad1().ps().whenBecomesTrue(new LambdaCommand().setStart(() -> {PedroComponent.follower().setPose(new Pose(PedroComponent.follower().getPose().getX(),PedroComponent.follower().getPose().getY(),(3*Math.PI)/2));}).setIsDone(() -> true));;
        Gamepads.gamepad1().touchpad().whenBecomesTrue(eStop);


        Gamepads.gamepad2().circle().whenBecomesTrue(new LaunchGreen());;
        Gamepads.gamepad2().square().whenBecomesTrue(LauncherSubsystem.INSTANCE.SpinUpToSpeed(800));
        Gamepads.gamepad2().triangle().whenBecomesTrue(forceLaunch).whenBecomesFalse(new LambdaCommand().setStart(forceLaunch::cancel).setIsDone(() -> true));
        Gamepads.gamepad2().cross().whenBecomesTrue(new LaunchPurple());;
        Gamepads.gamepad2().dpadUp().whenBecomesTrue(LauncherSubsystem.INSTANCE.SpeedUp);
        Gamepads.gamepad2().dpadDown().whenBecomesTrue(LauncherSubsystem.INSTANCE.SpeedDown);
        Gamepads.gamepad2().dpadLeft();
        Gamepads.gamepad2().dpadRight();
        Gamepads.gamepad2().leftStickX();
        Gamepads.gamepad2().leftStickX();
        Gamepads.gamepad2().leftStickY().lessThan(-0.75).whenBecomesTrue(LauncherSubsystem.INSTANCE.HoodPlus);
        Gamepads.gamepad2().leftStickY().atLeast(0.75).whenBecomesTrue(LauncherSubsystem.INSTANCE.HoodMinus);
        Gamepads.gamepad2().leftStickButton().whenTrue(new TiltCommand());
        Gamepads.gamepad2().rightStickY().lessThan(-0.75);
        Gamepads.gamepad2().rightStickY().atLeast(0.75);
        Gamepads.gamepad2().rightStickButton().toggleOnBecomesTrue().whenBecomesTrue(new LambdaCommand().setStart(()->{TurretSubsystem.INSTANCE.turnTurretOn();})).whenBecomesFalse(new LambdaCommand().setStart(()->{TurretSubsystem.INSTANCE.turnTurretOff();}));;
        Gamepads.gamepad2().leftTrigger().atLeast(0.75).whenBecomesTrue(new LambdaCommand().setStart(intakeToSorter::cancel).setIsDone(() -> true));
        Gamepads.gamepad2().rightTrigger().atLeast(0.75).whenTrue(PTOSubsystem.INSTANCE.disengage);
        Gamepads.gamepad2().leftBumper().whenBecomesTrue(intakeToSorter);
        Gamepads.gamepad2().rightBumper().whenTrue(IntakeSubsystem.INSTANCE.Outtake).whenBecomesFalse(IntakeSubsystem.INSTANCE.StopIntake);
        Gamepads.gamepad2().options();
        Gamepads.gamepad2().share().toggleOnBecomesTrue().whenBecomesTrue(runTurretAndLauncherFromHeadingNickMode).whenBecomesFalse(new LambdaCommand().setStart(runTurretAndLauncherFromHeadingNickMode::cancel));



        Gamepads.gamepad2().touchpad().whenBecomesTrue(eStop);
        Command start = new SequentialGroup(SorterSubsystem.INSTANCE.wake, SorterSubsystem.INSTANCE.resetSorter, PTOSubsystem.INSTANCE.wake, VisionSubsystem.INSTANCE.down);
        start.schedule();

    }

    @Override
    public void onUpdate() {
        Botpose = PedroComponent.follower().getPose();

        if(ActiveOpMode.isStarted() && !ActiveOpMode.isStopRequested()){
            if(Botpose!= null){
                PosStorage.memory.lastPose = PedroComponent.follower().getPose();
            }
            if(!inLift){
                if(Gamepads.gamepad2().ps().get() && deBounce.done()){
                    inLift = true;
//                    joyCommand.schedule();
                    tiltCommand.schedule();
                    deBounce.start();
                }
            }else{
                if((Gamepads.gamepad2().ps().get() || Gamepads.gamepad1().options().get()) && deBounce.done()){
                    inLift = false;
                    tiltCommand.cancel();
                    deBounce.start();
//                    joyCommand.stop(false);
//                    joyCommand.cancel();
                }
            }
//            telemetry.addData("last Pos",  PosStorage.memory.lastPose);
        }
//        telemetry.addData("inLift", inLift);
//        telemetry.addData("BotPose", Botpose);
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void onStop() {

        StatusSubsystem.INSTANCE.setPrismNorm();

    }
}
