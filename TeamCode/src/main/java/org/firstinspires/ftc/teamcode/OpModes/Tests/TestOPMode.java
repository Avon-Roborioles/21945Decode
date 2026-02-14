package org.firstinspires.ftc.teamcode.OpModes.Tests;



import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.Commands.PTOJoystickCommand;
import org.firstinspires.ftc.teamcode.Subsystems.PTOSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.StatusSubsystem;
import org.firstinspires.ftc.teamcode.Utility.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp
//@Disabled
@Configurable
public class TestOPMode extends NextFTCOpMode {


    {
        addComponents(
                new SubsystemComponent(PTOSubsystem.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {


    }

    @Override
    public void onWaitForStart() {

    }

    @Override
    public void onStartButtonPressed() {
//        Command joyCommand =  new PTOJoystickCommand(Gamepads.gamepad2().leftStickY(), Gamepads.gamepad2().rightStickY());
//        joyCommand.perpetually();
//        joyCommand.schedule();





    }

    @Override
    public void onUpdate() {

        PTOSubsystem.INSTANCE.Engage();
//        PTOSubsystem.INSTANCE.runLeftFromJoystick(Gamepads.gamepad2().leftStickY());
//        PTOSubsystem.INSTANCE.runRightFromJoystick(Gamepads.gamepad2().rightStickY());



    }

    @Override
    public void onStop() {


    }

}
