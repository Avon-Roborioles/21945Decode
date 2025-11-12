package org.firstinspires.ftc.teamcode.Subsystems;
import com.qualcomm.robotcore.hardware.HardwareMap;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.SubsystemGroup;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class SorterSubsystem extends SubsystemGroup {

    public ServoEx servoOne;
    public ServoEx servoTwo;
    public ServoEx servoThree;

    // Commands for each servo
    public Command servoOneOpen;
    public Command servoOneClose;

    public Command servoTwoOpen;
    public Command servoTwoClose;

    public Command servoThreeOpen;
    public Command servoThreeClose;

    public SorterSubsystem(HardwareMap hMap) {

        // Variables
        servoOne = new ServoEx("SorterServo1");
        servoTwo = new ServoEx("SorterServoTwo");
        servoThree = new ServoEx("SorterServoThree");

        // servo1 commands
        servoOneOpen = new SetPosition(servoOne, 0.1).requires(this);
        servoOneClose = new SetPosition(servoOne, 0.2).requires(this);

        // servo2 commands
        servoTwoOpen = new SetPosition(servoTwo, 0.1).requires(this);
        servoTwoClose = new SetPosition(servoTwo, 0.2).requires(this);

        // severo3 commands
        servoThreeOpen = new SetPosition(servoThree, 0.1).requires(this);
        servoThreeClose = new SetPosition(servoThree, 0.2).requires(this);
    }
}
