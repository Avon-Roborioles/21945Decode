package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.rev.RevColorSensorV3;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Utility.Timing;

import java.util.concurrent.TimeUnit;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.ServoEx;

public class CompSorterSubsystem implements Subsystem {
    public static final CompSorterSubsystem INSTANCE = new CompSorterSubsystem();
    private CompSorterSubsystem() {}

    private double lUp = 0.5;
    private double lDown = 0;
    private double cUp = 0.5;
    private double cDown = 0.035;
    private double rUp = 0.535;
    private double rDown = 0.035;
    private double cWake = cDown +0.005;
    private double lWake = lDown +0.025;
    private double rWake = rDown +0.005;
    private double cHug = cDown + 0.01;
    private double lHug = lDown + 0.01;
    private double rHug = rDown + 0.01;
    private boolean busy = false;

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
        UNKNOWN
    }

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
    private boolean leftDetected(){
        return (sortCSL.getDistance(DistanceUnit.MM)<75 && sortCSL.getDistance(DistanceUnit.MM)>38);
    }
    private boolean centerDetected(){
        return (sortCSC.getDistance(DistanceUnit.MM)<80 && sortCSC.getDistance(DistanceUnit.MM)>38);
    }
    private boolean rightDetected(){
        return (sortCSR.getDistance(DistanceUnit.MM)<44 && sortCSR.getDistance(DistanceUnit.MM)>38);
    }

    public SlotDetection leftSlot(){
        if(leftDetected()){
            if(sortCSL.red()>50 && sortCSL.green()<=100) {
                CompStatusSubsystem.INSTANCE.setLeftPurple();
                return SlotDetection.PURPLE;
            }else if (sortCSL.green()>= 39){
                CompStatusSubsystem.INSTANCE.setLeftGreen();
                return SlotDetection.GREEN;
            }else {
                CompStatusSubsystem.INSTANCE.setLeftError();
                return SlotDetection.UNKNOWN;
            }
        }else {
            CompStatusSubsystem.INSTANCE.setLeftOff();
            return SlotDetection.EMPTY;
        }
    }
    public SlotDetection centerSlot(){
        if(centerDetected()){
            if(sortCSC.red()>50 && sortCSC.green()<115 && sortCSC.blue()>125) {
                CompStatusSubsystem.INSTANCE.setCenterPurple();
                return SlotDetection.PURPLE;
            }else if (sortCSC.green()> 116){
                CompStatusSubsystem.INSTANCE.setCenterGreen();
                return SlotDetection.GREEN;
            }else {
                CompStatusSubsystem.INSTANCE.setCenterError();
                return SlotDetection.UNKNOWN;
            }
        }else {
            CompStatusSubsystem.INSTANCE.setCenterOff();
            return SlotDetection.EMPTY;
        }
    }
    public SlotDetection rightSlot(){
        if(rightDetected()){
            if(sortCSR.red()>20 && sortCSR.green()<37.5) {
                CompStatusSubsystem.INSTANCE.setRightPurple();
                return SlotDetection.PURPLE;
            }else if ( sortCSR.blue()<25){
                CompStatusSubsystem.INSTANCE.setRightGreen();
                return SlotDetection.GREEN;
            }else {
                CompStatusSubsystem.INSTANCE.setRightError();
                return SlotDetection.UNKNOWN;
            }
        }else {
            CompStatusSubsystem.INSTANCE.setRightOff();
            return SlotDetection.EMPTY;
        }
    }
    public boolean sorterFull(){
        return !(leftSlot() == SlotDetection.EMPTY) && !(centerSlot() == SlotDetection.EMPTY) && !(rightSlot() == SlotDetection.EMPTY);
    }
    public boolean sorterEmpty(){
        return !leftDetected() && !centerDetected() && !rightDetected();
    }




    @Override
    public void initialize() {
        // initialization logic (runs on init)
        sortCSL = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "Sort CS L");
        sortCSC = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "Sort CS C");
        sortCSR = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "Sort CS R");
        busy= false;
    }

    @Override
    public void periodic() {
        // periodic logic (runs every loop)
//        getSorterTelemetryAdv();
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
//        ActiveOpMode.telemetry().addData("Sort L Detected", leftDetected());
//        ActiveOpMode.telemetry().addData("Sort C Detected", centerDetected());
//        ActiveOpMode.telemetry().addData("Sort R Detected", rightDetected());
        ActiveOpMode.telemetry().addData("Sort CS L [A,R,G,B]","" + sortCSL.alpha() + ", " + sortCSL.red() + ", " + sortCSL.green() + ", " + sortCSL.blue());
        ActiveOpMode.telemetry().addData("Sort CS C [A,R,G,B]","" + sortCSC.alpha() + ", " + sortCSC.red() + ", " + sortCSC.green() + ", " + sortCSC.blue());
        ActiveOpMode.telemetry().addData("Sort CS R [A,R,G,B]","" + sortCSR.alpha() + ", " + sortCSR.red() + ", " + sortCSR.green() + ", " + sortCSR.blue());


    }
}
