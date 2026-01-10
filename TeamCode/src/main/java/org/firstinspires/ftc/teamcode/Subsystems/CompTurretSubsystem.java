package org.firstinspires.ftc.teamcode.Subsystems;

import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.control.KalmanFilter;
import com.pedropathing.control.KalmanFilterParameters;
import com.qualcomm.robotcore.hardware.DcMotor;


import org.firstinspires.ftc.teamcode.Utility.OctoQuadFWv3;
import org.firstinspires.ftc.teamcode.Utility.TrapezoidProfileConstraints;
import org.firstinspires.ftc.teamcode.Utility.TrapezoidProfileElement;


import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.interpolators.InterpolatorElement;
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
    private double angleOffset = 201.72+3.78;
    private double turretTargetPos =0;
    private final OctoQuadFWv3.EncoderDataBlock data = new OctoQuadFWv3.EncoderDataBlock();
    boolean homed = false;
    double maxPower = 1;
    boolean inTrap = false;

    private CompTurretSubsystem() {}

    // put hardware, commands, etc here
    public MotorEx turretMotor = new MotorEx("Turret Motor").reversed();
    private InterpolatorElement interpolator = new TrapezoidProfileElement(new TrapezoidProfileConstraints(1, 100));


    private ControlSystem turretControlSystem = ControlSystem.builder()
            .posSquid(0.005, 0.0000000000,0.0)//.posPid(0.0393/2, 0.00000000001,0.02)
            .basicFF(0,0,0.75)
//            .interpolator(new TrapezoidProfileElement(new TrapezoidProfileConstraints(20, 100)))
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

    public Command turretRight = new LambdaCommand()
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

    public void turnTurretToFieldAngle(double botHeadingRad, double fieldAngleRad){
        double turretZeroHeading = botHeadingRad + Math.PI;
        turretTargetPos = Math.toDegrees(fieldAngleRad - turretZeroHeading);


    }


    @Override
    public void initialize() {
        Octo = ActiveOpMode.hardwareMap().get(OctoQuadFWv3.class, "OctoQuad");
        Octo.resetEverything();
        interpolator.reset();
        Octo.setChannelBankConfig(OctoQuadFWv3.ChannelBankConfig.ALL_PULSE_WIDTH);
        Octo.setSingleEncoderDirection(0, OctoQuadFWv3.EncoderDirection.FORWARD);
        Octo.setSingleChannelPulseWidthParams(0, new OctoQuadFWv3.ChannelPulseWidthParams(0,1024));
        Octo.setSingleChannelPulseWidthTracksWrap(0, true);
        Octo.saveParametersToFlash();
        Octo.resetSinglePosition(0);
        turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turretTargetPos=0;
        turretControlSystem.reset();
        homed = false;
        inTrap = false;



        // initialization logic (runs on init)
    }

    @Override
    public void periodic() {
        if(!ActiveOpMode.opModeInInit()){

            Octo.readAllEncoderData(data);
            calculatePos();
            if(turretTargetPos>200){
                inTrap = true;
                interpolator.reset();
                turretTargetPos= -180;
                interpolator.setGoal(new KineticState(turretTargetPos));
            }else if(turretTargetPos<-200){
                inTrap = true;
                interpolator.reset();
                turretTargetPos= 180;
                interpolator.setGoal(new KineticState(turretTargetPos));
            }
//            if(turretTargetPos>200){
//                turretTargetPos= 200;
//            }else if(turretTargetPos<-200){
//                turretTargetPos= -200;
//            }

            if(!homed){
                turretControlSystem.setGoal(new KineticState(turretTargetPos));
                turretMotor.setPower((turretControlSystem.calculate(new KineticState(calculatePos(), (data.velocities[0]) * DEGREES_PER_US))*0.6) * maxPower);
                if(Math.abs(turretPos-turretTargetPos)<4){
                    turretMotor.setPower(0);
                    homed = true;
                }
            }else if(!inTrap) {

                turretControlSystem.setGoal(new KineticState(turretTargetPos));


                turretMotor.setPower((turretControlSystem.calculate(new KineticState(calculatePos(), (data.velocities[0]) * DEGREES_PER_US)))*maxPower);
            }else{
                turretControlSystem.setGoal(interpolator.getCurrentReference());
                turretMotor.setPower(turretControlSystem.calculate(new KineticState(calculatePos(), (data.velocities[0]) * DEGREES_PER_US))*maxPower);
                if(Math.abs(interpolator.getGoal().component1()-calculatePos()) <20){
                    inTrap = false;
                }
            }
        }
        getTurretTelemetryAdv();
        // periodic logic (runs every loop)
    }
    public void getTurretTelemetryAdv(){
        PanelsTelemetry.INSTANCE.getTelemetry().addData("Turret Position", calculatePos());
        PanelsTelemetry.INSTANCE.getTelemetry().addData("Turret Target", turretTargetPos);
        ActiveOpMode.telemetry().addLine("-------------- Turret Telemetry Adv: --------------");
        ActiveOpMode.telemetry().addData("Turret Homed", homed);
        ActiveOpMode.telemetry().addData("Turret In Trap", inTrap);
        ActiveOpMode.telemetry().addData("Turret Position", calculatePos());
        ActiveOpMode.telemetry().addData("Turret Velo", data.velocities[0]);
        ActiveOpMode.telemetry().addData("Turret Target", turretTargetPos);
        ActiveOpMode.telemetry().addData("Turret Power", turretControlSystem.calculate(new KineticState(calculatePos(), (data.velocities[0]) * DEGREES_PER_US)));

    }



}
