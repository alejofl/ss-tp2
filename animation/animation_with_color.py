import csv
import math
import matplotlib.pyplot as plt
from matplotlib.colors import hsv_to_rgb
from matplotlib.animation import FuncAnimation, FFMpegWriter
import numpy as np


def vector_to_rgb(angle):
    angle = angle % (2 * np.pi)
    if angle < 0:
        angle += 2 * np.pi

    return hsv_to_rgb((angle / 2 / np.pi, 1, 0.7))


with (open("../times3.txt") as times_file,
      open("../input.txt") as input_file):
    input_data = input_file.readlines()
    particle_count = int(input_data[0][:-1])
    plane_length = int(input_data[1][:-1])
    time_count = int(input_data[5][:-1])

    data = list(csv.reader(times_file, delimiter=" "))

    times = []
    for i in range(time_count):
        times.append(data[i * particle_count:(i + 1) * particle_count])

    fig, ax = plt.subplots()

    def update(i):
        ax.clear()
        ax.set_xlim(0, plane_length)
        ax.set_ylim(0, plane_length)
        ax.set_aspect('equal', adjustable='box')

        # Plot each particle
        for particle_data in times[i]:
            x, y, angle = float(particle_data[2]), float(particle_data[3]), float(particle_data[4])
            dx, dy = math.cos(angle), math.sin(angle)
            ax.quiver(x, y, dx, dy, color=vector_to_rgb(angle), angles='xy', scale_units='xy', scale=5, headaxislength=0, headlength=0, headwidth=0, width=0.0025)

        return ax

    # Create the animation
    ani = FuncAnimation(fig, update, frames=time_count, blit=False)
    # Display the animation
    # plt.show()
    # Save the animation
    ani.save("animation_with_color.mp4", writer=FFMpegWriter(fps=30))
