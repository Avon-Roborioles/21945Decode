package org.firstinspires.ftc.teamcode.Commands.Launch;

import org.firstinspires.ftc.teamcode.Subsystems.SorterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.Utility.Timing;

import java.util.concurrent.TimeUnit;

import dev.nextftc.core.commands.Command;
import dev.nextftc.ftc.ActiveOpMode;

public class LaunchWithOutSort extends Command {
    enum Step{
        GetReady,
        WaitForReady,
        Ready,
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
    Timing.Timer wait = new Timing.Timer(215, TimeUnit.MILLISECONDS);
    Timing.Timer reset = new Timing.Timer(150, TimeUnit.MILLISECONDS);
    Timing.Timer delay = new Timing.Timer(1000, TimeUnit.MILLISECONDS);
    Timing.Timer ready = new Timing.Timer(300, TimeUnit.MILLISECONDS);


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
        St = Step.GetReady;
        SorterSubsystem.INSTANCE.resetSorter();
        // executed when the command begins
    }

    @Override
    public void update() {
        switch (St) {
            case GetReady:
                SorterSubsystem.INSTANCE.resetSorter();
                ready.start();
                St = Step.WaitForReady;
                break;
            case WaitForReady:
                if(ready.done()){
                    St = Step.Ready;
                }
                break;
            case Ready:
                St = Step.LaunchCenter;

                break;
            case LaunchCenter:
                if(!(SorterSubsystem.INSTANCE.centerSlot() == SorterSubsystem.SlotDetection.EMPTY)){
                    if(TurretSubsystem.INSTANCE.turretFine()) {
                        SorterSubsystem.INSTANCE.sendCenter();
                        wait.start();
                        St = Step.WaitCenter;
                    }else{
                        ActiveOpMode.gamepad1().rumble(100);
                    }
                } else {
                    St = Step.LaunchLeft;
                }
                break;
            case WaitCenter:
                if(wait.done()){
                    SorterSubsystem.INSTANCE.resetSorter();
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
                if(!(SorterSubsystem.INSTANCE.leftSlot() == SorterSubsystem.SlotDetection.EMPTY)){
                    if(TurretSubsystem.INSTANCE.turretFine()) {
                        SorterSubsystem.INSTANCE.sendLeft();
                        wait.start();
                        St = Step.WaitLeft;
                    }else{
                        ActiveOpMode.gamepad1().rumble(100);
                    }
                } else {
                    St = Step.LaunchRight;

                }
                break;
            case WaitLeft:
                if(wait.done()){
                    SorterSubsystem.INSTANCE.resetSorter();
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
                if(!(SorterSubsystem.INSTANCE.rightSlot() == SorterSubsystem.SlotDetection.EMPTY)){
                    if(TurretSubsystem.INSTANCE.turretFine()) {
                        SorterSubsystem.INSTANCE.sendRight();
                        wait.start();
                        St = Step.WaitRight;
                    }else{
                        ActiveOpMode.gamepad1().rumble(100);
                    }
                } else {
                    St = Step.Pause;
                }
                break;
            case WaitRight:
                if(wait.done()){
                    SorterSubsystem.INSTANCE.resetSorter();
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
                if(!(SorterSubsystem.INSTANCE.leftSlot() == SorterSubsystem.SlotDetection.EMPTY) || !(SorterSubsystem.INSTANCE.centerSlot() == SorterSubsystem.SlotDetection.EMPTY) || !(SorterSubsystem.INSTANCE.rightSlot() == SorterSubsystem.SlotDetection.EMPTY)){
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
        SorterSubsystem.INSTANCE.resetSorter();
        // executed when the command ends
    }
}
