package org.firstinspires.ftc.teamcode.OpModes;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Subsystems.NewLauncher;
import org.firstinspires.ftc.teamcode.Subsystems.SorterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TempIntake;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.commands.groups.SequentialGroup;
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
                new SubsystemComponent(NewLauncher.INSTANCE,SorterSubsystem.INSTANCE),


//                new PedroComponent(Constants::createFollower),
//                new SubsystemComponent(TempIntake.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }
    @Override
    public void onInit(){
        NewLauncher.INSTANCE.build(hardwareMap, true);
        NewLauncher.INSTANCE.initialize();
        SorterSubsystem.INSTANCE.initialize();


    }
    @Override
    public void onStartButtonPressed() {
//        DriverControlledCommand driverControlled = new PedroDriverControlled(
//                Gamepads.gamepad1().leftStickY().negate(),
//                Gamepads.gamepad1().leftStickX().negate(),
//                Gamepads.gamepad1().rightStickX().negate(),
//                false
//        );
//        driverControlled.schedule();
        Gamepads.gamepad2().a().whenBecomesTrue(new SequentialGroup( SorterSubsystem.INSTANCE.ejectOne.setRequirements(SorterSubsystem.INSTANCE), SorterSubsystem.INSTANCE.resetSorter.setRequirements(SorterSubsystem.INSTANCE)));
        Gamepads.gamepad2().x().whenBecomesTrue(new SequentialGroup( SorterSubsystem.INSTANCE.ejectTwo.setRequirements(SorterSubsystem.INSTANCE), SorterSubsystem.INSTANCE.resetSorter.setRequirements(SorterSubsystem.INSTANCE)));
        Gamepads.gamepad2().y().whenBecomesTrue(new SequentialGroup( SorterSubsystem.INSTANCE.ejectThree.setRequirements(SorterSubsystem.INSTANCE), SorterSubsystem.INSTANCE.resetSorter.setRequirements(SorterSubsystem.INSTANCE)));
        Gamepads.gamepad2().b().whenBecomesTrue(new SequentialGroup( SorterSubsystem.INSTANCE.ejectOne, SorterSubsystem.INSTANCE.resetSorter,SorterSubsystem.INSTANCE.ejectTwo, SorterSubsystem.INSTANCE.resetSorter,SorterSubsystem.INSTANCE.ejectThree, SorterSubsystem.INSTANCE.resetSorter));
        Gamepads.gamepad2().dpadUp().whenBecomesTrue(NewLauncher.INSTANCE.hoodPlus);
        Gamepads.gamepad2().dpadDown().whenBecomesTrue(NewLauncher.INSTANCE.hoodMinus);
        Gamepads.gamepad2().leftBumper().whenBecomesTrue(NewLauncher.INSTANCE.speedUp);
        Gamepads.gamepad2().rightBumper().whenBecomesTrue(NewLauncher.INSTANCE.speedDown);
        Gamepads.gamepad2().back().toggleOnBecomesTrue()
                .whenTrue(NewLauncher.INSTANCE.stop());


    }
    @Override
    public void onUpdate() {



        NewLauncher.INSTANCE.getLauncherTelemetry(telemetry);
        NewLauncher.INSTANCE.getLauncherTelemetry(panelsTelemetry);
        telemetry.update();
        panelsTelemetry.update(telemetry);

    }
}
