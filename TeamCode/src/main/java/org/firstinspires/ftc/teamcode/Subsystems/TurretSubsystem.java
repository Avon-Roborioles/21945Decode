package org.firstinspires.ftc.teamcode.Subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.hardware.DcMotor;


import org.firstinspires.ftc.teamcode.Utility.OctoQuadFWv3;
import org.firstinspires.ftc.teamcode.Utility.TrapezoidProfileConstraints;
import org.firstinspires.ftc.teamcode.Utility.TrapezoidProfileElement;


import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.interpolators.InterpolatorElement;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.VoltageCompensatingMotor;
@Configurable
public class TurretSubsystem implements Subsystem {
    public static final TurretSubsystem INSTANCE = new TurretSubsystem();
    OctoQuadFWv3 Octo;
    private double turretPos = 0;
    private static final double DEGREES_PER_US = (410.67 / 1024.0);
    private double angleOffset = 204.2 - 0.8;
    private double turretTargetPosDeg =0;
    public static double abTurretTargetTune = 0;
    public static boolean abTuneMode = true;
    public static boolean powerMode = false;
    private double turretFieldAngleGoalDeg = 90;
    private double botHeadingRad = 0;
    double turretZeroHeadingRad = 0;
    double turretError = 0;
    private final OctoQuadFWv3.EncoderDataBlock data = new OctoQuadFWv3.EncoderDataBlock();
    double maxPower = 1;
    double power = 0;
    public static double powerAdd = 0;
    public static double powerSet = 0;

    boolean turretOn = false;

    public static double akp =0.0005;
    public static double akI =0;
    public static double akD = 0;
    public static double akS = 0;
    public static double akF = 0;


    public static double powerAdd_N200_N165 = -0.18;
    public static double powerAdd_N165_N145 = -0.09;
    public static double powerAdd_N145_N35 = 0;
    public static double powerAdd_N35_65 = 0.08;
    public static double powerAdd_65_190 = 0.09;
    public static double powerAdd_190_200 = 0.188;



    public static double kS_N200_N190 = 0.098;//-200 -> -190
    public static double kS_N190_N180 = 0.094;//-190 -> -180
    public static double kS_N180_N170 = 0.105;//-180 -> -170
    public static double kS_N170_N160 = 0.108;//-170 -> -160
    public static double kS_N160_N150 = 0.106;//-160 -> -150
    public static double kS_N150_N140 = 0.094;//-150 -> -140
    public static double kS_N140_N130 = 0.091;//-140 -> -130
    public static double kS_N130_N120 = 0.086;//-130 -> -120
    public static double kS_N120_N110 = 0.086;//-120 -> -110
    public static double kS_N110_N100 = 0.087;//-110 -> -100
    public static double kS_N100_N90 = 0.087;//-100 -> -90
    public static double kS_N90_N80 = 0.087;//-90 -> -80
    public static double kS_N80_N70 = 0.084;//-80 -> -70
    public static double kS_N70_N60 = 0.084;//-70 -> -60
    public static double kS_N60_N50 = 0.080;//-60 -> -50
    public static double kS_N50_N40 = 0.083;//-50 -> -40
    public static double kS_N40_N30 = 0.086;//-40 -> -30
    public static double kS_N30_N20 = 0.086;//-30 -> -20
    public static double kS_N20_N10 = 0.083;//-20 -> -10
    public static double kS_N10_0 = 0.086;//-10 -> 0
    public static double kS_0_10 = 0.086;//0 -> 10
    public static double kS_10_20 = 0.094;//10 -> 20
    public static double kS_20_30 = 0.109;//20 -> 30
    public static double kS_30_40 = 0.112;//30 -> 40
    public static double kS_40_50 = 0.124;//40 -> 50
    public static double kS_50_60 = 0.136;//50 -> 60
    public static double kS_60_70 = 0.142;//60 -> 70
    public static double kS_70_80 = 0.163;//70 -> 80
    public static double kS_80_90 = 0.165;//80 -> 90
    public static double kS_90_100 = 0.177;//90 -> 100
    public static double kS_100_110 = 0.168;//100 -> 110
    public static double kS_110_120 = 0.148;//110 -> 120
    public static double kS_120_130 = 0.134;//120 -> 130
    public static double kS_130_140 = 0.133;//130 -> 140
    public static double kS_140_150 = 0.125;//140 -> 150
    public static double kS_150_160 = 0.118;//150 -> 160
    public static double kS_160_170 = 0.118;//160 -> 170
    public static double kS_170_180 = 0.118;//170 -> 180
    public static double kS_180_190 = 0.118;//180 -> 190
    public static double kS_190_200 = 0.114;//190 -> 200

    








    private PIDCoefficients coefficients = new PIDCoefficients(akp, akI, akD);

    double lastSetPoint = 0;
    public static double kv = 0.0;

    private TurretSubsystem() {}

    // put hardware, commands, etc here
    public MotorEx turretMotorEx = new MotorEx("Turret Motor");

    public VoltageCompensatingMotor turretMotor = new VoltageCompensatingMotor( turretMotorEx , 0.25, 13 );
    private InterpolatorElement interpolator = new TrapezoidProfileElement(new TrapezoidProfileConstraints(20, 10));


    private ControlSystem turretControlSystem;
    private ControlSystem smallTurretSystem;

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
                turretTargetPosDeg += 5;
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
                turretTargetPosDeg -= 5;
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
    public void turnTurretToFieldAngleAuto(double fieldAngleRad, double botHeadingRad) {
        if (turretPos < 0 && botHeadingRad > 0) {
            botHeadingRad = -Math.PI + (botHeadingRad - Math.PI);
        } else if (turretPos > 0 && botHeadingRad < 0) {
            botHeadingRad = Math.PI + (botHeadingRad + Math.PI);
        }

        turretZeroHeadingRad = botHeadingRad + Math.PI;
        //400 degree flip
        if (fieldAngleRad > (turretZeroHeadingRad + Math.toRadians(200))) {
            fieldAngleRad -= Math.toRadians(360);
        } else if (fieldAngleRad < (turretZeroHeadingRad - Math.toRadians(200))) {
            fieldAngleRad += Math.toRadians(360);
        }
        //200 Degree Limit


        turretTargetPosDeg = - Math.toDegrees(fieldAngleRad - turretZeroHeadingRad);
    }


    public void turnTurretToFieldAngle(double fieldAngleRad){
        botHeadingRad = PedroComponent.follower().getPose().getHeading();
        if(turretPos < 0 && botHeadingRad > 0){
            botHeadingRad = -Math.PI + (botHeadingRad - Math.PI);
        }else if (turretPos > 0 && botHeadingRad < 0) {
            botHeadingRad = Math.PI + (botHeadingRad + Math.PI);
        }

        turretZeroHeadingRad = botHeadingRad + Math.PI;
        //400 degree flip
        if (fieldAngleRad > (turretZeroHeadingRad + Math.toRadians(200))){
            fieldAngleRad -=Math.toRadians(360);
        }else if (fieldAngleRad < (turretZeroHeadingRad - Math.toRadians(200))) {
            fieldAngleRad +=Math.toRadians(360);
        }
        //200 Degree Limit


        turretTargetPosDeg = - Math.toDegrees(fieldAngleRad - turretZeroHeadingRad);

    }

    public void moveTurretJoystick(double joystickValue, double joystick2){
        turretTargetPosDeg += joystickValue *2.5;
        if (Math.abs(joystick2) > 0.1){
            turretTargetPosDeg = calculatePos()+ joystick2 * 10;
        }
        lastSetPoint = turretTargetPosDeg;
    }

    public boolean TurretHappy(){
        return turretControlSystem.isWithinTolerance(new KineticState(3));
    }
    public boolean turretFine(){
        return turretControlSystem.isWithinTolerance(new KineticState(5));
    }

    public void turnTurretOn(){
        turretOn = true;
    }
    public void turnTurretOff(){
        turretOn = false;
    }







    @Override
    public void initialize() {
        turretMotorEx.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turretControlSystem = ControlSystem.builder()
                .posSquID(coefficients)
//                .posFilter()

                .build();


        Octo = ActiveOpMode.hardwareMap().get(OctoQuadFWv3.class, "OctoQuad");
        Octo.resetEverything();
        interpolator.reset();
        Octo.setChannelBankConfig(OctoQuadFWv3.ChannelBankConfig.ALL_PULSE_WIDTH);
        Octo.setSingleEncoderDirection(0, OctoQuadFWv3.EncoderDirection.FORWARD);
        Octo.setSingleChannelPulseWidthParams(0, new OctoQuadFWv3.ChannelPulseWidthParams(0,1024));
        Octo.setSingleChannelPulseWidthTracksWrap(0, true);
        Octo.saveParametersToFlash();
        Octo.resetSinglePosition(0);
        turretMotorEx.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turretTargetPosDeg =0;
        turretControlSystem.reset();
        power = 0;
        turretFieldAngleGoalDeg = 90;
        lastSetPoint = turretFieldAngleGoalDeg;
        coefficients.kI= akI;
        coefficients.kP= akp;
        coefficients.kD= akD;
        turretOn = false;


        // initialization logic (runs on init)
    }

    @Override
    public void periodic() {
        calculatePos();


        if(!ActiveOpMode.opModeInInit()){
            if (abTuneMode){
                turretOn = true;
                turretTargetPosDeg = abTurretTargetTune;
            }
            //400 Degree Flip
            if(turretTargetPosDeg>200){
                turretTargetPosDeg= (-160 + (turretTargetPosDeg-200));
            }else if(turretTargetPosDeg<-200){
                turretTargetPosDeg= ( 160 + (turretTargetPosDeg+200));
            }

            turretControlSystem.setGoal(new KineticState(turretTargetPosDeg));
            coefficients.kD= akD;
            if (Math.abs(turretPos-turretTargetPosDeg)<5){
                coefficients.kI= akI;
            }else{
                coefficients.kI= 0;
            }

            coefficients.kP= akp;
            if(turretPos<-190){
                akS=kS_N200_N190;
            }else if(turretPos< -180){
                akS=kS_N190_N180;
            }else if(turretPos<-170){
                akS = kS_N180_N170;
            }else if(turretPos<-160){
                akS = kS_N170_N160;
            }else if(turretPos<-150){
                akS = kS_N160_N150;
            }else if(turretPos<-140){
                akS = kS_N150_N140;
            }else if(turretPos<-130){
                akS = kS_N140_N130;
            }else if(turretPos<-120){
                akS = kS_N130_N120;
            }else if(turretPos<-110){
                akS = kS_N120_N110;
            }else if(turretPos<-100){
                akS = kS_N110_N100;
            }else if(turretPos<-90){
                akS = kS_N100_N90;
            }else if(turretPos<-80){
                akS = kS_N90_N80;
            }else if(turretPos<-70){
                akS = kS_N80_N70;
            }else if(turretPos<-60){
                akS = kS_N70_N60;
            }else if(turretPos<-50){
                akS = kS_N60_N50;
            }else if(turretPos<-40){
                akS = kS_N50_N40;
            }else if(turretPos<-30){
                akS = kS_N40_N30;
            }else if(turretPos<-20){
                akS = kS_N30_N20;
            }else if(turretPos<-10){
                akS = kS_N20_N10;
            }else if(turretPos<0){
                akS = kS_N10_0;
            }else if(turretPos<10){
                akS = kS_0_10;
            }else if(turretPos<20){
                akS = kS_10_20;
            }else if(turretPos<30){
                akS = kS_20_30;
            }else if(turretPos<40){
                akS = kS_30_40;
            }else if(turretPos<50){
                akS = kS_40_50;
            }else if(turretPos<60){
                akS = kS_50_60;
            }else if(turretPos<70){
                akS = kS_60_70;
            }else if(turretPos<80){
                akS = kS_70_80;
            }else if(turretPos<90){
                akS = kS_80_90;
            }else if(turretPos<100){
                akS = kS_90_100;
            }else if(turretPos<110){
                akS = kS_100_110;
            }else if(turretPos<120){
                akS = kS_110_120;
            }else if(turretPos<130){
                akS = kS_120_130;
            }else if(turretPos<140){
                akS = kS_130_140;
            }else if(turretPos<150){
                akS = kS_140_150;
            }else if(turretPos<160){
                akS = kS_150_160;
            }else if(turretPos<170){
                akS = kS_160_170;
            }else if(turretPos<180){
                akS = kS_170_180;
            }else if(turretPos<190){
                akS = kS_180_190;
            }else if(turretPos<200) {
                akS = kS_190_200;
            }
            turretError = turretTargetPosDeg-turretPos;

            if(turretPos <-165){
                powerAdd = powerAdd_N200_N165;
            }else if(turretPos <-145){
                powerAdd = powerAdd_N165_N145;
            }else if(turretPos <-35){
                powerAdd = powerAdd_N145_N35;
            }else if(turretPos < 65){
                powerAdd = powerAdd_N35_65;
            }else if(turretPos < 190){
                powerAdd = powerAdd_65_190;
            }else{
                powerAdd = powerAdd_190_200;
            }


            power = turretControlSystem.calculate(new KineticState(turretPos, (data.velocities[0]) * DEGREES_PER_US)) + ( kv * (turretTargetPosDeg - lastSetPoint));
            power += Math.signum(turretError)* akS;
            power += powerAdd;
            if(powerMode){
                power = powerSet;
            }
            if (turretOn && Math.abs(power) > 0.01 && Math.abs(turretError)> 0){
                turretMotor.setPower(power*maxPower );
            }else{
                turretMotor.setPower(0);
            }
        }else{
            turretTargetPosDeg = calculatePos();
        }
        getTurretTelemetryAdv();
        lastSetPoint = turretTargetPosDeg;

        // periodic logic (runs every loop)
    }
    public void getTurretTelemetryAdv(){
        PanelsTelemetry.INSTANCE.getTelemetry().addData("Turret Position", turretPos);
        PanelsTelemetry.INSTANCE.getTelemetry().addData("Turret Target", turretTargetPosDeg);
        PanelsTelemetry.INSTANCE.getTelemetry().addData("Turret Error", turretError);
        ActiveOpMode.telemetry().addLine("-------------- Turret Telemetry Adv: --------------");
        ActiveOpMode.telemetry().addData("Turret Position", turretPos);
        ActiveOpMode.telemetry().addData("Turret Velo", data.velocities[0]);
        ActiveOpMode.telemetry().addData("Turret Power", turretControlSystem.calculate(new KineticState(turretPos, (data.velocities[0]) * DEGREES_PER_US)));
        ActiveOpMode.telemetry().addData("Turret Error", turretError);
        ActiveOpMode.telemetry().addData("Turret Target", turretTargetPosDeg);
        ActiveOpMode.telemetry().addData("Turret Field Angle Goal", turretFieldAngleGoalDeg);
        ActiveOpMode.telemetry().addData("Turret Happy", TurretHappy());
        ActiveOpMode.telemetry().addData("Turret On", turretOn);
        ActiveOpMode.telemetry().addData("ks", akS);




    }



}
