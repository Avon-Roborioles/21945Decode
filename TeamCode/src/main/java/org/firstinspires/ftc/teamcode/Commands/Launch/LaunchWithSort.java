package org.firstinspires.ftc.teamcode.Commands.Launch;

import org.firstinspires.ftc.teamcode.Subsystems.SorterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.StatusSubsystem;
import org.firstinspires.ftc.teamcode.Utility.Timing;

import java.util.concurrent.TimeUnit;

import dev.nextftc.core.commands.Command;
import dev.nextftc.ftc.ActiveOpMode;

public class LaunchWithSort extends Command {
    enum Step{
        GetReady,
        WaitForReady,
        Ready,
        LaunchFirst,
        WaitFirst,
        ResetFirst,
        CheckForMissFirst,
        LaunchMiddle,
        WaitMiddle,
        ResetMiddle,
        CheckForMissMiddle,
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




    Step St = Step.LaunchFirst;
    Slot firstLaunch = Slot.Null;
    Slot middleLaunch = Slot.Null;
    Slot lastLaunch = Slot.Null;

    Timing.Timer wait = new Timing.Timer(205, TimeUnit.MILLISECONDS);
    Timing.Timer reset = new Timing.Timer(150, TimeUnit.MILLISECONDS);
    Timing.Timer delay = new Timing.Timer(1000, TimeUnit.MILLISECONDS);
    Timing.Timer ready = new Timing.Timer(300, TimeUnit.MILLISECONDS);


    public LaunchWithSort() {
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
                    St = Step.CheckForMissFirst;
                }
                break;
            case CheckForMissFirst:
                switch (firstLaunch){
                    case Left:
                        if(SorterSubsystem.INSTANCE.leftSlot() == SorterSubsystem.SlotDetection.EMPTY){
                            St = Step.LaunchMiddle;
                            left = SorterSubsystem.SlotDetection.LAUNCHED;
                        }else {
                            St = Step.LaunchFirst;
                        }
                        break;
                    case Right:
                        if(SorterSubsystem.INSTANCE.rightSlot() == SorterSubsystem.SlotDetection.EMPTY){
                            St = Step.LaunchMiddle;
                            right = SorterSubsystem.SlotDetection.LAUNCHED;
                        }else {
                            St = Step.LaunchFirst;
                        }
                        break;
                    case Center:
                        if(SorterSubsystem.INSTANCE.centerSlot() == SorterSubsystem.SlotDetection.EMPTY){
                            St = Step.LaunchMiddle;
                            center = SorterSubsystem.SlotDetection.LAUNCHED;
                        }else {
                            St = Step.LaunchFirst;
                        }
                        break;

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
                    St = Step.CheckForMissMiddle;
                }
                break;
            case CheckForMissMiddle:
                switch (firstLaunch){
                    case Left:
                        if(SorterSubsystem.INSTANCE.leftSlot() == SorterSubsystem.SlotDetection.EMPTY){
                            left = SorterSubsystem.SlotDetection.LAUNCHED;
                        }else {
                            left = SorterSubsystem.INSTANCE.leftSlot();
                            St = Step.LaunchMiddle;
                        }
                        break;
                    case Right:
                        if(SorterSubsystem.INSTANCE.rightSlot() == SorterSubsystem.SlotDetection.EMPTY){
                            right = SorterSubsystem.SlotDetection.LAUNCHED;
                        }else {
                            right = SorterSubsystem.INSTANCE.rightSlot();
                            St = Step.LaunchMiddle;
                        }
                        break;
                    case Center:
                        if(SorterSubsystem.INSTANCE.centerSlot() == SorterSubsystem.SlotDetection.EMPTY){
                            center = SorterSubsystem.SlotDetection.LAUNCHED;
                        }else {
                            center = SorterSubsystem.INSTANCE.centerSlot();
                            St = Step.LaunchMiddle;
                        }
                        break;

                }
                switch (middleLaunch){
                    case Left:
                        if(SorterSubsystem.INSTANCE.leftSlot() == SorterSubsystem.SlotDetection.EMPTY){
                            St = Step.LaunchLast;
                            left = SorterSubsystem.SlotDetection.LAUNCHED;
                        }else {
                            St = Step.LaunchMiddle;
                        }
                        break;
                    case Right:
                        if(SorterSubsystem.INSTANCE.rightSlot() == SorterSubsystem.SlotDetection.EMPTY){
                            St = Step.LaunchLast;
                            right = SorterSubsystem.SlotDetection.LAUNCHED;
                        }else {
                            St = Step.LaunchMiddle;
                        }
                        break;
                    case Center:
                        if(SorterSubsystem.INSTANCE.centerSlot() == SorterSubsystem.SlotDetection.EMPTY){
                            St = Step.LaunchLast;
                            center = SorterSubsystem.SlotDetection.LAUNCHED;
                        }else {
                            St = Step.LaunchMiddle;
                        }
                        break;
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
                SorterSubsystem.INSTANCE.resetSorter();
                if(reset.done()){
                    St = Step.CheckForMissLast;
                }
                break;
            case CheckForMissLast:
                left = SorterSubsystem.INSTANCE.leftSlot();
                right = SorterSubsystem.INSTANCE.rightSlot();
                center = SorterSubsystem.INSTANCE.centerSlot();
                if(!(left == SorterSubsystem.SlotDetection.EMPTY)){
                    St = Step.LaunchLast;
                }else if (!(right == SorterSubsystem.SlotDetection.EMPTY)){
                    St = Step.LaunchLast;
                }else if (!(center == SorterSubsystem.SlotDetection.EMPTY)){
                    St = Step.LaunchLast;
                }else {
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
        if(center == SorterSubsystem.SlotDetection.PURPLE){
            SorterSubsystem.INSTANCE.sendCenter();
            firstLaunch = Slot.Center;
            wait.start();
        }else if (left == SorterSubsystem.SlotDetection.PURPLE){
            SorterSubsystem.INSTANCE.sendLeft();
            firstLaunch = Slot.Left;
            wait.start();
        }else if (right == SorterSubsystem.SlotDetection.PURPLE){
            SorterSubsystem.INSTANCE.sendRight();
            firstLaunch = Slot.Right;
            wait.start();
        }else if (center == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendCenter();
            firstLaunch = Slot.Center;
            wait.start();
        }else if (left == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendLeft();
            firstLaunch = Slot.Left;
            wait.start();
        }else if (right == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendRight();
            firstLaunch = Slot.Right;
            wait.start();
        }else if (!(center == SorterSubsystem.SlotDetection.GREEN)){
            SorterSubsystem.INSTANCE.sendCenter();
            firstLaunch = Slot.Center;
            wait.start();
        }else if (!(left == SorterSubsystem.SlotDetection.GREEN)){
            SorterSubsystem.INSTANCE.sendLeft();
            firstLaunch = Slot.Left;
            wait.start();
        }else if (!(right == SorterSubsystem.SlotDetection.GREEN)){
            SorterSubsystem.INSTANCE.sendRight();
            firstLaunch = Slot.Right;
            wait.start();
        }
    }
    private void launchGreenFirst(){
        if(center == SorterSubsystem.SlotDetection.GREEN){
            SorterSubsystem.INSTANCE.sendCenter();
            firstLaunch = Slot.Center;
            wait.start();
        }else if (left == SorterSubsystem.SlotDetection.GREEN){
            SorterSubsystem.INSTANCE.sendLeft();
            firstLaunch = Slot.Left;
            wait.start();
        }else if (right == SorterSubsystem.SlotDetection.GREEN){
            SorterSubsystem.INSTANCE.sendRight();
            firstLaunch = Slot.Right;
            wait.start();
        }else if (center == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendCenter();
            firstLaunch = Slot.Center;
            wait.start();
        }else if (left == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendLeft();
            firstLaunch = Slot.Left;
            wait.start();
        }else if (right == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendRight();
            firstLaunch = Slot.Right;
            wait.start();
        }else if (!(center == SorterSubsystem.SlotDetection.PURPLE)){
            SorterSubsystem.INSTANCE.sendCenter();
            firstLaunch = Slot.Center;
            wait.start();
        }else if (!(left == SorterSubsystem.SlotDetection.PURPLE)){
            SorterSubsystem.INSTANCE.sendLeft();
            firstLaunch = Slot.Left;
            wait.start();
        }else if (!(right == SorterSubsystem.SlotDetection.PURPLE)){
            SorterSubsystem.INSTANCE.sendRight();
            firstLaunch = Slot.Right;
            wait.start();
        }
    }
    private void launchPurpleMiddle(){
        if(center == SorterSubsystem.SlotDetection.PURPLE){
            SorterSubsystem.INSTANCE.sendCenter();
            middleLaunch = Slot.Center;
            wait.start();
        }else if (left == SorterSubsystem.SlotDetection.PURPLE){
            SorterSubsystem.INSTANCE.sendLeft();
            middleLaunch = Slot.Left;
            wait.start();
        }else if (right == SorterSubsystem.SlotDetection.PURPLE){
            SorterSubsystem.INSTANCE.sendRight();
            middleLaunch = Slot.Right;
            wait.start();
        }else if (center == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendCenter();
            middleLaunch = Slot.Center;
            wait.start();
        }else if (left == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendLeft();
            middleLaunch = Slot.Left;
            wait.start();
        }else if (right == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendRight();
            middleLaunch = Slot.Right;
            wait.start();
        }else if (!(center == SorterSubsystem.SlotDetection.GREEN)){
            SorterSubsystem.INSTANCE.sendCenter();
            middleLaunch = Slot.Center;
            wait.start();
        }else if (!(left == SorterSubsystem.SlotDetection.GREEN)){
            SorterSubsystem.INSTANCE.sendLeft();
            middleLaunch = Slot.Left;
            wait.start();
        }else if (!(right == SorterSubsystem.SlotDetection.GREEN)){
            SorterSubsystem.INSTANCE.sendRight();
            middleLaunch = Slot.Right;
            wait.start();
        }
    }
    private void launchGreenMiddle(){
        if(center == SorterSubsystem.SlotDetection.GREEN){
            SorterSubsystem.INSTANCE.sendCenter();
            middleLaunch = Slot.Center;
            wait.start();
        }else if (left == SorterSubsystem.SlotDetection.GREEN){
            SorterSubsystem.INSTANCE.sendLeft();
            middleLaunch = Slot.Left;
            wait.start();
        }else if (right == SorterSubsystem.SlotDetection.GREEN){
            SorterSubsystem.INSTANCE.sendRight();
            middleLaunch = Slot.Right;
            wait.start();
        }else if (center == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendCenter();
            middleLaunch = Slot.Center;
            wait.start();
        }else if (left == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendLeft();
            middleLaunch = Slot.Left;
            wait.start();
        }else if (right == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendRight();
            middleLaunch = Slot.Right;
            wait.start();
        }else if (!(center == SorterSubsystem.SlotDetection.PURPLE)){
            SorterSubsystem.INSTANCE.sendCenter();
            middleLaunch = Slot.Center;
            wait.start();
        }else if (!(left == SorterSubsystem.SlotDetection.PURPLE)){
            SorterSubsystem.INSTANCE.sendLeft();
            middleLaunch = Slot.Left;
            wait.start();
        }else if (!(right == SorterSubsystem.SlotDetection.PURPLE)){
            SorterSubsystem.INSTANCE.sendRight();
            middleLaunch = Slot.Right;
            wait.start();
        }
    }
    private void launchPurpleLast(){
        if(center == SorterSubsystem.SlotDetection.PURPLE){
            SorterSubsystem.INSTANCE.sendCenter();
            lastLaunch = Slot.Center;
            wait.start();
        }else if (left == SorterSubsystem.SlotDetection.PURPLE){
            SorterSubsystem.INSTANCE.sendLeft();
            lastLaunch = Slot.Left;
            wait.start();
        }else if (right == SorterSubsystem.SlotDetection.PURPLE){
            SorterSubsystem.INSTANCE.sendRight();
            lastLaunch = Slot.Right;
            wait.start();
        }else if (center == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendCenter();
            lastLaunch = Slot.Center;
            wait.start();
        }else if (left == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendLeft();
            lastLaunch = Slot.Left;
            wait.start();
        }else if (right == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendRight();
            lastLaunch = Slot.Right;
            wait.start();
        }else if (!(center == SorterSubsystem.SlotDetection.GREEN)){
            SorterSubsystem.INSTANCE.sendCenter();
            lastLaunch = Slot.Center;
            wait.start();
        }else if (!(left == SorterSubsystem.SlotDetection.GREEN)){
            SorterSubsystem.INSTANCE.sendLeft();
            lastLaunch = Slot.Left;
            wait.start();
        }else if (!(right == SorterSubsystem.SlotDetection.GREEN)){
            SorterSubsystem.INSTANCE.sendRight();
            lastLaunch = Slot.Right;
            wait.start();
        }
    }
    private void launchGreenLast(){
        if(center == SorterSubsystem.SlotDetection.GREEN){
            SorterSubsystem.INSTANCE.sendCenter();
            lastLaunch = Slot.Center;
            wait.start();
        }else if (left == SorterSubsystem.SlotDetection.GREEN){
            SorterSubsystem.INSTANCE.sendLeft();
            lastLaunch = Slot.Left;
            wait.start();
        }else if (right == SorterSubsystem.SlotDetection.GREEN){
            SorterSubsystem.INSTANCE.sendRight();
            lastLaunch = Slot.Right;
            wait.start();
        }else if (center == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendCenter();
            lastLaunch = Slot.Center;
            wait.start();
        }else if (left == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendLeft();
            lastLaunch = Slot.Left;
            wait.start();
        }else if (right == SorterSubsystem.SlotDetection.UNKNOWN){
            SorterSubsystem.INSTANCE.sendRight();
            lastLaunch = Slot.Right;
            wait.start();
        }else if (!(center == SorterSubsystem.SlotDetection.PURPLE)){
            SorterSubsystem.INSTANCE.sendCenter();
            lastLaunch = Slot.Center;
            wait.start();
        }else if (!(left == SorterSubsystem.SlotDetection.PURPLE)){
            SorterSubsystem.INSTANCE.sendLeft();
            lastLaunch = Slot.Left;
            wait.start();
        }else if (!(right == SorterSubsystem.SlotDetection.PURPLE)){
            SorterSubsystem.INSTANCE.sendRight();
            lastLaunch = Slot.Right;
            wait.start();
        }

    }

}
