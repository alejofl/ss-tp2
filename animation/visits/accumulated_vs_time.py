import csv
import matplotlib.pyplot as plt


TIMES_NUMBER = 10
with open(f"../../visits{TIMES_NUMBER}.txt") as visits_file:
    zones = list(csv.reader(visits_file, delimiter=" "))
    for i in range(len(zones)):
        for j in range(len(zones[i])):
            zones[i][j] = int(zones[i][j])

    plt.rcParams.update({'font.size': 20})
    fig, ax = plt.subplots()
    lines = []
    for i in range(len(zones)):
        line, = ax.plot(range(len(zones[i])), zones[i], linewidth=2.0, label=f"Zona {i}")
        lines.append(line)

    ax.set_xlabel("Tiempo (s)", fontdict={"weight": "bold"})
    ax.set_ylabel("Cantidad de visitas", fontdict={"weight": "bold"})
    ax.legend(handles=lines)
    # Display the animation
    plt.show()
