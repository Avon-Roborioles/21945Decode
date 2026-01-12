package org.firstinspires.ftc.teamcode.Commands;

import org.firstinspires.ftc.teamcode.Subsystems.CompTurretSubsystem;

import java.util.function.DoubleSupplier;

import dev.nextftc.bindings.Range;
import dev.nextftc.core.commands.Command;

public class TurretJoystickCommand extends Command {
    Range input;

    public TurretJoystickCommand(Range input) {
        this.input = input;
        requires(CompTurretSubsystem.INSTANCE);
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
        CompTurretSubsystem.INSTANCE.moveTurretJoystick(input.get());
        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {

        // executed when the command ends
    }
}

