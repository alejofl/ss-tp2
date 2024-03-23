package ar.edu.itba.ss.tp2;

import ar.edu.itba.ss.cim.CellIndexMethod;
import ar.edu.itba.ss.cim.Plane;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        // Leemos archivo y obtenemos los datos
        List<String> data = null;
        try (Stream<String> stream = Files.lines(Paths.get("input.txt"))) {
            data = stream.toList();
        } catch (Exception e) {
            System.err.println("No input file found");
        }

        if (data == null || data.size() != 6) {
            throw new IllegalStateException();
        }

        final int particleCount = Integer.parseInt(data.get(0)); // N
        final int planeLength = Integer.parseInt(data.get(1)); // L
        final double interactionRadius = Double.parseDouble(data.get(2)); // r_c
        final double velocity = Double.parseDouble(data.get(3)); // v
        final double noise = Double.parseDouble(data.get(4)); // eta
        final int time = Integer.parseInt(data.get(5)); // t

        // Creamos el plano
        Plane.Builder<MovingParticle> planeBuilder = Plane.Builder.newBuilder();
        // Creamos todas las partículas (con posiciones random) y asignamos las partículas al plano
        for (int i = 0; i < particleCount; i++) {
            final double x = Math.random() * planeLength;
            final double y = Math.random() * planeLength;
            final double angle = 2 * Math.PI * Math.random() - Math.PI;
            planeBuilder = planeBuilder.withParticle(
                    MovingParticle.Builder.newBuilder()
                            .withIdentifier(String.format("p_%d", i))
                            .withX(x)
                            .withY(y)
                            .withRadius(0)
                            .withVelocity(velocity)
                            .withAngle(angle)
                            .build()
            );
        }
        final Plane<MovingParticle> plane = planeBuilder.withLength(planeLength).build();


        final CellIndexMethod.Builder<MovingParticle> cim = CellIndexMethod.Builder.newBuilder();

        // Ejecutamos el automata
        OffLatticeAutomata automata = OffLatticeAutomata.Builder.newBuilder()
                .withCIM(cim
                        .withInteractionRadius(interactionRadius)
                        .withOptimumMatrixCellCount()
                        .withPlane(plane)
                        .build()
                )
                .withNoise(noise)
                .build();

        automata.execute(time);
    }
}
