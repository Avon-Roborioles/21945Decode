package org.firstinspires.ftc.teamcode.Commands.Launch;

import org.firstinspires.ftc.teamcode.Subsystems.SorterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.StatusSubsystem;
import org.firstinspires.ftc.teamcode.Utility.Timing;

import java.util.concurrent.TimeUnit;

import dev.nextftc.core.commands.Command;
import dev.nextftc.ftc.ActiveOpMode;

public class LaunchPurple extends Command {
    enum Step{
        GetReady,
        WaitForReady,
        Ready,
        Launch,
        Wait,
        Reset,
        CheckForMiss,
        Done

    }

    private enum Slot {
        Left,
        Right,
        Center,
        Null
    }

    Step St = Step.Launch;
    Slot Launched = Slot.Null;
    SorterSubsystem.SlotDetection left, right, center;


    Timing.Timer wait = new Timing.Timer(250, TimeUnit.MILLISECONDS);
    Timing.Timer reset = new Timing.Timer(250, TimeUnit.MILLISECONDS);
    Timing.Timer ready = new Timing.Timer(250, TimeUnit.MILLISECONDS);

    public LaunchPurple() {
        requires(/* subsystems */);
        setInterruptible(true); // this is the default, so you don't need to specify
    }

    @Override
    public boolean isDone() {
        return St == Step.Done; // whether or not the command is done
    }

    @Override
    public void start() {
        SorterSubsystem.INSTANCE.resetSorter();
        St = Step.GetReady;

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
                left = SorterSubsystem.INSTANCE.leftSlot();
                right = SorterSubsystem.INSTANCE.rightSlot();
                center = SorterSubsystem.INSTANCE.centerSlot();
                St = Step.Launch;

                break;
            case Launch:
                launchPurple();
                St = Step.Wait;
                break;
            case Wait:
                if(wait.done()){
                    St = Step.Reset;
                    reset.start();
                }
                break;
            case Reset:
                SorterSubsystem.INSTANCE.resetSorter();
                if(reset.done()){
                    St = Step.CheckForMiss;
                }
                break;
            case CheckForMiss:
                switch (Launched){
                    case Left:
                        if(SorterSubsystem.INSTANCE.leftSlot() == SorterSubsystem.SlotDetection.EMPTY){
                            St = Step.Done;
                        }else {
                            St = Step.Launch;
                        }
                        break;
                    case Right:
                        if(SorterSubsystem.INSTANCE.rightSlot() == SorterSubsystem.SlotDetection.EMPTY){
                            St = Step.Done;
                        }else {
                            St = Step.Launch;
                        }
                        break;
                    case Center:
                        if(SorterSubsystem.INSTANCE.centerSlot() == SorterSubsystem.SlotDetection.EMPTY){
                            St = Step.Done;
                        }else {
                            St = Step.Launch;
                        }
                        break;

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

    private void launchPurple(){
        if(center == SorterSubsystem.SlotDetection.PURPLE){
            SorterSubsystem.INSTANCE.sendCenter();
            Launched = Slot.Center;
            wait.start();
        }else if (left == SorterSubsystem.SlotDetection.PURPLE){
            SorterSubsystem.INSTANCE.sendLeft();
            Launched = Slot.Left;
            wait.start();
        }else if (right == SorterSubsystem.SlotDetection.PURPLE){
            SorterSubsystem.INSTANCE.sendRight();
            Launched = Slot.Right;
            wait.start();
        }else if (center == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendCenter();
            Launched = Slot.Center;
            wait.start();
        }else if (left == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendLeft();
            Launched = Slot.Left;
            wait.start();
        }else if (right == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendRight();
            Launched = Slot.Right;
            wait.start();
        }else if (!(center == SorterSubsystem.SlotDetection.GREEN)){
            SorterSubsystem.INSTANCE.sendCenter();
            Launched = Slot.Center;
            wait.start();
        }else if (!(left == SorterSubsystem.SlotDetection.GREEN)){
            SorterSubsystem.INSTANCE.sendLeft();
            Launched = Slot.Left;
            wait.start();
        }else if (!(right == SorterSubsystem.SlotDetection.GREEN)){
            SorterSubsystem.INSTANCE.sendRight();
            Launched = Slot.Right;
            wait.start();
        }
    }

}
