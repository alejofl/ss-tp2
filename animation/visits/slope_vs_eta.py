import csv
import matplotlib.pyplot as plt
import numpy as np

plt.rcParams.update({'font.size': 20})
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

    slopes = []
    for zone in zones:
        sumXY = 0
        sumX = 0
        sumY = 0
        sumXSquared = 0
        for time in range(len(zone)):
            sumXY += time * zone[time]
            sumX += time
            sumY += zone[time]
            sumXSquared += time ** 2
        slope = (len(zone) * sumXY - sumX * sumY) / (len(zone) * sumXSquared - sumX ** 2)
        slopes.append(slope)

    xs.append(0.2 * k)
    ys.append(np.average(slopes))
    errors.append(np.std(slopes, ddof=1))

ax.errorbar(xs, ys, yerr=errors, fmt='o', capsize=5)

ax.set_xlabel("Ruido", fontdict={"weight": "bold"})
ax.set_ylabel("Número de visitas por unidad de tiempo", fontdict={"weight": "bold"})
ax.set_xlim(0, 6)

# Display the animation
plt.show()
