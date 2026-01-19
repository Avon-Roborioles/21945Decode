package org.firstinspires.ftc.teamcode.Commands;

import org.firstinspires.ftc.teamcode.Subsystems.CompIntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompSorterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompStatusSubsystem;

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
        CompSorterSubsystem.INSTANCE.resetSorter();



        // executed when the command begins
    }

    @Override
    public void update() {
        if(!full) {
            CompIntakeSubsystem.INSTANCE.intake();
            full = CompSorterSubsystem.INSTANCE.sorterFull();
        }else{
            if(CompIntakeSubsystem.INSTANCE.intakeBBTripped()){
                CompSorterSubsystem.INSTANCE.sortHug();
                CompIntakeSubsystem.INSTANCE.outtake();
            }else{
                done = true;
            }

//            CompSorterSubsystem.INSTANCE.light();
        }
        CompSorterSubsystem.INSTANCE.updateColor();
//        CompSorterSubsystem.INSTANCE.getSorterTelemetryAdv();

        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {
        if (!interrupted) {
            CompSorterSubsystem.INSTANCE.sortHug();
        }

        CompIntakeSubsystem.INSTANCE.stopIntake();
//        CompStatusSubsystem.INSTANCE.updatePrism();

        // executed when the command ends
    }
}
