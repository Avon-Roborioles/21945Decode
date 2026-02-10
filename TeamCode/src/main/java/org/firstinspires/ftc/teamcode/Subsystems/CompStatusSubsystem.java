package org.firstinspires.ftc.teamcode.Subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.PWMOutputEx;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.MovingStatistics;

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
@Configurable
public class CompStatusSubsystem implements Subsystem {
    public static final CompStatusSubsystem INSTANCE = new CompStatusSubsystem();
    private CompStatusSubsystem() {}
    public static double pwmTarget = 0;




    int brightness = 100 ;

    // put hardware, commands, etc here
    private AnalogInput floodgate ;
    private ServoImplEx beacon;
    public GoBildaPrismDriver prism;
    public ServoImplEx prismPwm;
    private CStatus left = CStatus.NULL;
    private CStatus center = CStatus.NULL;
    private CStatus right = CStatus.NULL;
    private OBPattern currentOBPattern = OBPattern.NULL;
    private VoltageSensor controlHubVoltageSensor;

    long prevTime = 0;
    long curTime = 0;
    long deltaTime = 0;
    Timing.Timer wait = new Timing.Timer(400, TimeUnit.MILLISECONDS);
    MovingStatistics statistics = new MovingStatistics(200);

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

    PrismAnimations.Solid leftSL = new PrismAnimations.Solid();
    PrismAnimations.Solid leftSB = new PrismAnimations.Solid();
    PrismAnimations.Solid leftSR = new PrismAnimations.Solid();
    PrismAnimations.Solid leftSF = new PrismAnimations.Solid();
    PrismAnimations.Solid rightSL = new PrismAnimations.Solid();
    PrismAnimations.Solid rightSB = new PrismAnimations.Solid();
    PrismAnimations.Solid rightSR = new PrismAnimations.Solid();
    PrismAnimations.Solid rightSF = new PrismAnimations.Solid();
    PrismAnimations.Solid centerSL = new PrismAnimations.Solid();
    PrismAnimations.Solid centerSB = new PrismAnimations.Solid();
    PrismAnimations.Solid centerSR = new PrismAnimations.Solid();
    PrismAnimations.Solid centerSF = new PrismAnimations.Solid();





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
        NULL,
        EPG,
        PEG,
        PPE,
        EGP,
        PEP,
        PGE,
        EPP,
        GEP,
        GPE,
        TWOE
    }
    public enum order {
        Center,
        Left,
        Right,
        Skip
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
        greenLeft.setPrimaryColor(0,0,255);
        greenLeft.setStartIndex(0);
        greenLeft.setStopIndex(11);

        //Green Middle Back
        greenMiddleBack.setBrightness(brightness);
        greenMiddleBack.setPrimaryColor(0,0,255);
        greenMiddleBack.setStartIndex(12);
        greenMiddleBack.setStopIndex(17);

        //Green Right
        greenRight.setBrightness(brightness);
        greenRight.setPrimaryColor(0,0,255);
        greenRight.setStartIndex(18);
        greenRight.setStopIndex(29);

        //Green Middle Front
        greenMiddleFront.setBrightness(brightness);
        greenMiddleFront.setPrimaryColor(0,0,255);
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

        leftSL.setBrightness(brightness);
        leftSL.setPrimaryColor(252, 51, 153);
        leftSL.setStartIndex(0);
        leftSL.setStopIndex(3);

        leftSB.setBrightness(brightness);
        leftSB.setPrimaryColor(252, 51, 153);
        leftSB.setStartIndex(12);
        leftSB.setStopIndex(13);

        leftSR.setBrightness(brightness);
        leftSR.setPrimaryColor(252, 51, 153);
        leftSR.setStartIndex(26);
        leftSR.setStopIndex(29);

        leftSF.setBrightness(brightness);
        leftSF.setPrimaryColor(252, 51, 153);
        leftSF.setStartIndex(34);
        leftSF.setStopIndex(35);

        centerSL.setBrightness(brightness);
        centerSL.setPrimaryColor(242, 171, 0);
        centerSL.setStartIndex(4);
        centerSL.setStopIndex(7);

        centerSB.setBrightness(brightness);
        centerSB.setPrimaryColor(242, 171, 0);
        centerSB.setStartIndex(14);
        centerSB.setStopIndex(15);

        centerSR.setBrightness(brightness);
        centerSR.setPrimaryColor(242, 171, 0);
        centerSR.setStartIndex(22);
        centerSR.setStopIndex(25);

        centerSF.setBrightness(brightness);
        centerSF.setPrimaryColor(242, 171, 0);
        centerSF.setStartIndex(32);
        centerSF.setStopIndex(33);

        rightSL.setBrightness(brightness);
        rightSL.setPrimaryColor(53, 239, 242);
        rightSL.setStartIndex(8);
        rightSL.setStopIndex(11);

        rightSB.setBrightness(brightness);
        rightSB.setPrimaryColor(53, 239, 242);
        rightSB.setStartIndex(16);
        rightSB.setStopIndex(17);

        rightSR.setBrightness(brightness);
        rightSR.setPrimaryColor(53, 239, 242);
        rightSR.setStartIndex(18);
        rightSR.setStopIndex(21);

        rightSF.setBrightness(brightness);
        rightSF.setPrimaryColor(53, 239, 242);
        rightSF.setStartIndex(30);
        rightSF.setStopIndex(31);








    }


    @Override
    public void initialize() {
        // initialization logic (runs on init)
        floodgate = ActiveOpMode.hardwareMap().get(AnalogInput.class, "FloodGate");
        beacon = ActiveOpMode.hardwareMap().get(ServoImplEx.class, "Beacon");
        prism = ActiveOpMode.hardwareMap().get(GoBildaPrismDriver.class, "Prism");
        prismPwm = ActiveOpMode.hardwareMap().get(ServoImplEx.class, "PrismPWM");
        prismPwm.setPwmRange(new PwmControl.PwmRange(500,2500));
        controlHubVoltageSensor = ActiveOpMode.hardwareMap().get(VoltageSensor.class, "Control Hub");
        buildAnimations();
        prism.setStripLength(36);
        prism.enableDefaultBootArtboard(true);
        prism.setDefaultBootArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);
        prism.clearAllAnimations();
        purpleSnakes.setBrightness(15);
        purpleSnakes.setSnakeLength(10);
        purpleSnakes.setColors(new Color(102,0,204));
        purpleSnakes.setSpeed(0.1F);
        purpleSnakes.setDirection(Direction.Forward);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0,purpleSnakes);
        prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);

        currentOBPattern = OBPattern.NULL;

        setUpLights();
        setPrismNorm();




    }
    public void setUpLights(){
        wait.start();
        while(!wait.done()) {

        }

        prism.clearAllAnimations();
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0,greenLeft);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1,greenRight);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2,greenMiddleBack);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3,greenMiddleFront);
        prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_1);

        wait.start();
        while(!wait.done()) {

        }

        prism.clearAllAnimations();
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0,leftSL);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1,leftSF);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2,leftSB);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3,leftSR);
        prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_2);

        wait.start();
        while(!wait.done()) {

        }

        prism.clearAllAnimations();
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0,rightSL);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1,rightSF);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2,rightSB);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3,rightSR);
        prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_3);

        wait.start();
        while(!wait.done()) {

        }

        prism.clearAllAnimations();
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0,centerSL);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1,centerSF);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2,centerSB);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3,centerSR);
        prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_4);

        wait.start();
        while(!wait.done()) {

        }

        prism.clearAllAnimations();
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0,leftSL);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1,leftSF);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2,leftSB);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3,leftSR);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_4,rightSL);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_5,rightSF);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_6,rightSB);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_7,rightSR);
        prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_5);

        wait.start();
        while(!wait.done()) {

        }

        prism.clearAllAnimations();
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0,centerSL);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1,centerSF);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2,centerSB);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3,centerSR);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_4,rightSL);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_5,rightSF);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_6,rightSB);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_7,rightSR);
        prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_6);

        wait.start();
        while(!wait.done()) {

        }

        prism.clearAllAnimations();
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0,leftSL);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1,leftSF);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2,leftSB);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3,leftSR);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_4,centerSL);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_5,centerSF);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_6,centerSB);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_7, centerSR);
        prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_7);

        wait.start();
        while(!wait.done()) {

        }
        setPrismNorm();














    }

    public long getLoopTimeMillis(){
        return deltaTime;
    }
    public double getLoopTimeSeconds(){
        return deltaTime/1000;
    }
    public double getLoopTimeHZ(){
        return 1e3/deltaTime;
    }

    @Override
    public void periodic() {
        // periodic logic (runs every loop)
        getStatusTelemetryAdv();

        curTime = System.currentTimeMillis();
        if (prevTime != 0)
        {
            deltaTime = curTime - prevTime;
            statistics.add(deltaTime);

        }
        prevTime = curTime;

//        setPrismToPWM((long) pwmTarget);
        if(ActiveOpMode.isStarted()){
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
        ActiveOpMode.telemetry().addData("Loop Hz", 1e3/statistics.getMean());
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
    public void setPrismToPWM(long PWM){
        if(PWM< 500){
            PWM = 500;
        }else if(PWM>2500){
            PWM = 2500;
        }
        PWM = PWM-500;
        prismPwm.setPosition(PWM/2000.0);

    }
    public void setPrismOrange(){
        setPrismToPWM(1115);

    }
    public void setPrismGreen(){
        setPrismToPWM(1400);

    }
    public void setPrismNorm(){
        setPrismToPWM(503);

    }
    public void setPrismOff(){
        setPrismToPWM(1060);
    }
    public void setPrismLeftOn(){
        setPrismToPWM(525);
    }
    public void setPrismRightOn(){
        setPrismToPWM(535);
    }
    public void setPrismCenterOn(){
        setPrismToPWM(545);
    }
    public void setPrismLeftAndRightOn(){
        setPrismToPWM(555);
    }
    public void setPrismLeftAndCenterOn(){
        setPrismToPWM(575);
    }
    public void setPrismRightAndCenterOn(){
        setPrismToPWM(565);
    }
    public void setPrismLeftAndRightAndCenterOn(){
        setPrismToPWM(515);
    }

    public void setLeftPurple(){
        if(!(left == CStatus.PURPLE)) {
            left = CStatus.PURPLE;
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, purpleLeft);
        }
    }
    public void setLeftGreen(){
        if(!(left == CStatus.GREEN)) {
            left = CStatus.GREEN;
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, greenLeft);
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
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, offLeft);
        }
    }
    public void setCenterPurple(){
        if(!(center == CStatus.PURPLE)) {
            center = CStatus.PURPLE;
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, purpleMiddleFront);
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2, purpleMiddleBack);
        }

    }
    public void setCenterGreen(){
        if(!(center == CStatus.GREEN)) {
            center = CStatus.GREEN;
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, greenMiddleFront);
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2, greenMiddleBack);
        }
    }
    public void setCenterError() {
        if(!(center == CStatus.ERROR)) {
            center = CStatus.ERROR;
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, errorMiddleFront);
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2, errorMiddleBack);
        }
    }
    public void setCenterOff() {
        if(!(center == CStatus.OFF)) {
            center = CStatus.OFF;
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, offMiddleFront);
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2, offMiddleBack);
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
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3, greenRight);
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
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3, offRight);
        }
    }

    public void setAllOff(){
        if(!(left == CStatus.OFF)) {
            left = CStatus.OFF;
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, offLeft);
        }
        if(!(center == CStatus.OFF)) {
            center = CStatus.OFF;
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, offMiddleFront);
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2, offMiddleBack);
        }
        if(!(right == CStatus.OFF)) {
            right = CStatus.OFF;
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3, offRight);
        }
    }
    public void setAllError() {
        if(!(left == CStatus.ERROR)) {
            left = CStatus.ERROR;
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, errorLeft);
        }
        if(!(center == CStatus.ERROR)) {
            center = CStatus.ERROR;
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, errorMiddleFront);
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2, errorMiddleBack);
        }
        if(!(right == CStatus.ERROR)) {
            right = CStatus.ERROR;
            prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3, errorRight);
        }
    }
    public void returnToDefault(){
        prism.clearAllAnimations();
        prism.insertAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0,purpleSnakes);
    }
    public void updatePrism(){
        prism.updateAllAnimations();
    }
    public void clearPrism(){
        prism.clearAllAnimations();
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
