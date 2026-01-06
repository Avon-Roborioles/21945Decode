package org.firstinspires.ftc.teamcode.Subsystems;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.teamcode.Utility.OctoQuadFWv3;


import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.MotorEx;

public class CompTurretSubsystem implements Subsystem {
    public static final CompTurretSubsystem INSTANCE = new CompTurretSubsystem();
    OctoQuadFWv3 Octo;
    private double turretPos = 0;
    private static final double DEGREES_PER_US = (410.67 / 1024.0);
    private double angleOffset = 201.72;
    private double turretTargetPos =0;
    private final OctoQuadFWv3.EncoderDataBlock data = new OctoQuadFWv3.EncoderDataBlock();
    boolean homed = false;
    double maxPower = 0;
    private CompTurretSubsystem() {}

    // put hardware, commands, etc here
    public MotorEx turretMotor = new MotorEx("Turret Motor");


    private ControlSystem turretControlSystem = ControlSystem.builder()
            .posPid(0.015, 0.00000000001,0.046)//.posPid(0.0393/2, 0.00000000001,0.02)
//            .velPid(0,0,0)

            .build();

    public double getRotatePositionRaw(){
        return turretMotor.getCurrentPosition();
    }

    public void rotateSpeed(double speed){
        turretMotor.setPower(speed);
    }
    public double calculatePos(){
        turretPos = ((data.positions[0] * DEGREES_PER_US)- angleOffset);
        return turretPos;
    }

    public Command turretLeft = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                turretTargetPos -= 5;
            })
            .setUpdate(() -> {
                // Runs on update
            })
            .setStop(interrupted -> {
                // Runs on stop
            })
            .setIsDone(() -> true) // Returns if the command has finished
            .requires(this)
            .setInterruptible(true);

    public Command turretRight = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
                turretTargetPos += 5;
            })
            .setUpdate(() -> {
                // Runs on update
            })
            .setStop(interrupted -> {
                // Runs on stop
            })
            .setIsDone(() -> true) // Returns if the command has finished
            .requires(this)
            .setInterruptible(true);


    @Override
    public void initialize() {
        Octo = ActiveOpMode.hardwareMap().get(OctoQuadFWv3.class, "OctoQuad");
        Octo.resetEverything();
        Octo.setChannelBankConfig(OctoQuadFWv3.ChannelBankConfig.ALL_PULSE_WIDTH);
        Octo.setSingleEncoderDirection(0, OctoQuadFWv3.EncoderDirection.REVERSE);
        Octo.setSingleChannelPulseWidthParams(0, new OctoQuadFWv3.ChannelPulseWidthParams(0,1024));
        Octo.setSingleChannelPulseWidthTracksWrap(0, true);
        Octo.saveParametersToFlash();
        Octo.resetSinglePosition(0);
        turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turretTargetPos=0;
        turretControlSystem.reset();
        homed = false;


        // initialization logic (runs on init)
    }

    @Override
    public void periodic() {
        if(!ActiveOpMode.opModeInInit()){

            Octo.readAllEncoderData(data);
            calculatePos();
//            if(turretTargetPos>200){
//                turretTargetPos= -180;
//            }else if(turretTargetPos<-200){
//                turretTargetPos= 180;
//            }
            if(turretTargetPos>200){
                turretTargetPos= 200;
            }else if(turretTargetPos<-200){
                turretTargetPos= -200;
            }
            if(!homed){
                turretControlSystem.setGoal(new KineticState(turretTargetPos));
                turretMotor.setPower(turretControlSystem.calculate(new KineticState(calculatePos(), (data.velocities[0]) * DEGREES_PER_US))*0.5 * maxPower);
                if(Math.abs(turretPos-turretTargetPos)<4){
                    turretMotor.setPower(0);
                    homed = true;
                }
            }else {
                turretControlSystem.setGoal(new KineticState(turretTargetPos));
                turretMotor.setPower(turretControlSystem.calculate(new KineticState(calculatePos(), (data.velocities[0]) * DEGREES_PER_US * maxPower)));
            }
        }
//        getTurretTelemetryAdv();
        // periodic logic (runs every loop)
    }
    public void getTurretTelemetryAdv(){
        ActiveOpMode.telemetry().addLine("-------------- Turret Telemetry Adv: --------------");
        ActiveOpMode.telemetry().addData("Turret Homed", homed);
        ActiveOpMode.telemetry().addData("Turret Position", calculatePos());
        ActiveOpMode.telemetry().addData("Turret Velo", data.velocities[0]);
        ActiveOpMode.telemetry().addData("Turret Target", turretTargetPos);
    }
}
