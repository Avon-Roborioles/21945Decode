package org.firstinspires.ftc.teamcode.OpModes.Tests;



import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.Commands.Intake.IntakeToSorterCommand;
import org.firstinspires.ftc.teamcode.Commands.Launch.LaunchWithOutSort;
import org.firstinspires.ftc.teamcode.Subsystems.CompIntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompLauncherSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompPTOSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompSorterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompStatusSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompTurretSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompVisionSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.LauncherSubsystemGroup;
import org.firstinspires.ftc.teamcode.Utility.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;

@TeleOp
//@Disabled
@Configurable
public class TestOPMode extends NextFTCOpMode {
    public static long pwmTarget = 1000;

    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();


    {
        addComponents(
                new SubsystemComponent( CompStatusSubsystem.INSTANCE),
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {
        PedroComponent.follower().setPose(new Pose(72,72,270));



    }

    @Override
    public void onWaitForStart() {
        panelsTelemetry.update(telemetry);
        CompStatusSubsystem.INSTANCE.setPrismNorm();


    }

    @Override
    public void onStartButtonPressed() {




    }

    @Override
    public void onUpdate() {



        CompStatusSubsystem.INSTANCE.setPrismToPWM(pwmTarget);

    }

    @Override
    public void onStop() {
        CompStatusSubsystem.INSTANCE.returnToDefault();
        CompStatusSubsystem.INSTANCE.prism.clearAllAnimations();
        CompStatusSubsystem.INSTANCE.prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);
        telemetry.addLine("Done");


    }

}
