package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

import org.firstinspires.ftc.teamcode.Subsystems.CompIntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompLauncherSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompPTOSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompSorterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompStatusSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompTurretSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompVisionSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.LauncherSubsystemGroup;
import org.firstinspires.ftc.teamcode.Utility.Storage;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

public abstract class AutoBase extends Storage {
    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    //test

    {
        addComponents(
            new SubsystemComponent(CompLauncherSubsystem.INSTANCE, CompIntakeSubsystem.INSTANCE, CompSorterSubsystem.INSTANCE, CompPTOSubsystem.INSTANCE,CompTurretSubsystem.INSTANCE, CompVisionSubsystem.INSTANCE,CompStatusSubsystem.INSTANCE ),
//                new SubsystemComponent(CompLauncherSubsystem.INSTANCE, CompIntakeSubsystem.INSTANCE, CompSorterSubsystem.INSTANCE, CompPTOSubsystem.INSTANCE, CompTurretSubsystem.INSTANCE, CompVisionSubsystem.INSTANCE, CompStatusSubsystem.INSTANCE, LauncherSubsystemGroup.INSTANCE),
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }
    @Override public void onUpdate () {
        memory.lastPose = PedroComponent.follower().getPose();

        telemetry.addData("Bot Pose", memory.lastPose);

        telemetry.update();


    }

}
