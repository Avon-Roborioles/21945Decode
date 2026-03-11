package org.firstinspires.ftc.teamcode.Commands.Tilt;

import org.firstinspires.ftc.teamcode.Subsystems.PTOSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.StatusSubsystem;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.core.commands.Command;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;

public class TiltCommand extends Command {

    public static double power = 0.85;
    public static double pwmTarget = 1005;
    public static double kp =0.1;
    private double lTouch = 100;
    private double rTouch = 100;
    private double RTilt = 200;
    private double LTilt = 200;
    public static double PTOKp = 0.00;
    public static double PTOKd = 0.00;
    public static double PTOKi = 0.00;

    private PIDCoefficients coefficients = new PIDCoefficients(PTOKp,PTOKi, PTOKd);
    private ControlSystem ptoControlSystem;



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



        // executed when the command begins
    }

    @Override
    public void update() {
//        PedroComponent.follower().getDrivetrain().runDrive(new double[]{inputL.get()*power, inputL.get()*power, inputL.get()*power, inputL.get()*power});





        ActiveOpMode.telemetry().addLine("-------------- PTO Joystick Command: --------------");
        ActiveOpMode.telemetry().addData("pwm", pwmTarget);
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

