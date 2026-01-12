package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.Utility.Prism.Color;
import org.firstinspires.ftc.teamcode.Utility.Prism.Direction;
import org.firstinspires.ftc.teamcode.Utility.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.Utility.Prism.PrismAnimations;
import org.firstinspires.ftc.teamcode.Utility.Timing;

import java.util.concurrent.TimeUnit;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

public class CompStatusSubsystem implements Subsystem {
    public static final CompStatusSubsystem INSTANCE = new CompStatusSubsystem();
    private CompStatusSubsystem() {}

    int brightness = 10 ;

    // put hardware, commands, etc here
    private AnalogInput floodgate ;
    private ServoImplEx beacon;
    public GoBildaPrismDriver prism;
    private CStatus left = CStatus.NULL;
    private CStatus center = CStatus.NULL;
    private CStatus right = CStatus.NULL;
    private OBPattern currentOBPattern = OBPattern.NULL;
    private VoltageSensor controlHubVoltageSensor;


    PrismAnimations.Snakes purpleSnakes = new PrismAnimations.Snakes();
    PrismAnimations.Solid purpleLeft = new PrismAnimations.Solid();
    PrismAnimations.Solid purpleMiddleBack = new PrismAnimations.Solid();
    PrismAnimations.Solid purpleMiddleFront = new PrismAnimations.Solid();
    PrismAnimations.Solid purpleRight = new PrismAnimations.Solid();
    PrismAnimations.Solid greenLeft = new PrismAnimations.Solid();
    PrismAnimations.Solid greenMiddleBack = new PrismAnimations.Solid();
    PrismAnimations.Solid greenMiddleFront = new PrismAnimations.Solid();
    PrismAnimations.Solid greenRight = new PrismAnimations.Solid();
    PrismAnimations.Solid offLeft = new PrismAnimations.Solid();
    PrismAnimations.Solid offMiddleBack = new PrismAnimations.Solid();
    PrismAnimations.Solid offMiddleFront = new PrismAnimations.Solid();
    PrismAnimations.Solid offRight = new PrismAnimations.Solid();
    PrismAnimations.Solid errorLeft = new PrismAnimations.Solid();
    PrismAnimations.Solid errorMiddleBack = new PrismAnimations.Solid();
    PrismAnimations.Solid errorMiddleFront = new PrismAnimations.Solid();
    PrismAnimations.Solid errorRight = new PrismAnimations.Solid();

    Timing.Timer delay = new Timing.Timer(3000, TimeUnit.MILLISECONDS);
    private enum CStatus{
        PURPLE,
        GREEN,
        OFF,
        ERROR,
        NULL
    }

    public enum OBPattern{
        PPG,
        PGP,
        GPP,
        NULL
    }


    private void buildAnimations(){


        //Purple Left
        purpleLeft.setBrightness(brightness);
        purpleLeft.setPrimaryColor(162,0,225);
        purpleLeft.setStartIndex(0);
        purpleLeft.setStopIndex(11);

        //Purple Middle Back
        purpleMiddleBack.setBrightness(brightness);
        purpleMiddleBack.setPrimaryColor(162,0,225);
        purpleMiddleBack.setStartIndex(12);
        purpleMiddleBack.setStopIndex(17);

        //Purple Right
        purpleRight.setBrightness(brightness);
        purpleRight.setPrimaryColor(162,0,225);
        purpleRight.setStartIndex(18);
        purpleRight.setStopIndex(29);

        //Purple Middle Front
        purpleMiddleFront.setBrightness(brightness);
        purpleMiddleFront.setPrimaryColor(162,0,225);
        purpleMiddleFront.setStartIndex(30);
        purpleMiddleFront.setStopIndex(36);

        //Green Left
        greenLeft.setBrightness(brightness);
        greenLeft.setPrimaryColor(0,255,0);
        greenLeft.setStartIndex(0);
        greenLeft.setStopIndex(11);

        //Green Middle Back
        greenMiddleBack.setBrightness(brightness);
        greenMiddleBack.setPrimaryColor(0,255,0);
        greenMiddleBack.setStartIndex(12);
        greenMiddleBack.setStopIndex(17);

        //Green Right
        greenRight.setBrightness(brightness);
        greenRight.setPrimaryColor(0,255,0);
        greenRight.setStartIndex(18);
        greenRight.setStopIndex(29);

        //Green Middle Front
        greenMiddleFront.setBrightness(brightness);
        greenMiddleFront.setPrimaryColor(0,255,0);
        greenMiddleFront.setStartIndex(30);
        greenMiddleFront.setStopIndex(36);

        //Off Left
        offLeft.setBrightness(0);
        offLeft.setPrimaryColor(0,0,0);
        offLeft.setStartIndex(0);
        offLeft.setStopIndex(11);

        //Off Middle Back
        offMiddleBack.setBrightness(0);
        offMiddleBack.setPrimaryColor(0,0,0);
        offMiddleBack.setStartIndex(12);
        offMiddleBack.setStopIndex(17);

        //Off Right
        offRight.setBrightness(0);
        offRight.setPrimaryColor(0,0,0);
        offRight.setStartIndex(18);
        offRight.setStopIndex(29);

        //Off Middle Front
        offMiddleFront.setBrightness(0);
        offMiddleFront.setPrimaryColor(0,0,0);
        offMiddleFront.setStartIndex(30);
        offMiddleFront.setStopIndex(36);

        //Error Left
        errorLeft.setBrightness(brightness);
        errorLeft.setPrimaryColor(255,128,0);
        errorLeft.setStartIndex(0);
        errorLeft.setStopIndex(11);

        //Error Middle Back
        errorMiddleBack.setBrightness(brightness);
        errorMiddleBack.setPrimaryColor(255,128,0);
        errorMiddleBack.setStartIndex(12);
        errorMiddleBack.setStopIndex(17);

        //Error Right
        errorRight.setBrightness(brightness);
        errorRight.setPrimaryColor(255,128,0);
        errorRight.setStartIndex(18);
        errorRight.setStopIndex(29);

        //Error Middle Front
        errorMiddleFront.setBrightness(brightness);
        errorMiddleFront.setPrimaryColor(255,128,0);
        errorMiddleFront.setStartIndex(30);
        errorMiddleFront.setStopIndex(36);
    }


    @Override
    public void initialize() {
        // initialization logic (runs on init)
        floodgate = ActiveOpMode.hardwareMap().get(AnalogInput.class, "FloodGate");
        beacon = ActiveOpMode.hardwareMap().get(ServoImplEx.class, "Beacon");
        prism = ActiveOpMode.hardwareMap().get(GoBildaPrismDriver.class, "Prism");
        controlHubVoltageSensor = ActiveOpMode.hardwareMap().get(VoltageSensor.class, "Control Hub");
        buildAnimations();
        prism.setStripLength(36);
        prism.enableDefaultBootArtboard(true);
        prism.setDefaultBootArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);
        prism.clearAllAnimations();
        purpleSnakes.setBrightness(5);
        purpleSnakes.setSnakeLength(10);
        purpleSnakes.setColors(new Color(160,32,225));
        purpleSnakes.setSpeed(0.1F);
        purpleSnakes.setDirection(Direction.Forward);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0,purpleSnakes);
        prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);
        currentOBPattern = OBPattern.NULL;



    }

    @Override
    public void periodic() {
        // periodic logic (runs every loop)
        getStatusTelemetryAdv();

        if(ActiveOpMode.isStarted()){
            //green
            beacon.setPosition(0.5);
        }else {
            //blue
            beacon.setPosition(0.6);
        }
    }
    public void setCurrentOBPattern(OBPattern pattern){
        currentOBPattern = pattern;
    }
    public OBPattern getCurrentOBPattern(){
        return currentOBPattern;
    }
    public void cycleOBPattern(){
        switch (currentOBPattern){
            case NULL:
                setCurrentOBPattern(OBPattern.PPG);
                break;
            case PPG:
                setCurrentOBPattern(OBPattern.PGP);
                break;
            case PGP:
                setCurrentOBPattern(OBPattern.GPP);
                break;
            case GPP:
                setCurrentOBPattern(OBPattern.NULL);
                break;

        }
    }
    public Command cycleOBPatternCommand = new LambdaCommand()
            .setStart(() -> {
                // Runs on start

            })
            .setUpdate(() -> {
                cycleOBPattern();
                // Runs on update
            })
            .setStop(interrupted -> {
                // Runs on stop
            })
            .setIsDone(() -> true) // Returns if the command has finished
            .requires(this)
            .setInterruptible(true);


    public double getFloodGateCurrent(){
        return (floodgate.getVoltage()/3.3)*80;
    }

    public void getStatusTelemetryAdv(){
        ActiveOpMode.telemetry().addLine("-------------- Status Telemetry Adv: --------------");
        ActiveOpMode.telemetry().addData("Current Draw", getFloodGateCurrent());
        ActiveOpMode.telemetry().addData("Voltage", controlHubVoltageSensor.getVoltage());
        ActiveOpMode.telemetry().addData("Current OB Pattern", currentOBPattern);
    }
    // ---------- Beacon Methods ----------
    public void setBeacon(double position){
        beacon.setPosition(position);
    }
    public void setBeaconOff(){
        beacon.setPosition(0);
    }
    public void setBeaconRed(){
        beacon.setPosition(0.277);
    }
    public void setBeaconOrange(){
        beacon.setPosition(0.333);
    }
    public void setBeaconYellow(){
        beacon.setPosition(0.388);
    }
    public void setBeaconSage(){
        beacon.setPosition(0.444);
    }
    public void setBeaconGreen(){
        beacon.setPosition(0.5);
    }
    public void setBeaconAzure(){
        beacon.setPosition(0.555);
    }
    public void setBeaconBlue(){
        beacon.setPosition(0.611);
    }
    public void setBeaconIndigo(){
        beacon.setPosition(0.666);
    }
    public void setBeaconViolet(){
        beacon.setPosition(0.722);
    }
    public void setBeaconWhite(){
        beacon.setPosition(1);
    }

    public void setLeftPurple(){
        if(!(left == CStatus.PURPLE)) {
            left = CStatus.PURPLE;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, purpleLeft);
        }
    }
    public void setLeftGreen(){
        if(!(left == CStatus.GREEN)) {
            left = CStatus.GREEN;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, greenLeft);
        }
    }
    public void setLeftError(){
        if(!(left == CStatus.ERROR)) {
            left = CStatus.ERROR;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, errorLeft);
        }
    }

    public void setLeftOff() {
        if(!(left == CStatus.OFF)) {
            left = CStatus.OFF;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, offLeft);
        }
    }
    public void setCenterPurple(){
        if(!(center == CStatus.PURPLE)) {
            center = CStatus.PURPLE;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, purpleMiddleFront);
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2, purpleMiddleBack);
        }

    }
    public void setCenterGreen(){
        if(!(center == CStatus.GREEN)) {
            center = CStatus.GREEN;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, greenMiddleFront);
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2, greenMiddleBack);
        }
    }
    public void setCenterError() {
        if(!(center == CStatus.ERROR)) {
            center = CStatus.ERROR;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, errorMiddleFront);
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2, errorMiddleBack);
        }
    }
    public void setCenterOff() {
        if(!(center == CStatus.OFF)) {
            center = CStatus.OFF;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, offMiddleFront);
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2, offMiddleBack);
        }
    }
    public void setRightPurple() {
        if(!(right == CStatus.PURPLE)) {
            right = CStatus.PURPLE;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3, purpleRight);
        }

    }
    public void setRightGreen() {
        if(!(right == CStatus.GREEN)) {
            right = CStatus.GREEN;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3, greenRight);
        }
    }
    public void setRightError(){
        if(!(right == CStatus.ERROR)) {
            right = CStatus.ERROR;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3, errorRight);
        }
    }
    public void setRightOff() {
        if(!(right == CStatus.OFF)) {
            right = CStatus.OFF;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3, offRight);
        }
    }

    public void setAllOff(){
        if(!(left == CStatus.OFF)) {
            left = CStatus.OFF;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, offLeft);
        }
        if(!(center == CStatus.OFF)) {
            center = CStatus.OFF;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, offMiddleFront);
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2, offMiddleBack);
        }
        if(!(right == CStatus.OFF)) {
            right = CStatus.OFF;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3, offRight);
        }
    }
    public void setAllError() {
        if(!(left == CStatus.ERROR)) {
            left = CStatus.ERROR;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, errorLeft);
        }
        if(!(center == CStatus.ERROR)) {
            center = CStatus.ERROR;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, errorMiddleFront);
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2, errorMiddleBack);
        }
        if(!(right == CStatus.ERROR)) {
            right = CStatus.ERROR;
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3, errorRight);
        }
    }
    public void returnToDefault(){
        prism.clearAllAnimations();
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0,purpleSnakes);
    }

    public Command sorterLight= new LambdaCommand()
            .setStart(() -> {
                // Runs on start
               delay.start();


            })
            .setUpdate(() -> {
                switch (CompSorterSubsystem.INSTANCE.leftSlot()){
                    case PURPLE:
                        setLeftPurple();
                        break;
                    case GREEN:
                        setLeftGreen();
                        break;
                    case UNKNOWN:
                        setLeftError();
                        break;
                    case EMPTY:
                        setLeftOff();
                        break;
                }
                switch (CompSorterSubsystem.INSTANCE.centerSlot()){
                    case PURPLE:
                        setCenterPurple();
                        break;
                    case GREEN:
                        setCenterGreen();
                        break;
                    case UNKNOWN:
                        setCenterError();
                        break;
                    case EMPTY:
                        setCenterOff();
                        break;
                }
                switch (CompSorterSubsystem.INSTANCE.rightSlot()){
                    case PURPLE:
                        setRightPurple();
                        break;
                    case GREEN:
                        setRightGreen();
                        break;
                    case UNKNOWN:
                        setRightError();
                        break;
                    case EMPTY:
                        setRightOff();
                        break;
                }

                // Runs on update
            })
            .setStop(interrupted -> {
                // Runs on stop

            })
            .setIsDone(() -> delay.done()) // Returns if the command has finished
            .requires()
            .setInterruptible(false);
}
