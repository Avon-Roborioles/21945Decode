package org.firstinspires.ftc.teamcode.Commands.Launch;

import org.firstinspires.ftc.teamcode.Subsystems.CompSorterSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.CompStatusSubsystem;
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


    CompSorterSubsystem.SlotDetection left, right, center;
    CompStatusSubsystem.OBPattern sorterPattern;
    Color first, middle, last;




    Step St = Step.LaunchFirst;
    Slot firstLaunch = Slot.Null;
    Slot middleLaunch = Slot.Null;
    Slot lastLaunch = Slot.Null;

    Timing.Timer wait = new Timing.Timer(250, TimeUnit.MILLISECONDS);
    Timing.Timer reset = new Timing.Timer(125, TimeUnit.MILLISECONDS);
    Timing.Timer ready = new Timing.Timer(250, TimeUnit.MILLISECONDS);


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
                    St = Step.CheckForMissFirst;
                }
                break;
            case CheckForMissFirst:
                switch (firstLaunch){
                    case Left:
                        if(CompSorterSubsystem.INSTANCE.leftSlot() == CompSorterSubsystem.SlotDetection.EMPTY){
                            St = Step.LaunchMiddle;
                            left = CompSorterSubsystem.SlotDetection.LAUNCHED;
                        }else {
                            St = Step.LaunchFirst;
                        }
                        break;
                    case Right:
                        if(CompSorterSubsystem.INSTANCE.rightSlot() == CompSorterSubsystem.SlotDetection.EMPTY){
                            St = Step.LaunchMiddle;
                            right = CompSorterSubsystem.SlotDetection.LAUNCHED;
                        }else {
                            St = Step.LaunchFirst;
                        }
                        break;
                    case Center:
                        if(CompSorterSubsystem.INSTANCE.centerSlot() == CompSorterSubsystem.SlotDetection.EMPTY){
                            St = Step.LaunchMiddle;
                            center = CompSorterSubsystem.SlotDetection.LAUNCHED;
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
                CompSorterSubsystem.INSTANCE.resetSorter();
                if(reset.done()){
                    St = Step.CheckForMissMiddle;
                }
                break;
            case CheckForMissMiddle:
                switch (firstLaunch){
                    case Left:
                        if(CompSorterSubsystem.INSTANCE.leftSlot() == CompSorterSubsystem.SlotDetection.EMPTY){
                            left = CompSorterSubsystem.SlotDetection.LAUNCHED;
                        }else {
                            left = CompSorterSubsystem.INSTANCE.leftSlot();
                            St = Step.LaunchMiddle;
                        }
                        break;
                    case Right:
                        if(CompSorterSubsystem.INSTANCE.rightSlot() == CompSorterSubsystem.SlotDetection.EMPTY){
                            right = CompSorterSubsystem.SlotDetection.LAUNCHED;
                        }else {
                            right = CompSorterSubsystem.INSTANCE.rightSlot();
                            St = Step.LaunchMiddle;
                        }
                        break;
                    case Center:
                        if(CompSorterSubsystem.INSTANCE.centerSlot() == CompSorterSubsystem.SlotDetection.EMPTY){
                            center = CompSorterSubsystem.SlotDetection.LAUNCHED;
                        }else {
                            center = CompSorterSubsystem.INSTANCE.centerSlot();
                            St = Step.LaunchMiddle;
                        }
                        break;

                }
                switch (middleLaunch){
                    case Left:
                        if(CompSorterSubsystem.INSTANCE.leftSlot() == CompSorterSubsystem.SlotDetection.EMPTY){
                            St = Step.LaunchLast;
                            left = CompSorterSubsystem.SlotDetection.LAUNCHED;
                        }else {
                            St = Step.LaunchMiddle;
                        }
                        break;
                    case Right:
                        if(CompSorterSubsystem.INSTANCE.rightSlot() == CompSorterSubsystem.SlotDetection.EMPTY){
                            St = Step.LaunchLast;
                            right = CompSorterSubsystem.SlotDetection.LAUNCHED;
                        }else {
                            St = Step.LaunchMiddle;
                        }
                        break;
                    case Center:
                        if(CompSorterSubsystem.INSTANCE.centerSlot() == CompSorterSubsystem.SlotDetection.EMPTY){
                            St = Step.LaunchLast;
                            center = CompSorterSubsystem.SlotDetection.LAUNCHED;
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
                    St = Step.LaunchLast;
                }else if (!(right == CompSorterSubsystem.SlotDetection.EMPTY)){
                    St = Step.LaunchLast;
                }else if (!(center == CompSorterSubsystem.SlotDetection.EMPTY)){
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
        CompSorterSubsystem.INSTANCE.resetSorter();
        // executed when the command ends
    }

    private void launchPurpleFirst(){
        if(center == CompSorterSubsystem.SlotDetection.PURPLE){
            CompSorterSubsystem.INSTANCE.sendCenter();
            firstLaunch = Slot.Center;
            wait.start();
        }else if (left == CompSorterSubsystem.SlotDetection.PURPLE){
            CompSorterSubsystem.INSTANCE.sendLeft();
            firstLaunch = Slot.Left;
            wait.start();
        }else if (right == CompSorterSubsystem.SlotDetection.PURPLE){
            CompSorterSubsystem.INSTANCE.sendRight();
            firstLaunch = Slot.Right;
            wait.start();
        }else if (center == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendCenter();
            firstLaunch = Slot.Center;
            wait.start();
        }else if (left == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendLeft();
            firstLaunch = Slot.Left;
            wait.start();
        }else if (right == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendRight();
            firstLaunch = Slot.Right;
            wait.start();
        }else if (!(center == CompSorterSubsystem.SlotDetection.GREEN)){
            CompSorterSubsystem.INSTANCE.sendCenter();
            firstLaunch = Slot.Center;
            wait.start();
        }else if (!(left == CompSorterSubsystem.SlotDetection.GREEN)){
            CompSorterSubsystem.INSTANCE.sendLeft();
            firstLaunch = Slot.Left;
            wait.start();
        }else if (!(right == CompSorterSubsystem.SlotDetection.GREEN)){
            CompSorterSubsystem.INSTANCE.sendRight();
            firstLaunch = Slot.Right;
            wait.start();
        }
    }
    private void launchGreenFirst(){
        if(center == CompSorterSubsystem.SlotDetection.GREEN){
            CompSorterSubsystem.INSTANCE.sendCenter();
            firstLaunch = Slot.Center;
            wait.start();
        }else if (left == CompSorterSubsystem.SlotDetection.GREEN){
            CompSorterSubsystem.INSTANCE.sendLeft();
            firstLaunch = Slot.Left;
            wait.start();
        }else if (right == CompSorterSubsystem.SlotDetection.GREEN){
            CompSorterSubsystem.INSTANCE.sendRight();
            firstLaunch = Slot.Right;
            wait.start();
        }else if (center == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendCenter();
            firstLaunch = Slot.Center;
            wait.start();
        }else if (left == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendLeft();
            firstLaunch = Slot.Left;
            wait.start();
        }else if (right == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendRight();
            firstLaunch = Slot.Right;
            wait.start();
        }else if (!(center == CompSorterSubsystem.SlotDetection.PURPLE)){
            CompSorterSubsystem.INSTANCE.sendCenter();
            firstLaunch = Slot.Center;
            wait.start();
        }else if (!(left == CompSorterSubsystem.SlotDetection.PURPLE)){
            CompSorterSubsystem.INSTANCE.sendLeft();
            firstLaunch = Slot.Left;
            wait.start();
        }else if (!(right == CompSorterSubsystem.SlotDetection.PURPLE)){
            CompSorterSubsystem.INSTANCE.sendRight();
            firstLaunch = Slot.Right;
            wait.start();
        }
    }
    private void launchPurpleMiddle(){
        if(center == CompSorterSubsystem.SlotDetection.PURPLE){
            CompSorterSubsystem.INSTANCE.sendCenter();
            middleLaunch = Slot.Center;
            wait.start();
        }else if (left == CompSorterSubsystem.SlotDetection.PURPLE){
            CompSorterSubsystem.INSTANCE.sendLeft();
            middleLaunch = Slot.Left;
            wait.start();
        }else if (right == CompSorterSubsystem.SlotDetection.PURPLE){
            CompSorterSubsystem.INSTANCE.sendRight();
            middleLaunch = Slot.Right;
            wait.start();
        }else if (center == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendCenter();
            middleLaunch = Slot.Center;
            wait.start();
        }else if (left == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendLeft();
            middleLaunch = Slot.Left;
            wait.start();
        }else if (right == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendRight();
            middleLaunch = Slot.Right;
            wait.start();
        }else if (!(center == CompSorterSubsystem.SlotDetection.GREEN)){
            CompSorterSubsystem.INSTANCE.sendCenter();
            middleLaunch = Slot.Center;
            wait.start();
        }else if (!(left == CompSorterSubsystem.SlotDetection.GREEN)){
            CompSorterSubsystem.INSTANCE.sendLeft();
            middleLaunch = Slot.Left;
            wait.start();
        }else if (!(right == CompSorterSubsystem.SlotDetection.GREEN)){
            CompSorterSubsystem.INSTANCE.sendRight();
            middleLaunch = Slot.Right;
            wait.start();
        }
    }
    private void launchGreenMiddle(){
        if(center == CompSorterSubsystem.SlotDetection.GREEN){
            CompSorterSubsystem.INSTANCE.sendCenter();
            middleLaunch = Slot.Center;
            wait.start();
        }else if (left == CompSorterSubsystem.SlotDetection.GREEN){
            CompSorterSubsystem.INSTANCE.sendLeft();
            middleLaunch = Slot.Left;
            wait.start();
        }else if (right == CompSorterSubsystem.SlotDetection.GREEN){
            CompSorterSubsystem.INSTANCE.sendRight();
            middleLaunch = Slot.Right;
            wait.start();
        }else if (center == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendCenter();
            middleLaunch = Slot.Center;
            wait.start();
        }else if (left == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendLeft();
            middleLaunch = Slot.Left;
            wait.start();
        }else if (right == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendRight();
            middleLaunch = Slot.Right;
            wait.start();
        }else if (!(center == CompSorterSubsystem.SlotDetection.PURPLE)){
            CompSorterSubsystem.INSTANCE.sendCenter();
            middleLaunch = Slot.Center;
            wait.start();
        }else if (!(left == CompSorterSubsystem.SlotDetection.PURPLE)){
            CompSorterSubsystem.INSTANCE.sendLeft();
            middleLaunch = Slot.Left;
            wait.start();
        }else if (!(right == CompSorterSubsystem.SlotDetection.PURPLE)){
            CompSorterSubsystem.INSTANCE.sendRight();
            middleLaunch = Slot.Right;
            wait.start();
        }
    }
    private void launchPurpleLast(){
        if(center == CompSorterSubsystem.SlotDetection.PURPLE){
            CompSorterSubsystem.INSTANCE.sendCenter();
            lastLaunch = Slot.Center;
            wait.start();
        }else if (left == CompSorterSubsystem.SlotDetection.PURPLE){
            CompSorterSubsystem.INSTANCE.sendLeft();
            lastLaunch = Slot.Left;
            wait.start();
        }else if (right == CompSorterSubsystem.SlotDetection.PURPLE){
            CompSorterSubsystem.INSTANCE.sendRight();
            lastLaunch = Slot.Right;
            wait.start();
        }else if (center == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendCenter();
            lastLaunch = Slot.Center;
            wait.start();
        }else if (left == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendLeft();
            lastLaunch = Slot.Left;
            wait.start();
        }else if (right == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendRight();
            lastLaunch = Slot.Right;
            wait.start();
        }else if (!(center == CompSorterSubsystem.SlotDetection.GREEN)){
            CompSorterSubsystem.INSTANCE.sendCenter();
            lastLaunch = Slot.Center;
            wait.start();
        }else if (!(left == CompSorterSubsystem.SlotDetection.GREEN)){
            CompSorterSubsystem.INSTANCE.sendLeft();
            lastLaunch = Slot.Left;
            wait.start();
        }else if (!(right == CompSorterSubsystem.SlotDetection.GREEN)){
            CompSorterSubsystem.INSTANCE.sendRight();
            lastLaunch = Slot.Right;
            wait.start();
        }
    }
    private void launchGreenLast(){
        if(center == CompSorterSubsystem.SlotDetection.GREEN){
            CompSorterSubsystem.INSTANCE.sendCenter();
            lastLaunch = Slot.Center;
            wait.start();
        }else if (left == CompSorterSubsystem.SlotDetection.GREEN){
            CompSorterSubsystem.INSTANCE.sendLeft();
            lastLaunch = Slot.Left;
            wait.start();
        }else if (right == CompSorterSubsystem.SlotDetection.GREEN){
            CompSorterSubsystem.INSTANCE.sendRight();
            lastLaunch = Slot.Right;
            wait.start();
        }else if (center == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendCenter();
            lastLaunch = Slot.Center;
            wait.start();
        }else if (left == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendLeft();
            lastLaunch = Slot.Left;
            wait.start();
        }else if (right == CompSorterSubsystem.SlotDetection.UNKNOWN){
            CompSorterSubsystem.INSTANCE.sendRight();
            lastLaunch = Slot.Right;
            wait.start();
        }else if (!(center == CompSorterSubsystem.SlotDetection.PURPLE)){
            CompSorterSubsystem.INSTANCE.sendCenter();
            lastLaunch = Slot.Center;
            wait.start();
        }else if (!(left == CompSorterSubsystem.SlotDetection.PURPLE)){
            CompSorterSubsystem.INSTANCE.sendLeft();
            lastLaunch = Slot.Left;
            wait.start();
        }else if (!(right == CompSorterSubsystem.SlotDetection.PURPLE)){
            CompSorterSubsystem.INSTANCE.sendRight();
            lastLaunch = Slot.Right;
            wait.start();
        }

    }

}
