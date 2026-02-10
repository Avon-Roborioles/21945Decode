package org.firstinspires.ftc.teamcode.OpModes.Tests;



import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.Subsystems.StatusSubsystem;
import org.firstinspires.ftc.teamcode.Utility.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp
//@Disabled
@Configurable
public class TestOPMode extends NextFTCOpMode {
    public static long pwmTarget = 1000;

    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();


    {
        addComponents(
                new SubsystemComponent( StatusSubsystem.INSTANCE),
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
        StatusSubsystem.INSTANCE.setPrismNorm();


    }

    @Override
    public void onStartButtonPressed() {




    }

    @Override
    public void onUpdate() {



        StatusSubsystem.INSTANCE.setPrismToPWM(pwmTarget);

    }

    @Override
    public void onStop() {
        StatusSubsystem.INSTANCE.returnToDefault();
        StatusSubsystem.INSTANCE.prism.clearAllAnimations();
        StatusSubsystem.INSTANCE.prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);
        telemetry.addLine("Done");


    }

}
