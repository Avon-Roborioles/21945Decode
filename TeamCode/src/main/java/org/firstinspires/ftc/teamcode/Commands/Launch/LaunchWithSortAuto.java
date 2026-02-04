package org.firstinspires.ftc.teamcode.Commands.Launch;

import org.firstinspires.ftc.teamcode.Subsystems.CompSorterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompStatusSubsystem;
import org.firstinspires.ftc.teamcode.Utility.Timing;

import java.util.concurrent.TimeUnit;

import dev.nextftc.core.commands.Command;
import dev.nextftc.ftc.ActiveOpMode;

public class LaunchWithSortAuto extends Command {
    enum Step{
        GetReady,
        WaitForReady,
        Ready,
        LaunchFirst,
        WaitFirst,
        ResetFirst,
        LaunchMiddle,
        WaitMiddle,
        ResetMiddle,
        LaunchLast,
        ResetLast,
        WaitLast,
        CheckForMissLast,
        Done

    }
    private enum Color {
        PURPLE,
        GREEN
    }
    private enum Slot {
        Left,
        Right,
        Center,
        Null
    }




    CompSorterSubsystem.SlotDetection left, right, center;
    CompStatusSubsystem.OBPattern sorterPattern;
    Color first, middle, last;
    Color leftSlotSet, rightSlotSet, centerSlotSet;




    Step St = Step.LaunchFirst;
    Slot firstLaunch = Slot.Null;

    Slot middleLaunch = Slot.Null;
    Slot lastLaunch = Slot.Null;

    Timing.Timer wait = new Timing.Timer(205, TimeUnit.MILLISECONDS);
    Timing.Timer reset = new Timing.Timer(150, TimeUnit.MILLISECONDS);
    Timing.Timer delay = new Timing.Timer(1000, TimeUnit.MILLISECONDS);
    Timing.Timer ready = new Timing.Timer(300, TimeUnit.MILLISECONDS);


    public LaunchWithSortAuto() {
        requires(/* subsystems */);
        setInterruptible(true); // this is the default, so you don't need to specify
    }

    @Override
    public boolean isDone() {
        return St == Step.Done; // whether or not the command is done
    }

    @Override
    public void start() {
        CompSorterSubsystem.INSTANCE.resetSorter();
        St = Step.GetReady;
        firstLaunch = Slot.Null;


        sorterPattern = CompStatusSubsystem.INSTANCE.getCurrentOBPattern();
        switch (sorterPattern){
            case PPG:
                first = Color.PURPLE;
                middle = Color.PURPLE;
                last = Color.GREEN;
                break;
            case PGP:
                first = Color.PURPLE;
                middle = Color.GREEN;
                last = Color.PURPLE;
                break;
            case GPP:
                first = Color.GREEN;
                middle = Color.PURPLE;
                last = Color.PURPLE;
                break;
            case NULL:
                first = Color.PURPLE;
                middle = Color.PURPLE;
                last = Color.GREEN;
                break;

        }
    }

    @Override
    public void update() {


        switch (St) {
            case GetReady:
                CompSorterSubsystem.INSTANCE.resetSorter();
                ready.start();
                St = Step.WaitForReady;
                break;
            case WaitForReady:
                if(ready.done()){
                    St = Step.Ready;
                }
                break;
            case Ready:
                left = CompSorterSubsystem.INSTANCE.leftSlot();
                right = CompSorterSubsystem.INSTANCE.rightSlot();
                center = CompSorterSubsystem.INSTANCE.centerSlot();

                if (left == CompSorterSubsystem.SlotDetection.GREEN){
                    leftSlotSet = Color.GREEN;
                    rightSlotSet = Color.PURPLE;
                    centerSlotSet = Color.PURPLE;
                }else if(right == CompSorterSubsystem.SlotDetection.GREEN){
                    leftSlotSet = Color.PURPLE;
                    rightSlotSet = Color.GREEN;
                    centerSlotSet = Color.PURPLE;
                }else{
                    leftSlotSet = Color.PURPLE;
                    rightSlotSet = Color.PURPLE;
                    centerSlotSet = Color.GREEN;
                }
                St = Step.LaunchFirst;

                break;
            case LaunchFirst:
                if(first == Color.PURPLE){
                    launchPurpleFirst();
                    St = Step.WaitFirst;
                }else if (first == Color.GREEN){
                    launchGreenFirst();
                    St = Step.WaitFirst;
                }
                break;
            case WaitFirst:
                if(wait.done()){
                    St = Step.ResetFirst;
                    reset.start();
                }
                break;
            case ResetFirst:
                CompSorterSubsystem.INSTANCE.resetSorter();
                if(reset.done()){
                    St = Step.LaunchMiddle;
                }
                break;
            case LaunchMiddle:
                if(middle == Color.PURPLE){
                    launchPurpleMiddle();
                    St = Step.WaitMiddle;
                }else if (middle == Color.GREEN){
                    launchGreenMiddle();
                    St = Step.WaitMiddle;
                }
                break;
            case WaitMiddle:
                if(wait.done()){
                    St = Step.ResetMiddle;
                    reset.start();
                }
                break;
            case ResetMiddle:
                CompSorterSubsystem.INSTANCE.resetSorter();
                if(reset.done()){
                    St = Step.LaunchLast;
                }
                break;

            case LaunchLast:
                if(last == Color.PURPLE){
                    launchPurpleLast();
                    St = Step.WaitLast;
                }else if (last == Color.GREEN){
                    launchGreenLast();
                    St = Step.WaitLast;
                }
                break;
            case WaitLast:
                if(wait.done()){
                    St = Step.ResetLast;
                    reset.start();
                }
                break;
            case ResetLast:
                CompSorterSubsystem.INSTANCE.resetSorter();
                if(reset.done()){
                    St = Step.CheckForMissLast;
                }
                break;
            case CheckForMissLast:
                left = CompSorterSubsystem.INSTANCE.leftSlot();
                right = CompSorterSubsystem.INSTANCE.rightSlot();
                center = CompSorterSubsystem.INSTANCE.centerSlot();
                if(!(left == CompSorterSubsystem.SlotDetection.EMPTY)){
                    CompSorterSubsystem.INSTANCE.sendLeft();
                    St = Step.WaitLast;
                }else if (!(right == CompSorterSubsystem.SlotDetection.EMPTY)){
                    CompSorterSubsystem.INSTANCE.sendRight();
                    St = Step.WaitLast;
                }else if (!(center == CompSorterSubsystem.SlotDetection.EMPTY)){
                    CompSorterSubsystem.INSTANCE.sendCenter();
                    St = Step.WaitLast;
                }else {
                    St = Step.Done;
                }
        }
        // executed on every update of the command
        ActiveOpMode.telemetry().addData("Step", St);
    }

    @Override
    public void stop(boolean interrupted) {
        CompSorterSubsystem.INSTANCE.resetSorter();
        // executed when the command ends
    }

    private void launchPurpleFirst(){
        if(centerSlotSet == Color.PURPLE){
            CompSorterSubsystem.INSTANCE.sendCenter();
            firstLaunch = Slot.Center;
            wait.start();
        }else if (leftSlotSet == Color.PURPLE){
            CompSorterSubsystem.INSTANCE.sendLeft();
            firstLaunch = Slot.Left;
            wait.start();
        }else if (rightSlotSet == Color.PURPLE){
            CompSorterSubsystem.INSTANCE.sendRight();
            firstLaunch = Slot.Right;
            wait.start();
        }
    }
    private void launchGreenFirst(){
        if(centerSlotSet == Color.GREEN){
            CompSorterSubsystem.INSTANCE.sendCenter();
            firstLaunch = Slot.Center;
            wait.start();
        }else if (leftSlotSet == Color.GREEN){
            CompSorterSubsystem.INSTANCE.sendLeft();
            firstLaunch = Slot.Left;
            wait.start();
        }else if (rightSlotSet == Color.GREEN){
            CompSorterSubsystem.INSTANCE.sendRight();
            firstLaunch = Slot.Right;
            wait.start();
        }
    }
    private void launchPurpleMiddle(){
        if(centerSlotSet == Color.PURPLE){
            CompSorterSubsystem.INSTANCE.sendCenter();
            middleLaunch = Slot.Center;
            wait.start();
        }else if (leftSlotSet == Color.PURPLE){
            CompSorterSubsystem.INSTANCE.sendLeft();
            middleLaunch = Slot.Left;
            wait.start();
        }else if (rightSlotSet == Color.PURPLE){
            CompSorterSubsystem.INSTANCE.sendRight();
            middleLaunch = Slot.Right;
            wait.start();
        }

    }
    private void launchGreenMiddle(){
        if(centerSlotSet == Color.GREEN){
            CompSorterSubsystem.INSTANCE.sendCenter();
            middleLaunch = Slot.Center;
            wait.start();
        }else if (leftSlotSet == Color.GREEN){
            CompSorterSubsystem.INSTANCE.sendLeft();
            middleLaunch = Slot.Left;
            wait.start();
        }else if (rightSlotSet == Color.GREEN){
            CompSorterSubsystem.INSTANCE.sendRight();
            middleLaunch = Slot.Right;
            wait.start();
        }
    }
    private void launchPurpleLast(){
        if(centerSlotSet == Color.PURPLE){
            CompSorterSubsystem.INSTANCE.sendCenter();
            lastLaunch = Slot.Center;
            wait.start();
        }else if (leftSlotSet == Color.PURPLE){
            CompSorterSubsystem.INSTANCE.sendLeft();
            lastLaunch = Slot.Left;
            wait.start();
        }else if (rightSlotSet == Color.PURPLE){
            CompSorterSubsystem.INSTANCE.sendRight();
            lastLaunch = Slot.Right;
            wait.start();
        }

    }
    private void launchGreenLast(){
        if(centerSlotSet == Color.GREEN){
            CompSorterSubsystem.INSTANCE.sendCenter();
            lastLaunch = Slot.Center;
            wait.start();
        }else if (leftSlotSet == Color.GREEN){
            CompSorterSubsystem.INSTANCE.sendLeft();
            lastLaunch = Slot.Left;
            wait.start();
        }else if (rightSlotSet == Color.GREEN){
            CompSorterSubsystem.INSTANCE.sendRight();
            lastLaunch = Slot.Right;
            wait.start();
        }

    }

}
