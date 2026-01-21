package org.firstinspires.ftc.teamcode.Commands.Intake;

import org.firstinspires.ftc.teamcode.Subsystems.CompIntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompSorterSubsystem;

import dev.nextftc.core.commands.Command;

public class AutoIntake extends Command {
    public AutoIntake() {
        requires(CompIntakeSubsystem.INSTANCE/* subsystems */);
        setInterruptible(true); // this is the default, so you don't need to specify
    }

    @Override
    public boolean isDone() {return CompSorterSubsystem.INSTANCE.sorterFull();// whether or not the command is done
    }

    @Override
    public void start() {
        CompSorterSubsystem.INSTANCE.resetSorter();

        CompIntakeSubsystem.INSTANCE.intake();

        // executed when the command begins
    }

    @Override
    public void update() {

        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {
        if (!interrupted) {
            CompSorterSubsystem.INSTANCE.sortHug();
        }

        CompIntakeSubsystem.INSTANCE.stopIntake();

        // executed when the command ends
    }
}
