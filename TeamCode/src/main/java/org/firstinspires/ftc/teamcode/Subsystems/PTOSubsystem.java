package org.firstinspires.ftc.teamcode.Subsystems;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.bindings.Range;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class PTOSubsystem implements Subsystem {
    public static final PTOSubsystem INSTANCE = new PTOSubsystem();
    private PTOSubsystem() {}
    double lUp = 0;
    double lDown = 0.7;
    double rUp = 0;
    double rDown = 0.7;
    boolean engaged = false;
    private static final double DEGREES_PER_US = (360 / 1024.0);
    private double angleOffsetL = 87;
    private double angleOffsetR = 340;
    public double ptoLeftPos = 0;
    public double ptoRightPos = 0;

    // put hardware, commands, etc here
    public ServoEx ptoL = new ServoEx("PTO L");
    public ServoEx ptoR = new ServoEx("PTO R");
    MotorEx fl, fr, bl,br;
    public Command disengage = new ParallelGroup(new SetPosition(ptoL, lUp).setIsDone(()->true), new SetPosition(ptoR, rUp).setIsDone(()->true), new LambdaCommand().setStart(() -> engaged = false).setIsDone(()->true));
    public Command engage = new ParallelGroup(new SetPosition(ptoL, lDown), new SetPosition(ptoR, rDown), new LambdaCommand().setStart(() -> engaged = true).setIsDone(()->true));

    public void Engage(){
        ptoL.setPosition(lDown);
        ptoR.setPosition(rDown);
        engaged = true;
    }
    public void Disengage(){
        ptoL.setPosition(lUp);
        ptoR.setPosition(rUp);
        engaged = false;
    }
    @Override
    public void initialize() {
        engaged = false;
        fr = new MotorEx("FrontRight");
        fl = new MotorEx("FrontLeft").reversed();
        br = new MotorEx("BackRight");
        bl = new MotorEx("BackLeft").reversed();


        // initialization logic (runs on init)
    }
    public double getPtoLPosition(){
        ptoLeftPos =((TurretSubsystem.INSTANCE.data.positions[1] * DEGREES_PER_US)- angleOffsetL);
        return ptoLeftPos;
    }
    public double getPtoRPosition(){
        ptoRightPos =((TurretSubsystem.INSTANCE.data.positions[2] * DEGREES_PER_US)- angleOffsetR);
        return ptoRightPos;
    }


    @Override
    public void periodic() {
        // periodic logic (runs every loop)
        getPTOTelemetryAdv();
    }
    public void getPTOTelemetryAdv(){
        ActiveOpMode.telemetry().addLine("-------------- PTO Telemetry Adv: --------------");
        ActiveOpMode.telemetry().addData("Engaged", engaged);
        ActiveOpMode.telemetry().addData("PTO L Pos", getPtoLPosition());
        ActiveOpMode.telemetry().addData("PTO R Pos", getPtoRPosition());



    }


}
