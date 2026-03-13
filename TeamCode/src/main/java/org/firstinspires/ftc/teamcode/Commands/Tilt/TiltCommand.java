package org.firstinspires.ftc.teamcode.Commands.Tilt;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.Subsystems.PTOSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.StatusSubsystem;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.core.commands.Command;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;
@Configurable
public class TiltCommand extends Command {

    public static double MaxPtoPower = 1;
    public static double pwmTarget = 1005;
    private double lTouch = 50;
    private double rTouch =30;
    private double RTilt = 121;
    private double LTilt = 134;
    private double leftPower = 0;
    private double rightPower = 0;

    public static double PTOKFPre = 0.3;
    public static double PTOKFTilt = 1;
    private double PTOKF = 0;
    private double leftGoal;
    private double rightGoal;

    private boolean pastTouch = false;





    public TiltCommand() {
    }
    @Override
    public boolean isDone() {
        return false; // whether or not the command is done
    }

    @Override
    public void start() {
        PTOSubsystem.INSTANCE.Engage();
        PedroComponent.follower().breakFollowing();
        ActiveOpMode.gamepad1().rumble(1000);
        ActiveOpMode.gamepad2().rumble(1000);
        StatusSubsystem.INSTANCE.setPrismToPWM((long) pwmTarget);

        pastTouch = false;




        // executed when the command begins
    }

    @Override
    public void update() {

            if(PTOSubsystem.INSTANCE.getPtoLPosition() < lTouch || PTOSubsystem.INSTANCE.getPtoRPosition()< rTouch){
                PTOKF = PTOKFPre;

            }else{
                pastTouch = true;
                PTOKF = PTOKFTilt;
            }
            if(!pastTouch){
                leftGoal = lTouch+5;
                rightGoal = rTouch+5;
            }else{
                leftGoal = LTilt;
                rightGoal = RTilt;
            }
            if(PTOSubsystem.INSTANCE.getPtoLPosition() - leftGoal<0){
                leftPower =  Math.signum(PTOSubsystem.INSTANCE.getPtoLPosition() - leftGoal)* PTOKF;
            }
            if(PTOSubsystem.INSTANCE.getPtoRPosition() - rightGoal<0){
                rightPower =  Math.signum(PTOSubsystem.INSTANCE.getPtoRPosition() - rightGoal)* PTOKF;
            }



            PedroComponent.follower().getDrivetrain().runDrive(new double[]{leftPower * MaxPtoPower, leftPower * MaxPtoPower, rightPower * MaxPtoPower, rightPower * MaxPtoPower});





        ActiveOpMode.telemetry().addLine("-------------- PTO Joystick Command: --------------");
        ActiveOpMode.telemetry().addData("pwm", pwmTarget);
        ActiveOpMode.telemetry().addData("in Touch", pastTouch);
        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {
        PedroComponent.follower().getDrivetrain().runDrive(new double[]{0, 0,0,0});
        PedroComponent.follower().breakFollowing();
        PTOSubsystem.INSTANCE.Disengage();
        PedroComponent.follower().startTeleOpDrive();

        // executed when the command ends
    }
}

