package org.firstinspires.ftc.teamcode.Commands.Intake;

import org.firstinspires.ftc.teamcode.Subsystems.CompIntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompSorterSubsystem;
import org.firstinspires.ftc.teamcode.Utility.Timing;

import java.util.concurrent.TimeUnit;

import dev.nextftc.core.commands.Command;

public class AutoIntake extends Command {
    enum intakeSeq {
        Intake,
        CheckForFull,
        Hug,
        StopIntake,
        CheckBB,
        Outake,
        ReleaseHug,
        Wait,
        CheckForFullAgain,
        HugAgain,
        Done

    }
    intakeSeq step;
    Timing.Timer wait = new Timing.Timer(250, TimeUnit.MILLISECONDS);
    public AutoIntake(long Time) {
        end = new Timing.Timer(Time, TimeUnit.MILLISECONDS);
        requires(CompIntakeSubsystem.INSTANCE/* subsystems */);
        setInterruptible(true); // this is the default, so you don't need to specify
    }

    Timing.Timer end;

    @Override
    public boolean isDone() {return step == intakeSeq.Done|| end.done();// whether or not the command is done
    }

    @Override
    public void start() {
        end.start();
        step =intakeSeq.Intake;

        CompSorterSubsystem.INSTANCE.resetSorter();

        // executed when the command begins
    }

    @Override
    public void update() {
        switch (step) {
            case Intake:
                CompIntakeSubsystem.INSTANCE.intake();
                step = intakeSeq.CheckForFull;
                break;
            case CheckForFull:
                if (CompSorterSubsystem.INSTANCE.sorterFull()) {
                    step = intakeSeq.Hug;
                }
                break;
            case Hug:
                CompSorterSubsystem.INSTANCE.sortHug();
                step = intakeSeq.StopIntake;
                break;
            case StopIntake:
                CompIntakeSubsystem.INSTANCE.stopIntake();
                step = intakeSeq.CheckBB;
                break;
            case CheckBB:
                CompIntakeSubsystem.INSTANCE.outtake();
                if (CompIntakeSubsystem.INSTANCE.intakeBBTripped()) {
                    step = intakeSeq.Outake;
                }else{
                    step = intakeSeq.ReleaseHug;
                    CompIntakeSubsystem.INSTANCE.stopIntake();
                }
                break;
            case Outake:
                CompIntakeSubsystem.INSTANCE.outtake();
                if(!CompIntakeSubsystem.INSTANCE.intakeBBTripped()){
                    step = intakeSeq.ReleaseHug;
                }
                break;
            case ReleaseHug:
                CompSorterSubsystem.INSTANCE.resetSorter();
                step = intakeSeq.Wait;
                wait.start();
                break;
            case Wait:
                if (wait.done()) {
                    step = intakeSeq.CheckForFullAgain;
                }
                break;
            case CheckForFullAgain:
                if (CompSorterSubsystem.INSTANCE.sorterFull()) {
                    step = intakeSeq.HugAgain;
                    CompIntakeSubsystem.INSTANCE.outtake();
                }else{
                    CompIntakeSubsystem.INSTANCE.intake();
                }
                break;
            case HugAgain:
                CompSorterSubsystem.INSTANCE.sortHug();
                CompIntakeSubsystem.INSTANCE.outtake();
                step = intakeSeq.Done;
                break;
        }

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
