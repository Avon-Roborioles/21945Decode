package org.firstinspires.ftc.teamcode.Commands;

import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.SorterSubsystem;
import org.firstinspires.ftc.teamcode.Utility.Timing;

import java.util.concurrent.TimeUnit;

import dev.nextftc.core.commands.Command;

public class AutoIntakeNoTime extends Command {
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
    public AutoIntakeNoTime() {
        requires(IntakeSubsystem.INSTANCE/* subsystems */);
        setInterruptible(true); // this is the default, so you don't need to specify
    }


    @Override
    public boolean isDone() {
        return step == intakeSeq.Done;// whether or not the command is done
    }

    @Override
    public void start() {
        step = intakeSeq.Intake;

        SorterSubsystem.INSTANCE.resetSorter();
        IntakeSubsystem.INSTANCE.intake();

        // executed when the command begins
    }

    @Override
    public void update() {
        switch (step) {
            case Intake:
                IntakeSubsystem.INSTANCE.intake();
                step = intakeSeq.CheckForFull;
                break;
            case CheckForFull:
                if (SorterSubsystem.INSTANCE.sorterFull()) {
                    step = intakeSeq.Hug;
                }
                break;
            case Hug:
                SorterSubsystem.INSTANCE.sortHug();
                step = intakeSeq.StopIntake;
                break;
            case StopIntake:
                IntakeSubsystem.INSTANCE.stopIntake();
                step = intakeSeq.CheckBB;
                break;
            case CheckBB:
                IntakeSubsystem.INSTANCE.outtake();
                if (IntakeSubsystem.INSTANCE.intakeBBTripped()) {
                    step = intakeSeq.Outake;
                }else{
                    step = intakeSeq.ReleaseHug;
                    IntakeSubsystem.INSTANCE.stopIntake();
                }
                break;
            case Outake:
                IntakeSubsystem.INSTANCE.outtake();
                if(!IntakeSubsystem.INSTANCE.intakeBBTripped()){
                    step = intakeSeq.ReleaseHug;
                }
                break;
            case ReleaseHug:
                SorterSubsystem.INSTANCE.resetSorter();
                step = intakeSeq.Wait;
                wait.start();
                break;
            case Wait:
                if (wait.done()) {
                    step = intakeSeq.CheckForFullAgain;
                }
                break;
            case CheckForFullAgain:
                if (SorterSubsystem.INSTANCE.sorterFull()) {
                    step = intakeSeq.HugAgain;
                    IntakeSubsystem.INSTANCE.outtake();
                }else{
                    IntakeSubsystem.INSTANCE.intake();
                }
                break;
            case HugAgain:
                SorterSubsystem.INSTANCE.sortHug();
                IntakeSubsystem.INSTANCE.outtake();
                step = intakeSeq.Done;
                break;
        }

        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {
        if (!interrupted) {
            SorterSubsystem.INSTANCE.sortHug();
        }


        IntakeSubsystem.INSTANCE.stopIntake();


        // executed when the command ends
    }
}
