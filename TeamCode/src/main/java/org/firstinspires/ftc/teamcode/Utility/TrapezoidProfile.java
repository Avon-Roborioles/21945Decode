package org.firstinspires.ftc.teamcode.Utility;

import dev.nextftc.control.KineticState; /**
 * A trapezoidal motion profile generator.
 */
 public class TrapezoidProfile {
    private final TrapezoidProfileConstraints constraints;
    private int direction = 0;
    private KineticState currentState = new KineticState();
    private double endAccel = 0.0;
    private double endVel = 0.0;
    private double endDecel = 0.0;

    public TrapezoidProfile(TrapezoidProfileConstraints constraints) {
        this.constraints = constraints;
    }

    public double getTotalTime() {
        return endDecel;
    }

    public KineticState calculate(double tSeconds, KineticState current, KineticState goal) {
        direction = shouldFlipAcceleration(current, goal) ? -1 : 1;
        currentState = direct(current);
        KineticState directGoal = direct(goal);

        if (Math.abs(currentState.getVelocity()) > constraints.maxVelocity) {
            currentState = new KineticState(
                    currentState.getPosition(),
                    Math.copySign(constraints.maxVelocity, currentState.getVelocity()),
                    currentState.getAcceleration()
            );
        }

        double cutoffBegin = currentState.getVelocity() / constraints.maxAcceleration;
        double cutoffDistBegin = cutoffBegin * cutoffBegin * constraints.maxAcceleration / 2.0;

        double cutoffEnd = directGoal.getVelocity() / constraints.maxAcceleration;
        double cutoffDistEnd = cutoffEnd * cutoffEnd * constraints.maxAcceleration / 2.0;

        double fullTrapezoidDist = cutoffDistBegin + (directGoal.getPosition() - currentState.getPosition()) + cutoffDistEnd;
        double accelerationTime = constraints.maxVelocity / constraints.maxAcceleration;

        double fullSpeedDist = fullTrapezoidDist - accelerationTime * accelerationTime * constraints.maxAcceleration;

        if (fullSpeedDist < 0) {
            accelerationTime = Math.sqrt(fullTrapezoidDist / constraints.maxAcceleration);
            fullSpeedDist = 0.0;
        }

        endAccel = accelerationTime - cutoffBegin;
        endVel = endAccel + fullSpeedDist / constraints.maxVelocity;
        endDecel = endVel + accelerationTime - cutoffEnd;

        double position;
        double velocity;
        double accel;

        if (tSeconds < endAccel) {
            velocity = currentState.getVelocity() + tSeconds * constraints.maxAcceleration;
            position = currentState.getPosition() + (currentState.getVelocity() + tSeconds * constraints.maxAcceleration / 2.0) * tSeconds;
            accel = constraints.maxAcceleration;
        } else if (tSeconds < endVel) {
            velocity = constraints.maxVelocity;
            position = currentState.getPosition() +
                    ((currentState.getVelocity() + endAccel * constraints.maxAcceleration / 2.0) * endAccel
                            + constraints.maxVelocity * (tSeconds - endAccel));
            accel = 0.0;
        } else if (tSeconds <= endDecel) {
            velocity = directGoal.getVelocity() + (endDecel - tSeconds) * constraints.maxAcceleration;
            double timeLeft = endDecel - tSeconds;
            position = directGoal.getPosition() - (directGoal.getVelocity() + timeLeft * constraints.maxAcceleration / 2.0) * timeLeft;
            accel = -constraints.maxAcceleration;
        } else {
            velocity = directGoal.getVelocity();
            position = directGoal.getPosition();
            accel = 0.0;
        }

        return direct(new KineticState(position, velocity, accel));
    }

    public double timeLeftUntil(double target) {
        double position = currentState.getPosition() * direction;
        double velocity = currentState.getVelocity() * direction;

        double localEndAccel = endAccel * direction;
        double localEndFullSpeed = (endVel - endAccel) * direction;

        if (target < position) {
            localEndAccel = -localEndAccel;
            localEndFullSpeed = -localEndFullSpeed;
            velocity = -velocity;
        }

        localEndAccel = Math.max(localEndAccel, 0.0);
        localEndFullSpeed = Math.max(localEndFullSpeed, 0.0);

        double acceleration = constraints.maxAcceleration;
        double deceleration = -constraints.maxAcceleration;

        double distToTarget = Math.abs(target - position);
        if (distToTarget < 1e-6) return 0.0;

        double accelDist = velocity * localEndAccel + 0.5 * acceleration * localEndAccel * localEndAccel;
        double decelVelocity = (localEndAccel > 0) ? Math.sqrt(Math.abs(velocity * velocity + 2 * acceleration * accelDist)) : velocity;

        double fullSpeedDist = constraints.maxVelocity * localEndFullSpeed;
        double decelDist;

        if (accelDist > distToTarget) {
            accelDist = distToTarget;
            fullSpeedDist = 0.0;
            decelDist = 0.0;
        } else if (accelDist + fullSpeedDist > distToTarget) {
            fullSpeedDist = distToTarget - accelDist;
            decelDist = 0.0;
        } else {
            decelDist = distToTarget - fullSpeedDist - accelDist;
        }

        double accelTime = ((-velocity + Math.sqrt(Math.abs(velocity * velocity + 2 * acceleration * accelDist))) / acceleration);
        double decelTime = ((-decelVelocity + Math.sqrt(Math.abs(decelVelocity * decelVelocity + 2 * deceleration * decelDist))) / deceleration);
        double fullSpeedTime = fullSpeedDist / constraints.maxVelocity;

        return accelTime + fullSpeedTime + decelTime;
    }

    public boolean isFinished(double t) {
        return t >= endDecel;
    }

    private KineticState direct(KineticState state) {
        return new KineticState(
                state.getPosition() * direction,
                state.getVelocity() * direction,
                state.getAcceleration() * direction
        );
    }

    private static boolean shouldFlipAcceleration(KineticState initial, KineticState goal) {
        return initial.getPosition() > goal.getPosition();
    }
}
