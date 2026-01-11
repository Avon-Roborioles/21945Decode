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
    private double angleOffset = 204.2;
    private double turretTargetPos =0;
    private double turretFieldAngleGoal = 180;
    private double distancePastFlip = 0;
    private double lastFlipAngle = 0;
    private double lastFlipGoal = 0;
    private final OctoQuadFWv3.EncoderDataBlock data = new OctoQuadFWv3.EncoderDataBlock();
    double maxPower = 1;
    double power = 0;

    private CompTurretSubsystem() {}

    // put hardware, commands, etc here
    public MotorEx turretMotor = new MotorEx("Turret Motor").reversed();
    private InterpolatorElement interpolator = new TrapezoidProfileElement(new TrapezoidProfileConstraints(20, 10));


    private ControlSystem turretControlSystem = ControlSystem.builder()
            .posSquid(0.01, 0.00000000005,0.036)
            .basicFF(0,0,0.29)
            .build();

    public double getRotatePositionRaw(){
        return turretMotor.getCurrentPosition();
    }

    public void rotateSpeed(double speed){
        turretMotor.setPower(speed);
    }
    public double calculatePos(){
        Octo.readAllEncoderData(data);
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

    public void moveTurretByAngle(double input){
//        turretFieldAngleGoal += input*5;
        turretTargetPos += input * 5;

    }

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
        power = 0;



        // initialization logic (runs on init)
    }

    @Override
    public void periodic() {
        turretControlSystem.reset();
        calculatePos();
        if(!ActiveOpMode.opModeInInit()){

            if(turretTargetPos>200){
                lastFlipAngle = turretTargetPos;
                distancePastFlip = turretTargetPos-200;

                turretTargetPos = (-160 + (turretTargetPos-200));
                lastFlipGoal = turretTargetPos;

            }else if(turretTargetPos<-200){
                lastFlipAngle = turretTargetPos;
                distancePastFlip = turretTargetPos+200;
                turretTargetPos = ( 160 + (turretTargetPos+200));
                lastFlipGoal = turretTargetPos;
            }

            turretControlSystem.setGoal(new KineticState(turretTargetPos));
            power = turretControlSystem.calculate(new KineticState(turretPos, (data.velocities[0]) * DEGREES_PER_US));
            if (Math.abs(power) > 0.175){
                turretMotor.setPower(power*maxPower);
            }else{
                turretMotor.setPower(0);
            }
        }
        getTurretTelemetryAdv();
        // periodic logic (runs every loop)
    }
    public void getTurretTelemetryAdv(){
        PanelsTelemetry.INSTANCE.getTelemetry().addData("Turret Position", turretPos);
        PanelsTelemetry.INSTANCE.getTelemetry().addData("Turret Target", turretTargetPos);
        ActiveOpMode.telemetry().addLine("-------------- Turret Telemetry Adv: --------------");
        ActiveOpMode.telemetry().addData("Turret Position", turretPos);
        ActiveOpMode.telemetry().addData("Turret Velo", data.velocities[0]);
        ActiveOpMode.telemetry().addData("Turret Power", turretControlSystem.calculate(new KineticState(turretPos, (data.velocities[0]) * DEGREES_PER_US)));
        ActiveOpMode.telemetry().addData("Turret Error", Math.abs(turretPos-turretTargetPos));
        ActiveOpMode.telemetry().addData("Turret Target", turretTargetPos);
        ActiveOpMode.telemetry().addData("Last Flip Angle", lastFlipAngle);
        ActiveOpMode.telemetry().addData("Distance Past Flip", distancePastFlip);
        ActiveOpMode.telemetry().addData("Last Flip Goal", lastFlipGoal);

    }



}
