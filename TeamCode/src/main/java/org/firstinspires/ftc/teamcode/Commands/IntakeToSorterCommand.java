package org.firstinspires.ftc.teamcode.Commands;

import org.firstinspires.ftc.teamcode.Subsystems.CompIntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompSorterSubsystem;

import dev.nextftc.core.commands.Command;


public class IntakeToSorterCommand extends Command {
    boolean full = false;
    boolean done = false;
    boolean beamBreakClear = false;
    public IntakeToSorterCommand() {
        requires(CompIntakeSubsystem.INSTANCE, CompSorterSubsystem.INSTANCE/* subsystems */);
        setInterruptible(true); // this is the default, so you don't need to specify
    }

    @Override
    public boolean isDone() {
        return done; // whether or not the command is done
    }

    @Override
    public void start() {
        full = false;
        done = false;


        // executed when the command begins
    }

    @Override
    public void update() {
        if(!full) {
            CompIntakeSubsystem.INSTANCE.intake();
            full = CompSorterSubsystem.INSTANCE.sorterFull();
        }else{
            CompSorterSubsystem.INSTANCE.sortHug();
            if(CompIntakeSubsystem.INSTANCE.intakeBBTripped()){
                CompIntakeSubsystem.INSTANCE.outtake();
            }else{
                done = true;
            }
        }

        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {
        CompIntakeSubsystem.INSTANCE.stopIntake();
        CompSorterSubsystem.INSTANCE.resetSorter();
        // executed when the command ends
    }
}
