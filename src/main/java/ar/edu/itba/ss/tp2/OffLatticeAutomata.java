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
        return x + velocity * Math.cos(angle);
    }

    private double calculateNewYPosition(double y, double velocity, double angle) {
        return y + velocity * Math.sin(angle);
    }

    private double calculateNewAngle(double angle, List<Double> neighboursAngles) {
        double numerator = (neighboursAngles.stream().mapToDouble(Math::sin).sum() + Math.sin(angle)) / (neighboursAngles.size() + 1);
        double denominator = (neighboursAngles.stream().mapToDouble(Math::cos).sum() + Math.cos(angle)) / (neighboursAngles.size() + 1);
        double randomNoise = noise * Math.random() - noise / 2;
        return Math.atan2(denominator, numerator) + randomNoise;
    }

    public void execute(int t) {
        if (t < 0) {
            throw new IllegalArgumentException();
        }

        try (
                BufferedWriter polarizationWriter = Files.newBufferedWriter(
                        Paths.get("polarization.txt"),
                        StandardOpenOption.WRITE,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                );
                BufferedWriter timesWriter = Files.newBufferedWriter(
                        Paths.get("times.txt"),
                        StandardOpenOption.WRITE,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                )
        ) {
            // t_0
            final ArrayList<Double> angles = new ArrayList<>();
            final ArrayList<MovingParticle> particles = new ArrayList<>(cim.getPlane().getParticles());
            particles.sort(Comparator.comparing(particle -> Integer.parseInt(particle.getIdentifier().substring(2))));
            for (MovingParticle particle : particles) {
                timesWriter.write(String.format("%d %s %f %f %f", 0, particle.getIdentifier(), particle.getX(), particle.getY(), particle.getAngle()));
                timesWriter.newLine();

                angles.add(particle.getAngle());
            }
            double sumCos = angles.stream().mapToDouble(Math::cos).sum();
            double sumSin = angles.stream().mapToDouble(Math::sin).sum();
            double polarization = Math.sqrt(Math.pow(sumCos, 2) + Math.pow(sumSin, 2)) / cim.getPlane().getParticles().size();
            polarizationWriter.write(String.format("%f", polarization));
            polarizationWriter.newLine();

            // t_i / i > 0
            for (int i = 1; i < t; i++) {
                angles.clear();

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

                    angles.add(particle.getAngle());
                }

                sumCos = angles.stream().mapToDouble(Math::cos).sum();
                sumSin = angles.stream().mapToDouble(Math::sin).sum();
                polarization = Math.sqrt(Math.pow(sumCos, 2) + Math.pow(sumSin, 2)) / cim.getPlane().getParticles().size();
                polarizationWriter.write(String.format("%f", polarization));
                polarizationWriter.newLine();
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
