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
import dev.nextftc.control.feedforward.BasicFeedforwardParameters;
import dev.nextftc.control.interpolators.InterpolatorElement;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.VoltageCompensatingMotor;
@Configurable
public class CompTurretSubsystem implements Subsystem {
    public static final CompTurretSubsystem INSTANCE = new CompTurretSubsystem();
    OctoQuadFWv3 Octo;
    private double turretPos = 0;
    private static final double DEGREES_PER_US = (410.67 / 1024.0);
    private double angleOffset = 204.2 - 0.8;
    private double turretTargetPosDeg =0;
    private double turretFieldAngleGoalDeg = 90;
    private double botHeadingRad = 0;
    double turretZeroHeadingRad = 0;
    private final OctoQuadFWv3.EncoderDataBlock data = new OctoQuadFWv3.EncoderDataBlock();
    double maxPower = 1;
    double power = 0;
    double leftLimit = -140;
    double rightLimit = 130;
    boolean turretOn = true;

    public static double kp=0.005;
    public static double kI=0.00000000000;
    public static double kD = 0.005;
    public static double kF = 0;
    public static double threshold = 0;
    public static double kS = -0.02;
    public static double kSM = -0.02;
    public static double kSFP = -0.02;
    public static double kSFN = -0.02;
    public static double negThres = -30;
    public static double posThres = 30;

    public static double thresh1 = -114;
    public static double kS1 = -0.14;//-end to -114
    public static double thresh2 = -80;
    public static double kS2 = -0.12;//-114 to -80
    public static double thresh3 = -15;
    public static double kS3 = -0.09;//-80 to -15
    public static double thresh4 = 20;
    public static double kS4 = -0.09;//-15 to 20
    public static double thresh5 = 40;
    public static double kS5 = -0.15;





    private PIDCoefficients coefficients = new PIDCoefficients(kp,kI,kD);
    private BasicFeedforwardParameters parameters = new BasicFeedforwardParameters(0,0,kF);

    double lastSetPoint = 0;
    public static double kv = 0.0;

    private CompTurretSubsystem() {}

    // put hardware, commands, etc here
    public MotorEx turretMotorEx = new MotorEx("Turret Motor").reversed();

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
        if(turretTargetPosDeg<leftLimit- 20){
            turretTargetPosDeg= rightLimit;

        }else if(turretTargetPosDeg>rightLimit+20){
            turretTargetPosDeg= leftLimit;
        }

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







    @Override
    public void initialize() {
        turretMotorEx.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turretControlSystem = ControlSystem.builder()
                .posSquID(coefficients)
                .basicFF(parameters)
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
        coefficients.kI=kI;
        coefficients.kP=kp;
        coefficients.kD=kD;
        turretOn = true;


        // initialization logic (runs on init)
    }

    @Override
    public void periodic() {
        calculatePos();


        if(!ActiveOpMode.opModeInInit()){
            //400 Degree Flip
            if(turretTargetPosDeg>200){
                turretTargetPosDeg= (-160 + (turretTargetPosDeg-200));
            }else if(turretTargetPosDeg<-200){
                turretTargetPosDeg= ( 160 + (turretTargetPosDeg+200));
            }
            // 200 Hard limit
            if(turretTargetPosDeg<leftLimit){
                turretTargetPosDeg= leftLimit;

            }else if(turretTargetPosDeg>rightLimit){
                turretTargetPosDeg= rightLimit;
            }
            turretControlSystem.setGoal(new KineticState(turretTargetPosDeg));
            coefficients.kD=kD;
            coefficients.kI=kI;
            coefficients.kP=kp;
            parameters.kS=kF;

            
            if (turretPos < thresh1){
                kS = kS1;
            }else if (turretPos< thresh2){
                kS = kS2;
            }else if(turretPos< thresh3){
                kS = kS3;
            }else if(turretPos< thresh4){
                kS = kS4;
            }else if (turretPos> thresh5){
                kS = kS5;
            }
//            power = turretControlSystem.calculate(new KineticState(turretPos, (data.velocities[0]) * DEGREES_PER_US)) + ( kv * (turretTargetPosDeg - lastSetPoint)) + Math.signum(turretPos-turretTargetPosDeg)* kS;
            power = turretControlSystem.calculate(new KineticState(turretPos, (data.velocities[0]) * DEGREES_PER_US)) ;
            power += Math.signum(turretPos-turretTargetPosDeg)* kS;

            if (turretOn){
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
        ActiveOpMode.telemetry().addLine("-------------- Turret Telemetry Adv: --------------");
        ActiveOpMode.telemetry().addData("Turret Position", turretPos);
        ActiveOpMode.telemetry().addData("Turret Velo", data.velocities[0]);
        ActiveOpMode.telemetry().addData("Turret Power", turretControlSystem.calculate(new KineticState(turretPos, (data.velocities[0]) * DEGREES_PER_US)));
        ActiveOpMode.telemetry().addData("Turret Error", Math.abs(turretPos- turretTargetPosDeg));
        ActiveOpMode.telemetry().addData("Turret Target", turretTargetPosDeg);
        ActiveOpMode.telemetry().addData("Turret Field Angle Goal", turretFieldAngleGoalDeg);
        ActiveOpMode.telemetry().addData("Turret Happy", TurretHappy());




    }



}
