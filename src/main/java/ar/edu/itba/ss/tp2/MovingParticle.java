package ar.edu.itba.ss.tp2;

import ar.edu.itba.ss.cim.Particle;

import java.util.Objects;

public class MovingParticle extends Particle {
    private double velocity;
    private double angle;

    protected MovingParticle(String identifier, double radius, double x, double y, double velocity, double angle) {
        super(identifier, radius, x, y);
        this.velocity = velocity;
        this.angle = angle;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getAngle() {
        return angle;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MovingParticle that = (MovingParticle) o;
        return Double.compare(velocity, that.velocity) == 0 && Double.compare(angle, that.angle) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), velocity, angle);
    }

    public static class Builder {
        private String identifier;
        private Double radius;
        private Double x;
        private Double y;
        private Double velocity;
        private Double angle;

        protected Builder() {

        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withIdentifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder withRadius(double radius) {
            this.radius = radius;
            return this;
        }

        public Builder withX(double x) {
            this.x = x;
            return this;
        }

        public Builder withY(double y) {
            this.y = y;
            return this;
        }

        public Builder withVelocity(double velocity) {
            this.velocity = velocity;
            return this;
        }

        public Builder withAngle(double angle) {
            this.angle = angle;
            return this;
        }

        public MovingParticle build() {
            if (this.identifier == null || this.radius == null || this.x == null || this.y == null || velocity == null || angle == null) {
                throw new IllegalStateException();
            }
            return new MovingParticle(
                    this.identifier,
                    this.radius,
                    this.x,
                    this.y,
                    this.velocity,
                    this.angle
            );
        }
    }
}
