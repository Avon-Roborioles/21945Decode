package org.firstinspires.ftc.teamcode.Commands;



import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;

import org.jetbrains.annotations.NotNull;

import dev.nextftc.core.commands.Command;
import dev.nextftc.extensions.pedro.PedroComponent;

public final class FollowPathNew extends Command {
    private final PathChain path;
    private final Boolean holdEnd;
    private final Double maxPower;

    /**
     * Full Constructor
     */
    public FollowPathNew(@NotNull PathChain path, Boolean holdEnd, Double maxPower) {
        if (path == null) throw new IllegalArgumentException("Path cannot be null");
        
        this.path = path;
        this.holdEnd = holdEnd;
        this.maxPower = maxPower;

        // Validation logic from original code
        if (this.maxPower != null && this.holdEnd == null) {
            throw new IllegalArgumentException("If maxPower is passed, holdEnd must be passed as well.");
        }
    }

    /**
     * Constructor for a single Path (converts to PathChain)
     */
    public FollowPathNew(@NotNull Path path, Boolean holdEnd, Double maxPower) {
        this(new PathChain(path), holdEnd, maxPower);
    }

    /**
     * Overloaded constructors to handle optional parameters (replacing Kotlin's @JvmOverloads)
     */
    public FollowPathNew(@NotNull PathChain path, boolean holdEnd) {
        this(path, holdEnd, null);
    }

    public FollowPathNew(@NotNull PathChain path) {
        this(path, null, null);
    }

    public FollowPathNew(@NotNull Path path, boolean holdEnd) {
        this(new PathChain(path), holdEnd, null);
    }

    public FollowPathNew(@NotNull Path path) {
        this(new PathChain(path), null, null);
    }

    @Override
    public void start() {
        // Logic to choose the correct Pedro Follower method based on provided params
        if (this.holdEnd != null && this.maxPower != null) {
            PedroComponent.Companion.follower().followPath(this.path, this.maxPower, this.holdEnd);
        } else if (this.holdEnd != null) {
            PedroComponent.Companion.follower().followPath(this.path, this.holdEnd);
        } else {
            PedroComponent.Companion.follower().followPath(this.path);
        }
    }

    @Override
    public boolean isDone() {
        // Returns true when the follower is no longer busy
        return !PedroComponent.Companion.follower().isBusy() || PedroComponent.follower().isRobotStuck();
    }

    @Override
    public void stop(boolean interrupted) {
        if (interrupted) {
            PedroComponent.Companion.follower().breakFollowing();
        }
    }
}