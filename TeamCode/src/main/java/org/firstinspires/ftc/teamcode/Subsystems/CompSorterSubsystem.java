package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Utility.Timing;

import java.util.concurrent.TimeUnit;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.impl.ServoEx;

public class CompSorterSubsystem implements Subsystem {
    public static final CompSorterSubsystem INSTANCE = new CompSorterSubsystem();
    private CompSorterSubsystem() {}

    private double hugPos = 0.075;

    private double lDown = 0.02;
    private double rDown = 0.035;
    private double cDown = 0.035;
    private double lUp = lDown+0.5;
    private double rUp = rDown+ 0.5;
    private double cUp = cDown+ 0.5;
    private double lWake = lDown +0.025;
    private double rWake = rDown +0.025;
    private double cWake = cDown +0.025;
    private double lHug = lDown + hugPos;
    private double rHug = rDown + hugPos;
    private double cHug = cDown + hugPos;
    private boolean busy = false;
    private NormalizedRGBA leftColor;
    private NormalizedRGBA centerColor;
    private NormalizedRGBA rightColor;

    Timing.Timer wait = new Timing.Timer(250, TimeUnit.MILLISECONDS);
    Timing.Timer reset = new Timing.Timer(200, TimeUnit.MILLISECONDS);
    Timing.Timer wakeT = new Timing.Timer(50, TimeUnit.MILLISECONDS);

    public ServoEx sortL = new ServoEx("Sort L");
    public ServoEx sortC = new ServoEx("Sort C");
    public ServoEx sortR = new ServoEx("Sort R");
    public RevColorSensorV3 sortCSL;
    public RevColorSensorV3 sortCSR;
    public RevColorSensorV3 sortCSC;

    public enum SlotDetection {
        EMPTY,
        PURPLE,
        GREEN,
        UNKNOWN,
        LAUNCHED
    }
    public Command hug = new LambdaCommand()
            .setStart(() -> {
                // Runs on start

                sortL.setPosition(lHug);
                sortC.setPosition(cHug);
                sortR.setPosition(rHug);
            })
            .setUpdate(() -> {
                // Runs on update
            })
            .setStop(interrupted -> {

                // Runs on stop
            })
            .setIsDone(() -> true) // Returns if the command has finished
            .requires()
            .setInterruptible(false);
    public Command wake = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                busy = true;
                sortL.setPosition(lWake);
                sortC.setPosition(cWake);
                sortR.setPosition(rWake);
                wakeT.start();
            })
            .setUpdate(() -> {
                // Runs on update
            })
            .setStop(interrupted -> {
                sortL.setPosition(lDown);
                sortC.setPosition(cDown);
                sortR.setPosition(rDown);
                // Runs on stop
            })
            .setIsDone(() -> wakeT.done()) // Returns if the command has finished
            .requires()
            .setInterruptible(false);

    public void sortHug(){
        sortL.setPosition(lHug);
        sortC.setPosition(cHug);
        sortR.setPosition(rHug);
    }




    public Command ejectL = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                busy = true;
                sortL.setPosition(lUp);
                wait.start();
            })
            .setUpdate(() -> {
                // Runs on update
            })
            .setStop(interrupted -> {
                sortL.setPosition(lDown);
                // Runs on stop
            })
            .setIsDone(() -> wait.done()) // Returns if the command has finished
            .requires()
            .setInterruptible(false);
    public void sendLeft() {
        sortL.setPosition(lUp);
    }
    public void sendCenter() {
        sortC.setPosition(cUp);
    }
    public void sendRight() {
        sortR.setPosition(rUp);
    }
    public void resetSorter(){
        sortL.setPosition(lDown);
        sortC.setPosition(cDown);
        sortR.setPosition(rDown);
    }

    public Command ejectC =

            new LambdaCommand()
                    .setStart(() -> {
                        // Runs on start
                        sortC.setPosition(cUp);
                        wait.start();
                        busy = true;
                    })
                    .setUpdate(() -> {
                        // Runs on update
                    })
                    .setStop(interrupted -> {
                        sortC.setPosition(cDown);
                        // Runs on stop
                    })
                    .setIsDone(() -> wait.done()) // Returns if the command has finished
                    .requires()
                    .setInterruptible(false);

    public Command ejectR = new LambdaCommand()
            .setStart(() -> {
                // Runs on start

                sortR.setPosition(rUp);
                busy = true;
                wait.start();
            })
            .setUpdate(() -> {
                // Runs on update
            })
            .setStop(interrupted -> {
                sortR.setPosition(rDown);
                // Runs on stop
            })
            .setIsDone(() -> wait.done()) // Returns if the command has finished
            .requires()
            .setInterruptible(false);

    public Command resetSorter = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                busy = true;

                reset.start();
            })
            .setUpdate(() -> {
                sortL.setPosition(lDown);
                sortC.setPosition(cDown);
                sortR.setPosition(rDown);
                // Runs on update
            })
            .setStop(interrupted -> {
                // Runs on stop
                busy = false;
            })
            .setIsDone(() -> reset.done()) // Returns if the command has finished
            .requires()
            .setInterruptible(false);
    // put hardware, commands, etc here
    public boolean leftDetected(){
        return (sortCSL.getDistance(DistanceUnit.MM)<70 );
    }
    public boolean leftDetectedDumb(){
        return (sortCSL.getNormalizedColors().alpha > 0.64);
    }

    public boolean centerDetected(){
        return (sortCSC.getDistance(DistanceUnit.MM)<80 );
    }
    public boolean centerDetectedDumb(){
        return (sortCSC.getNormalizedColors().alpha > 0.113);
    }
    public boolean rightDetected(){
        return (sortCSR.getDistance(DistanceUnit.MM)<42);
    }
    public boolean rightDetectedDumb(){
        return (sortCSR.getNormalizedColors().alpha > 0.190);
    }


    public SlotDetection leftSlot(){
        updateColor();
        if(leftDetected()){
            if(leftColor.red<0.37) {
                CompStatusSubsystem.INSTANCE.setLeftGreen();
                return SlotDetection.GREEN;
            }else {
                CompStatusSubsystem.INSTANCE.setLeftPurple();
                return SlotDetection.PURPLE;
            }
        }else {
            CompStatusSubsystem.INSTANCE.setLeftOff();
            return SlotDetection.EMPTY;
        }
    }
    public SlotDetection centerSlot(){
        updateColor();
        if(centerDetected()){
            if(centerColor.green>0.70) {
                CompStatusSubsystem.INSTANCE.setCenterGreen();
                return SlotDetection.GREEN;
            }else {
                CompStatusSubsystem.INSTANCE.setCenterPurple();
                return SlotDetection.PURPLE;
            }
        }else {
            CompStatusSubsystem.INSTANCE.setCenterOff();
            return SlotDetection.EMPTY;
        }
    }
    public SlotDetection rightSlot(){
        updateColor();
        if(rightDetected()){
            if(rightColor.red<0.40) {
                CompStatusSubsystem.INSTANCE.setRightGreen();
                return SlotDetection.GREEN;
            }else{
                CompStatusSubsystem.INSTANCE.setRightPurple();
                return SlotDetection.PURPLE;
            }
        }else {
            CompStatusSubsystem.INSTANCE.setRightOff();
            return SlotDetection.EMPTY;
        }
    }
    public void light(){


//        if(leftColor.red>20 && leftColor.green<37.5) {
//            CompStatusSubsystem.INSTANCE.setRightPurple();
//        }else if ( leftColor.blue<25) {
//            CompStatusSubsystem.INSTANCE.setRightGreen();
//        }else {
//            CompStatusSubsystem.INSTANCE.setRightError();
//        }
//        if(centerColor.red>50 && centerColor.green<115 && centerColor.blue>125) {
//            CompStatusSubsystem.INSTANCE.setCenterPurple();
//        }else if (centerColor.green> 116){
//            CompStatusSubsystem.INSTANCE.setCenterGreen();
//        }else {
//            CompStatusSubsystem.INSTANCE.setCenterError();
//        }
//
//        if(leftColor.red>50 && leftColor.green<=100) {
//            CompStatusSubsystem.INSTANCE.setLeftPurple();
//        }else if (leftColor.green>= 39){
//            CompStatusSubsystem.INSTANCE.setLeftGreen();
//        }else {
//            CompStatusSubsystem.INSTANCE.setLeftError();
//        }





    }
    public boolean sorterFull(){
        return (leftDetected()) && (centerDetected()) && (rightDetected());
    }
    public boolean sorterFullDumb(){
        return (leftDetectedDumb() && centerDetectedDumb() && rightDetectedDumb());
    }
    public boolean sorterEmpty(){
        return !leftDetected() && !centerDetected() && !rightDetected();
    }

    public void updateColor(){
        leftColor = sortCSL.getNormalizedColors();
        centerColor = sortCSC.getNormalizedColors();
        rightColor = sortCSR.getNormalizedColors();
    }



    @Override
    public void initialize() {
        // initialization logic (runs on init)
        sortCSL = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "Sort CS L");
        sortCSC = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "Sort CS C");
        sortCSR = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "Sort CS R");
        sortCSL.setGain(400);
        sortCSR.setGain(800);
        sortCSC.setGain(400);

        busy= false;
    }

    @Override
    public void periodic() {
        // periodic logic (runs every loop)
//        getSorterTelemetryAdv();
//        ActiveOpMode.telemetry().addData("Left Slot:", leftSlot());
//        ActiveOpMode.telemetry().addData("Center Slot:", centerSlot());
//        ActiveOpMode.telemetry().addData("Right Slot:", rightSlot());

    }
    public void getSorterTelemetryAdv(){

        ActiveOpMode.telemetry().addLine("-------------- Sorter Telemetry Adv: --------------");
        ActiveOpMode.telemetry().addData("Sorter Busy:", busy);
        ActiveOpMode.telemetry().addData("Sort CS L Distance MM:", sortCSL.getDistance(DistanceUnit.MM));
        ActiveOpMode.telemetry().addData("Sort CS C Distance MM:", sortCSC.getDistance(DistanceUnit.MM));
        ActiveOpMode.telemetry().addData("Sort CS R Distance MM:", sortCSR.getDistance(DistanceUnit.MM));
        ActiveOpMode.telemetry().addData("Left Slot:", leftSlot());
        ActiveOpMode.telemetry().addData("Center Slot:", centerSlot());
        ActiveOpMode.telemetry().addData("Right Slot:", rightSlot());
            ActiveOpMode.telemetry().addData("Sort CS L [A,R,G,B]","" + leftColor.alpha + ", " + leftColor.red + ", " + leftColor.green + ", " + leftColor.blue);
        ActiveOpMode.telemetry().addData("Sort CS C [A,R,G,B]","" + centerColor.alpha + ", " + centerColor.red + ", " + centerColor.green + ", " + centerColor.blue);
        ActiveOpMode.telemetry().addData("Sort CS R [A,R,G,B]","" + rightColor.alpha + ", " + rightColor.red + ", " + rightColor.green + ", " + rightColor.blue);


    }
}
