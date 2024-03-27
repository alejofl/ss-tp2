import csv
import matplotlib.pyplot as plt
import numpy as np


with (open("../../input.txt") as input_file, open("../../visits_input.txt") as visits_file):
    input_data = input_file.readlines()
    particle_count = int(input_data[0][:-1])
    visits_data = visits_file.readlines()
    visits_percentage = float(visits_data[3][:-1]) / 100.0
    particle_threshold = int(particle_count * visits_percentage)

    fig, ax = plt.subplots()
    xs = []
    ys = []
    errors = []

    files = [open(f"../../visits{t}.txt") for t in range(0, 30)]
    for k in range(len(files)):
        zones = list(csv.reader(files[k], delimiter=" "))
        for i in range(len(zones)):
            for j in range(len(zones[i])):
                zones[i][j] = int(zones[i][j])

        times = []
        for zone in zones:
            for time in range(len(zone)):
                if zone[time] >= particle_threshold:
                    times.append(time)
                    break

        xs.append(0.2 * k)
        ys.append(np.average(times))
        errors.append(np.std(times, ddof=1))

    ax.errorbar(xs, ys, yerr=errors, fmt='o', capsize=5)

    ax.set_xlim(0, 6)
    plt.xticks(np.arange(0, 6.2, 0.2))

    # Display the animation
    plt.show()
