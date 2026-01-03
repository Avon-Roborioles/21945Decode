package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.AnalogInput;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

public class CompStatusSubsystem implements Subsystem {
    public static final CompStatusSubsystem INSTANCE = new CompStatusSubsystem();
    private CompStatusSubsystem() {}

    // put hardware, commands, etc here
    private AnalogInput floodgate ;

    @Override
    public void initialize() {
        // initialization logic (runs on init)
        floodgate = ActiveOpMode.hardwareMap().get(AnalogInput.class, "FloodGate");
    }

    @Override
    public void periodic() {
        // periodic logic (runs every loop)
        getStatusTelemetryAdv();
    }
    public double getFloodGateCurrent(){
        return (floodgate.getVoltage()/3.3)*80;
    }
    public void getStatusTelemetryAdv(){
        ActiveOpMode.telemetry().addLine("-------------- Status Telemetry Adv: --------------");
        ActiveOpMode.telemetry().addData("Current Draw", getFloodGateCurrent());
    }
}
