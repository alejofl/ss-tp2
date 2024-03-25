import csv
import math
import matplotlib.pyplot as plt

with (open("../input.txt") as input_file):
    input_data = input_file.readlines()
    particle_count = int(input_data[0][:-1])
    time_count = int(input_data[5][:-1])

    fig, ax = plt.subplots()
    lines = []

    files = [open(f"../times{t}.txt") for t in range(0, 9)]
    for k in range(len(files)):
        data = list(csv.reader(files[k], delimiter=" "))

        times = []
        for i in range(time_count):
            times.append(data[i * particle_count:(i + 1) * particle_count])

        polarizations = []
        for time in times:
            sumSin = 0
            sumCos = 0
            for particle_data in time:
                angle = float(particle_data[4])
                sumSin += math.sin(angle)
                sumCos += math.cos(angle)
            polarization = math.sqrt(sumSin ** 2 + sumCos ** 2) / particle_count
            polarizations.append(polarization)

        line, = ax.plot(range(len(polarizations)), polarizations, linewidth=2.0, label=f"\u03B7 = {0.5 * (k + 1)}")
        lines.append(line)

    ax.set_xlim(0, time_count)
    ax.set_ylim(0, 1)
    x_step = 1 if time_count < 20 else 5 if time_count < 100 else 10
    plt.xticks(range(0, time_count + 1, x_step))
    plt.yticks([x / 10.0 for x in range(0, 11, 1)])
    ax.legend(handles=lines)

    # Display the animation
    plt.show()
