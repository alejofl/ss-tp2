import csv
import math
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation, FFMpegWriter


def is_inside(zones, x, y):
    for zone in zones:
        if (x - float(zone[1])) ** 2 + (y - float(zone[2])) ** 2 <= float(zone[0]) ** 2:
            return 'g'
    return 'k'


TIMES_NUMBER = 3
with (open(f"../../times{TIMES_NUMBER}.txt") as times_file,
      open("../../input.txt") as input_file,
      open("../../visits_zones.txt") as zones_file):
    input_data = input_file.readlines()
    particle_count = int(input_data[0][:-1])
    plane_length = int(input_data[1][:-1])
    time_count = int(input_data[5][:-1])

    data = list(csv.reader(times_file, delimiter=" "))
    zones = list(csv.reader(zones_file, delimiter=" "))

    times = []
    for i in range(time_count):
        times.append(data[i * particle_count:(i + 1) * particle_count])

    fig, ax = plt.subplots()

    def update(i):
        ax.clear()
        ax.set_xlim(0, plane_length)
        ax.set_ylim(0, plane_length)
        ax.set_aspect('equal', adjustable='box')
        ax.get_xaxis().set_visible(False)
        ax.get_yaxis().set_visible(False)

        for zone in zones:
            ax.add_patch(
                plt.Circle(
                    (float(zone[1]), float(zone[2])),
                    float(zone[0]),
                    color='b',
                    fill=False
                )
            )

        # Plot each particle
        for particle_data in times[i]:
            x, y, angle = float(particle_data[2]), float(particle_data[3]), float(particle_data[4])
            dx, dy = math.cos(angle), math.sin(angle)
            ax.quiver(x, y, dx, dy, color=is_inside(zones, x, y), angles='xy', scale_units='xy', scale=5, headaxislength=0, headlength=0, headwidth=0, width=0.0025,)

        return ax

    # Create the animation
    ani = FuncAnimation(fig, update, frames=time_count, blit=False, repeat=False)
    # Display the animation
    # plt.show()
    # Save the animation
    ani.save("visits_animation.mp4", writer=FFMpegWriter(fps=30))
