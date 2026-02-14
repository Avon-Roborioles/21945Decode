package org.firstinspires.ftc.teamcode.Commands.Turret;

import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;

import dev.nextftc.bindings.Range;
import dev.nextftc.core.commands.Command;

public class TurretJoystickCommand extends Command {
    Range input, input2;

    public TurretJoystickCommand(Range input, Range input2 ) {
        this.input = input;
        this.input2 = input2;
        requires(TurretSubsystem.INSTANCE);
        setInterruptible(true);
    }
    @Override
    public boolean isDone() {
        return false; // whether or not the command is done
    }

    @Override
    public void start() {

        // executed when the command begins
    }

    @Override
    public void update() {
        TurretSubsystem.INSTANCE.moveTurretJoystick(input.get(), input2.get());
        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {

        // executed when the command ends
    }
}

