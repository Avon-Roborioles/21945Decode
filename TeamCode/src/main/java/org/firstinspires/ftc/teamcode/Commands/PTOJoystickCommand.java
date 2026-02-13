package org.firstinspires.ftc.teamcode.Commands;

import org.firstinspires.ftc.teamcode.Subsystems.PTOSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;

import dev.nextftc.bindings.Range;
import dev.nextftc.core.commands.Command;
import dev.nextftc.extensions.pedro.PedroComponent;

public class PTOJoystickCommand extends Command {
    Range inputL, inputR;

    public PTOJoystickCommand(Range inputL, Range inputR) {
        this.inputL = inputL;
        this.inputR = inputR;
        requires();
        setInterruptible(true);
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
        PTOSubsystem.INSTANCE.runLeftFromJoystick(inputL);
        PTOSubsystem.INSTANCE.runRightFromJoystick(inputR);
        PedroComponent.follower().breakFollowing();
        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {

        // executed when the command ends
    }
}

