package org.firstinspires.ftc.teamcode.Commands.Launch;

import org.firstinspires.ftc.teamcode.Subsystems.SorterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.StatusSubsystem;
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




    SorterSubsystem.SlotDetection left, right, center;
    StatusSubsystem.OBPattern sorterPattern;
    Color first, middle, last;
    Color leftSlotSet, rightSlotSet, centerSlotSet;




    Step St = Step.LaunchFirst;
    Slot firstLaunch = Slot.Null;

    Slot middleLaunch = Slot.Null;
    Slot lastLaunch = Slot.Null;

    Timing.Timer wait = new Timing.Timer(250, TimeUnit.MILLISECONDS);
    Timing.Timer reset = new Timing.Timer(200, TimeUnit.MILLISECONDS);
    Timing.Timer delay = new Timing.Timer(1000, TimeUnit.MILLISECONDS);
    Timing.Timer ready = new Timing.Timer(300, TimeUnit.MILLISECONDS);
    boolean checkedForMiss =false;

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
        SorterSubsystem.INSTANCE.resetSorter();
        St = Step.GetReady;
        firstLaunch = Slot.Null;
        checkedForMiss = false;


        sorterPattern = StatusSubsystem.INSTANCE.getCurrentOBPattern();
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

                if (left == SorterSubsystem.SlotDetection.GREEN){
                    leftSlotSet = Color.GREEN;
                    rightSlotSet = Color.PURPLE;
                    centerSlotSet = Color.PURPLE;
                }else if(right == SorterSubsystem.SlotDetection.GREEN){
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
                SorterSubsystem.INSTANCE.resetSorter();
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
                SorterSubsystem.INSTANCE.resetSorter();
                if(reset.done()){
                    St = Step.LaunchLast;
                }
                break;

            case LaunchLast:
                launchLast();
                St = Step.WaitLast;

                break;
            case WaitLast:
                if(wait.done()){
                    St = Step.ResetLast;
                    reset.start();
                }
                break;
            case ResetLast:
                SorterSubsystem.INSTANCE.resetSorter();
                if(reset.done()){
                    St = Step.CheckForMissLast;
                }
                break;
            case CheckForMissLast:
                left = SorterSubsystem.INSTANCE.leftSlot();
                right = SorterSubsystem.INSTANCE.rightSlot();
                center = SorterSubsystem.INSTANCE.centerSlot();
                if(!checkedForMiss) {
                    checkedForMiss = true;
                    if (!(left == SorterSubsystem.SlotDetection.EMPTY)) {
                        SorterSubsystem.INSTANCE.sendLeft();
                        wait.start();
                        St = Step.WaitLast;
                    } else if (!(right == SorterSubsystem.SlotDetection.EMPTY)) {
                        SorterSubsystem.INSTANCE.sendRight();
                        wait.start();
                        St = Step.WaitLast;
                    } else if (!(center == SorterSubsystem.SlotDetection.EMPTY)) {
                        SorterSubsystem.INSTANCE.sendCenter();
                        wait.start();
                        St = Step.WaitLast;
                    } else {
                        St = Step.Done;
                    }
                }else{
                    St = Step.Done;
                }
        }
        // executed on every update of the command
        ActiveOpMode.telemetry().addData("Step", St);
    }

    @Override
    public void stop(boolean interrupted) {
        SorterSubsystem.INSTANCE.resetSorter();
        // executed when the command ends
    }

    private void launchPurpleFirst(){
        if(centerSlotSet == Color.PURPLE){
            SorterSubsystem.INSTANCE.sendCenter();
            firstLaunch = Slot.Center;
            wait.start();
        }else if (leftSlotSet == Color.PURPLE){
            SorterSubsystem.INSTANCE.sendLeft();
            firstLaunch = Slot.Left;
            wait.start();
        }else if (rightSlotSet == Color.PURPLE){
            SorterSubsystem.INSTANCE.sendRight();
            firstLaunch = Slot.Right;
            wait.start();
        }
    }
    private void launchGreenFirst(){
        if(centerSlotSet == Color.GREEN){
            SorterSubsystem.INSTANCE.sendCenter();
            firstLaunch = Slot.Center;
            wait.start();
        }else if (leftSlotSet == Color.GREEN){
            SorterSubsystem.INSTANCE.sendLeft();
            firstLaunch = Slot.Left;
            wait.start();
        }else if (rightSlotSet == Color.GREEN){
            SorterSubsystem.INSTANCE.sendRight();
            firstLaunch = Slot.Right;
            wait.start();
        }
    }
    private void launchPurpleMiddle(){
        if(centerSlotSet == Color.PURPLE){
            SorterSubsystem.INSTANCE.sendCenter();
            middleLaunch = Slot.Center;
            wait.start();
        }else if (leftSlotSet == Color.PURPLE){
            SorterSubsystem.INSTANCE.sendLeft();
            middleLaunch = Slot.Left;
            wait.start();
        }else if (rightSlotSet == Color.PURPLE){
            SorterSubsystem.INSTANCE.sendRight();
            middleLaunch = Slot.Right;
            wait.start();
        }

    }
    private void launchGreenMiddle(){
        if(centerSlotSet == Color.GREEN){
            SorterSubsystem.INSTANCE.sendCenter();
            middleLaunch = Slot.Center;
            wait.start();
        }else if (leftSlotSet == Color.GREEN){
            SorterSubsystem.INSTANCE.sendLeft();
            middleLaunch = Slot.Left;
            wait.start();
        }else if (rightSlotSet == Color.GREEN){
            SorterSubsystem.INSTANCE.sendRight();
            middleLaunch = Slot.Right;
            wait.start();
        }
    }
    private void launchLast(){
       if (firstLaunch != Slot.Center && middleLaunch != Slot.Center){
           SorterSubsystem.INSTANCE.sendCenter();
       }else if(firstLaunch != Slot.Right && firstLaunch != Slot.Right){
           SorterSubsystem.INSTANCE.sendRight();
       }else{
           SorterSubsystem.INSTANCE.sendLeft();
       }

    }

}
