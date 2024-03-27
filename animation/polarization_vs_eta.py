import csv
import math
import matplotlib.pyplot as plt
import numpy as np


with (open("../input.txt") as input_file):
    STATIONARY = 500

    input_data = input_file.readlines()
    particle_count = int(input_data[0][:-1])
    time_count = int(input_data[5][:-1])

    fig, ax = plt.subplots()
    xs = []
    ys = []
    errors = []

    files = [open(f"../times{t}.txt") for t in range(0, 30)]
    for k in range(len(files)):
        data = list(csv.reader(files[k], delimiter=" "))

        times = []
        for i in range(time_count):
            times.append(data[i * particle_count:(i + 1) * particle_count])

        stationary_times = times[STATIONARY:]
        polarizations = []
        for time in stationary_times:
            sumSin = 0
            sumCos = 0
            for particle_data in time:
                angle = float(particle_data[4])
                sumSin += math.sin(angle)
                sumCos += math.cos(angle)
            polarization = math.sqrt(sumSin ** 2 + sumCos ** 2) / particle_count
            polarizations.append(polarization)

        xs.append(0.2 * k)
        ys.append(np.average(polarizations))
        errors.append(np.std(polarizations, ddof=1))

    ax.errorbar(xs, ys, yerr=errors, fmt='o', capsize=5)

    ax.set_xlim(0, 6)
    ax.set_ylim(0, 1)
    plt.xticks(np.arange(0, 6.2, 0.2))
    plt.yticks(np.arange(0, 1.1, 0.1))

    # Display the animation
    plt.show()
