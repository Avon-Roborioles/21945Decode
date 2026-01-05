package org.firstinspires.ftc.teamcode.Commands;

import org.firstinspires.ftc.teamcode.Subsystems.CompIntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompSorterSubsystem;

import dev.nextftc.core.commands.Command;


public class IntakeToSorterCommand extends Command {
    public IntakeToSorterCommand() {
        requires(CompIntakeSubsystem.INSTANCE, CompSorterSubsystem.INSTANCE/* subsystems */);
        setInterruptible(true); // this is the default, so you don't need to specify
    }

    @Override
    public boolean isDone() {
        return CompSorterSubsystem.INSTANCE.sorterFull(); // whether or not the command is done
    }

    @Override
    public void start() {

        // executed when the command begins
    }

    @Override
    public void update() {
        CompIntakeSubsystem.INSTANCE.intake();
        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {
        CompIntakeSubsystem.INSTANCE.stopIntake();
        // executed when the command ends
    }
}
