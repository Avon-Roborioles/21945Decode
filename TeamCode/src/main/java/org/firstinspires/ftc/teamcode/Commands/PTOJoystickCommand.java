package org.firstinspires.ftc.teamcode.Commands;

import org.firstinspires.ftc.teamcode.Subsystems.PTOSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;

import dev.nextftc.bindings.Range;
import dev.nextftc.core.commands.Command;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;

public class PTOJoystickCommand extends Command {
    Range inputL, inputR;

    public PTOJoystickCommand(Range inputL, Range inputR) {
        this.inputL = inputL;
        this.inputR = inputR;
    }
    @Override
    public boolean isDone() {
        return false; // whether or not the command is done
    }

    @Override
    public void start() {
        PTOSubsystem.INSTANCE.Engage();
        PedroComponent.follower().breakFollowing();



        // executed when the command begins
    }

    @Override
    public void update() {

        PedroComponent.follower().getDrivetrain().runDrive(new double[]{inputL.get(), inputL.get(), inputR.get(), inputR.get()});

        ActiveOpMode.telemetry().addLine("-------------- PTO Joystick Command: --------------");
        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {
        PedroComponent.follower().getDrivetrain().runDrive(new double[]{0, 0,0,0});
        PedroComponent.follower().breakFollowing();
        PTOSubsystem.INSTANCE.Disengage();
        PedroComponent.follower().startTeleOpDrive();

        // executed when the command ends
    }
}

