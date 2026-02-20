package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.LauncherSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.PTOSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.SorterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.StatusSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.VisionSubsystem;
import org.firstinspires.ftc.teamcode.Utility.PosStorage;
import org.firstinspires.ftc.teamcode.Utility.Storage;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

public abstract class AutoBase extends Storage {
    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    //test

    {
        addComponents(
            new SubsystemComponent(LauncherSubsystem.INSTANCE, IntakeSubsystem.INSTANCE, SorterSubsystem.INSTANCE, PTOSubsystem.INSTANCE, TurretSubsystem.INSTANCE, VisionSubsystem.INSTANCE, StatusSubsystem.INSTANCE ),
//                new SubsystemComponent(CompLauncherSubsystem.INSTANCE, CompIntakeSubsystem.INSTANCE, CompSorterSubsystem.INSTANCE, CompPTOSubsystem.INSTANCE, CompTurretSubsystem.INSTANCE, CompVisionSubsystem.INSTANCE, CompStatusSubsystem.INSTANCE, LauncherSubsystemGroup.INSTANCE),
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }


    @Override public void onUpdate () {
        StatusSubsystem.INSTANCE.setPrismToPWM(965);
        if(ActiveOpMode.isStarted() && !ActiveOpMode.isStopRequested()){
            if(PedroComponent.follower().getPose() != null){
                PosStorage.memory.lastPose = PedroComponent.follower().getPose();
            }
            telemetry.addData("last Pos",  PosStorage.memory.lastPose);
        }
        telemetry.update();


    }

}
