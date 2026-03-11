package org.firstinspires.ftc.teamcode.Commands.Tilt;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.Subsystems.PTOSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.StatusSubsystem;

import dev.nextftc.bindings.Range;
import dev.nextftc.core.commands.Command;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;
@Configurable
public class PTOJoystickCommand extends Command {
    Range inputL;
    public static double power = 0.85;
    public static double pwmTarget = 1005;



    public PTOJoystickCommand(Range inputL) {
        this.inputL = inputL;


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
        PedroComponent.follower().getDrivetrain().runDrive(new double[]{inputL.get()*power, inputL.get()*power, inputL.get()*power, inputL.get()*power});
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

