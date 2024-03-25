package ar.edu.itba.ss.tp2;

import ar.edu.itba.ss.cim.CellIndexMethod;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class OffLatticeAutomata {
    private final CellIndexMethod<MovingParticle> cim;
    private final double noise;

    protected OffLatticeAutomata(CellIndexMethod<MovingParticle> cim, double noise) {
        this.cim = cim;
        this.noise = noise;
    }

    private double calculateNewXPosition(double x, double velocity, double angle) {
        final double result = x + velocity * Math.cos(angle);
        return result > cim.getPlane().getLength() ? result - cim.getPlane().getLength() : result < 0 ? result + cim.getPlane().getLength() : result;
    }

    private double calculateNewYPosition(double y, double velocity, double angle) {
        final double result = y + velocity * Math.sin(angle);
        return result > cim.getPlane().getLength() ? result - cim.getPlane().getLength() : result < 0 ? result + cim.getPlane().getLength() : result;
    }

    private double calculateNewAngle(double angle, List<Double> neighboursAngles) {
        double sinProm = (neighboursAngles.stream().mapToDouble(Math::sin).sum() + Math.sin(angle)) / (neighboursAngles.size() + 1);
        double cosProm = (neighboursAngles.stream().mapToDouble(Math::cos).sum() + Math.cos(angle)) / (neighboursAngles.size() + 1);
        double randomNoise = noise * Math.random() - noise / 2;
        return Math.atan2(sinProm, cosProm) + randomNoise;
    }

    public void execute(int t) {
        this.execute(t, "times.txt");
    }

    public void execute(int t, String filename) {
        if (t < 0) {
            throw new IllegalArgumentException();
        }

        try (
                BufferedWriter timesWriter = Files.newBufferedWriter(
                        Paths.get(filename),
                        StandardOpenOption.WRITE,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                )
        ) {
            // t_0
            final ArrayList<MovingParticle> particles = new ArrayList<>(cim.getPlane().getParticles());
            particles.sort(Comparator.comparing(particle -> Integer.parseInt(particle.getIdentifier().substring(2))));
            for (MovingParticle particle : particles) {
                timesWriter.write(String.format("%d %s %f %f %f", 0, particle.getIdentifier(), particle.getX(), particle.getY(), particle.getAngle()));
                timesWriter.newLine();
            }

            // t_i / i > 0
            for (int i = 1; i < t; i++) {
                Map<MovingParticle, Set<MovingParticle>> neighbours = cim.execute();
                List<Map.Entry<MovingParticle, Set<MovingParticle>>> entries = new ArrayList<>(neighbours.entrySet());
                entries.sort(Comparator.comparing(entry -> Integer.parseInt(entry.getKey().getIdentifier().substring(2))));

                for (Map.Entry<MovingParticle, Set<MovingParticle>> entry : entries) {
                    MovingParticle particle = entry.getKey();
                    Set<MovingParticle> neighboursForParticle = entry.getValue();
                    particle.setAngle(calculateNewAngle(particle.getAngle(), neighboursForParticle.stream().map(MovingParticle::getAngle).toList()));
                    particle.setX(calculateNewXPosition(particle.getX(), particle.getVelocity(), particle.getAngle()));
                    particle.setY(calculateNewYPosition(particle.getY(), particle.getVelocity(), particle.getAngle()));
                    timesWriter.write(String.format("%d %s %f %f %f", i, particle.getIdentifier(), particle.getX(), particle.getY(), particle.getAngle()));
                    timesWriter.newLine();
                }
            }
        } catch (Exception e) {
            System.err.println("Error writing output");
        }
    }

    public CellIndexMethod<MovingParticle> getCIM() {
        return cim;
    }

    public double getNoise() {
        return noise;
    }

    public static class Builder {
        private CellIndexMethod<MovingParticle> cim;
        private Double noise;

        protected Builder() {

        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withCIM(CellIndexMethod<MovingParticle> cim) {
            this.cim = cim;
            return this;
        }

        public Builder withNoise(double noise) {
            this.noise = noise;
            return this;
        }

        public OffLatticeAutomata build() {
            if (cim == null || noise == null) {
                throw new IllegalStateException();
            }
            return new OffLatticeAutomata(cim, noise);
        }
    }
}
