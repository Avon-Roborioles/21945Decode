package org.firstinspires.ftc.teamcode.OpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Subsystems.NewLauncher;
import org.firstinspires.ftc.teamcode.Subsystems.TempIntake;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;

@Configurable

@TeleOp
public class LauncherTeleop extends NextFTCOpMode {

    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

    public LauncherTeleop() {

        addComponents(
                new SubsystemComponent(NewLauncher.INSTANCE),
                new PedroComponent(Constants::createFollower),
                new SubsystemComponent(TempIntake.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }
    @Override
    public void onInit(){
        NewLauncher.INSTANCE.build(hardwareMap, true);
        NewLauncher.INSTANCE.initialize();


    }
    @Override
    public void onStartButtonPressed() {
        DriverControlledCommand driverControlled = new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY().negate(),
                Gamepads.gamepad1().leftStickX().negate(),
                Gamepads.gamepad1().rightStickX().negate(),
                false
        );
        driverControlled.schedule();

        Gamepads.gamepad2().b().toggleOnBecomesTrue()
                .whenTrue(NewLauncher.INSTANCE.stop());


    }
    @Override
    public void onUpdate() {
        NewLauncher.INSTANCE.runLauncherFromAprilTag().schedule();
        NewLauncher.INSTANCE.getLauncherTelemetry(telemetry);
        NewLauncher.INSTANCE.getLauncherTelemetry(panelsTelemetry);
        telemetry.update();
        panelsTelemetry.update(telemetry);

    }
}
