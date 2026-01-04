package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Utility.Prism.GoBildaPrismDriver;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

public class CompStatusSubsystem implements Subsystem {
    public static final CompStatusSubsystem INSTANCE = new CompStatusSubsystem();
    private CompStatusSubsystem() {}

    // put hardware, commands, etc here
    private AnalogInput floodgate ;
    private ServoImplEx beacon;
    GoBildaPrismDriver prism;

    @Override
    public void initialize() {
        // initialization logic (runs on init)
        floodgate = ActiveOpMode.hardwareMap().get(AnalogInput.class, "FloodGate");
        beacon = ActiveOpMode.hardwareMap().get(ServoImplEx.class, "Beacon");
        prism = ActiveOpMode.hardwareMap().get(GoBildaPrismDriver.class, "Prism");
        prism.setStripLength(36);
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
    public double getFloodGateCurrent(){
        return (floodgate.getVoltage()/3.3)*80;
    }

    public void getStatusTelemetryAdv(){
        ActiveOpMode.telemetry().addLine("-------------- Status Telemetry Adv: --------------");
        ActiveOpMode.telemetry().addData("Current Draw", getFloodGateCurrent());
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







}
