package org.firstinspires.ftc.teamcode.Commands;

import org.firstinspires.ftc.teamcode.Subsystems.CompSorterSubsystem;
import org.firstinspires.ftc.teamcode.Utility.Timing;

import java.util.concurrent.TimeUnit;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.CommandGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.ftc.ActiveOpMode;

public class LaunchWithOutSort extends Command {
    enum Step{
        LaunchCenter,
        WaitCenter,
        ResetCenter,
        LaunchLeft,
        WaitLeft,
        ResetLeft,
        LaunchRight,
        ResetRight,
        WaitRight,
        Pause,
        CheckForMiss,
        Done

    }

    Step St = Step.LaunchCenter;
    Timing.Timer wait = new Timing.Timer(250, TimeUnit.MILLISECONDS);
    Timing.Timer reset = new Timing.Timer(200, TimeUnit.MILLISECONDS);
    Timing.Timer delay = new Timing.Timer(1000, TimeUnit.MILLISECONDS);

    public LaunchWithOutSort() {
        requires(/* subsystems */);
        setInterruptible(true); // this is the default, so you don't need to specify
    }

    @Override
    public boolean isDone() {
        return St == Step.Done; // whether or not the command is done
    }

    @Override
    public void start() {
        St = Step.LaunchCenter;
        // executed when the command begins
    }

    @Override
    public void update() {
        switch (St) {
            case LaunchCenter:
                if(!(CompSorterSubsystem.INSTANCE.centerSlot() == CompSorterSubsystem.SlotDetection.EMPTY)){
                    CompSorterSubsystem.INSTANCE.sendCenter();
                    wait.start();
                    St = Step.WaitCenter;
                } else {
                    St = Step.LaunchLeft;
                }
                break;
            case WaitCenter:
                if(wait.done()){
                    CompSorterSubsystem.INSTANCE.resetSorter();
                    reset.start();
                    St = Step.ResetCenter;
                }
                break;
            case ResetCenter:
                if(reset.done()){
                    St = Step.LaunchLeft;
                }
                break;
            case LaunchLeft:
                if(!(CompSorterSubsystem.INSTANCE.leftSlot() == CompSorterSubsystem.SlotDetection.EMPTY)){
                    CompSorterSubsystem.INSTANCE.sendLeft();
                    wait.start();
                    St = Step.WaitLeft;
                } else {
                    St = Step.LaunchRight;

                }
                break;
            case WaitLeft:
                if(wait.done()){
                    CompSorterSubsystem.INSTANCE.resetSorter();
                    reset.start();
                    St = Step.ResetLeft;
                }
                break;
            case ResetLeft:
                if(reset.done()){
                    St = Step.LaunchRight;
                }
                break;
            case LaunchRight:
                if(!(CompSorterSubsystem.INSTANCE.rightSlot() == CompSorterSubsystem.SlotDetection.EMPTY)){
                    CompSorterSubsystem.INSTANCE.sendRight();
                    wait.start();
                    St = Step.WaitRight;
                } else {
                    St = Step.Pause;
                }
                break;
            case WaitRight:
                if(wait.done()){
                    CompSorterSubsystem.INSTANCE.resetSorter();
                    reset.start();
                    St = Step.ResetRight;
                }
                break;
            case ResetRight:
                if(reset.done()){
                    St = Step.Pause;
                }
                break;
            case Pause:
                if(!delay.isTimerOn()) {
                    delay.start();
                }
                if(delay.done()) {
                    St = Step.CheckForMiss;
                }
                break;
            case CheckForMiss:
                if(!(CompSorterSubsystem.INSTANCE.leftSlot() == CompSorterSubsystem.SlotDetection.EMPTY) || !(CompSorterSubsystem.INSTANCE.centerSlot() == CompSorterSubsystem.SlotDetection.EMPTY) || !(CompSorterSubsystem.INSTANCE.rightSlot() == CompSorterSubsystem.SlotDetection.EMPTY)){
                    St = Step.LaunchCenter;
                }else {
                    St = Step.Done;
                }
                break;
        }
        // executed on every update of the command
        ActiveOpMode.telemetry().addData("Step", St);

    }

    @Override
    public void stop(boolean interrupted) {
        // executed when the command ends
    }
}
