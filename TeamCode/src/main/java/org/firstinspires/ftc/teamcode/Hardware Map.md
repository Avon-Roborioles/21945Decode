## Hardware Map

## Control Hub
USBs:
USB 3.0: "LimeLight"

Motors:
M0: "BackRight"
M1: "FrontRight"
M2: "FrontLeft"
M3: "BackLeft"

Servo (Pass Through to Servo Power Module):
S0-(S1): "PTO L"
S1-(S2): "PTO R"
S2-(S3): "Hood"
S3-(S4): "Sort L"
S4-(S5): "Sort C"
S5-(S6): "Sort R"

Uart:
U0:Expansion Hub Data

I2C:
I0: Internal IMU "imu"
I1: "Odometry"
I2: "OctoQuad"
I3: "Prism"

Digital: 
D6: "IntakeBB"

Analog:
A2: "FloodGate"

## Servo Hub
S1: PTO L
S2: PTO R
S3: Hood
S4: Sort L
S5: Sort C
S6: Sort R

## OctoQuad
P0: "Turret Pos"


## Expansion Hub

Motors:
M0: "Launch Motor 1"
M1: "Launch Motor 2"
M2: "Turret Motor"
M3: "Intake Motor"

Servos:
S4: "LL Tilt"
S5: "Beacon"

Uart:
U0: Control Hub Data

I2C:
I1: "Sort CS R"
I2: "Sort CS C"
I3: "Sort CS L"

Analog:

Digital:

