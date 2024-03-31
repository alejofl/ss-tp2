# Off-Lattice Automata Simulation

## Introduction

This project is a simulation of an off-lattice automata model. The model is based on the paper "Novel type of phase transition in a system of self-driven particles" by Tamás Vicsek and collaborators. The model is a 2D simulation of cells that move around a domain and collide with each other, changing its velocity. The purpose of this project is to study its transition from a disordered state to an ordered state, mimicking the behavior of a flock of birds.

## Requirements

* Java 19
* Maven
* Python 3 (only if you want the animation to be rendered)

## Building the project

To build the project, `cd` to the root of the project and run the following command:

```bash
mvn clean package
```

This will compile and package a `.jar` file in the `target` directory.

## Executing the project

> [!NOTE]  
> The following instructions assume that you have built the project as described in the previous section and that the generated `.jar` file is in the current working directory.

The program expects to have an input file named `input.txt` in the current working directory. The input file should contain the following structure:

```text
{{ number_of_particles }}
{{ plane_length }}
{{ interaction_radius }}
{{ velocity_module }}
{{ starting_noise }}
{{ time }}
```

Where:

* `number_of_particles` is the number of particles in the simulation. Must be an integer.
* `plane_length` is the length of the square plane where the particles are located. Must be a floating point number.
* `interaction_radius` is the radius of the interaction between particles. Must be a floating point number.
* `velocity_module` is the module of the velocity of the particles. Must be a floating point number.
* `starting_noise` is the noise added to the velocity of the particles on each step. It represents an angle in radians. Must be a floating point number. The recommended value is `0`.
* `time` is the number of steps the simulation will run. Must be an integer.

To execute the project, run the following command:

```bash
java -jar ss-tp2-1.0-SNAPSHOT.jar
```

This will execute the program and generate a series of output files named `times{{ index }}.txt` in the current working directory. Starting from the starting noise angle, the program will run the simulation and generate an output file, increasing the angle by 0.2 radians each time, until the noise reaches 6 radians. 

Each file will have the following structure:

```text
{{ time }} {{ particle_id }} {{ x_position }} {{ y_position }} {{ velocity_angle }}
...
```

Where:

* `time` is the current time step of the simulation.
* `particle_id` is the identifier of the particle.
* `x_position` is the x-coordinate of the particle on that step.
* `y_position` is the y-coordinate of the particle on that step.
* `velocity_angle` is the angle of the velocity of the particle on that step.

There will be one line for each particle and step in the simulation. For example: if there are 300 particles and the simulation runs for 100 steps, the file will have 30000 lines.

## Visualizing the output

> [!NOTE]  
> The following instructions assume that you have executed the project as described in the previous section and that the files `input.txt` and `times{{ index }}.txt` are in the current working directory.

### Installing dependencies

To visualize the output, we must run a Python script. First, we need to install the required dependencies. To do so, run the following command:

```bash
python -m venv venv
source venv/bin/activate
pip install -r animation/requirements.txt
```

### Generating the animation

You must specify the index of the output file you want to visualize. To do so, edit the `animation/animation.py` file and change the value of the `TIMES_NUMBER` variable to the desired index.

To visualize an animation of the simulation, run the following command:

```bash
python animation/animation.py
```

This will generate an animation of the particles in the simulation. You will be able to see how the particles move and how the start to behave like a flock of birds.

Another animation is also available, in which the particles are colored according to their velocity. To generate this animation, run the following command:

```bash
python animation/animation_with_color.py
```

### Visualizing the polarization vs time graph

The polarization of the system is a measure of how ordered the system is. It is expected to reach a stationary point after a certain time.

To visualize the polarization vs time graph, run the following command:

```bash
python animation/polarization_vs_time.py
```

This will generate a graph showing the polarization of the system over time. You will be able to see how the system transitions from a disordered state to an ordered state for each noise, as there is one plot-line for each of them.

### Visualizing the polarization vs noise graph

The polarization of the system is also expected to change with the noise added to the system. To visualize the polarization vs noise graph, run the following command:

```bash
python animation/polarization_vs_eta.py
```

### Visits of particles to random zones in the plane

It is also interesting to analyze the visits of particles to random zones in the plane, and how they change with the noise.

#### Generating the data

The script expects to have an input file named `visits_input.txt` in the current working directory. The input file should have the following structure:

```text
{{ number_of_zones }}
{{ zone_radius }}
{{ pbc }}
{{ visit_threshold }}
```

Where:

* `number_of_zones` is the number of zones in the plane. Must be an integer.
* `zone_radius` is the radius of the zones. Must be a floating point number.
* `pbc` is a value indicating if the simulation should use periodic boundary conditions. If there are periodic boundary conditions, the value should be `p` or `P`.
* `visit_threshold` is the percentage of particles that must be a visitor. This is used for a graphical representation of the data. Must be a floating point number between `0` and `100`.

To generate the data, run the following command:

```bash
python animation/visits/visits.py
```

This will generate a series of output files named `visits{{ index }}.txt` For each file, the corresponding `times{{ index }}.txt` will be used. The output files will have the following structure:

```text
{{ accumulated_visits_t0 }} {{ accumulated_visits_t1 }} ...
...
```

There will be one line for each zone and one value in each line for each time.

For example, if there are 2 zones and `{{ time }}` is set to 5, the file would have the following structure:

```text
15 17 18 22 45
0 2 2 19 30
```

#### Generating the animation

You must specify the index of the output file you want to visualize. To do so, edit the `animation/visits/animation.py` file and change the value of the `TIMES_NUMBER` variable to the desired index.

To visualize the visits of particles to random zones in the plane, run the following command:

```bash
python animation/visits/animation.py
```

#### Visualizing the accumulated visits vs time graph

You must specify the index of the output file you want to visualize. To do so, edit the `animation/visits/accumulated_vs_time.py` file and change the value of the `TIMES_NUMBER` variable to the desired index.

To visualize the accumulated visits vs time graph, run the following command:

```bash
python animation/visits/accumulated_vs_time.py
```

#### Visualizing the observable value vs noise graph

The observable value will change whether the system has periodic boundary conditions or not:

* If the system has periodic boundary conditions, the observable value is the time until the visit threshold is met.
* If the system does not have periodic boundary conditions, the observable value is the number of visits per unit of time.

To visualize the visit threshold vs noise graph, run the following command:

```bash
python animation/visits/threshold_vs_eta.py
```

To visualize the visits per unit of time vs noise graph, run the following command:

```bash
python animation/visits/slope_vs_eta.py
```

## Final Remarks

This project was done in an academic environment, as part of the curriculum of Systems Simulation from Instituto Tecnológico de Buenos Aires (ITBA)

The project was carried out by:

* Alejo Flores Lucey
* Nehuén Gabriel Llanos