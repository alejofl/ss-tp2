import csv
import random
import os

class Zone:
    def __init__(self, x, y, radius, is_pbc):
        self.x = x
        self.y = y
        self.radius = radius
        self.is_pbc = is_pbc
        self.last_visitors = []
        self.current_visitors = []
        self.statistics = []
        self.current_visits = 0

    def contains(self, x, y):
        return (x - self.x) ** 2 + (y - self.y) ** 2 <= self.radius ** 2

    def is_visiting(self, x, y, identifier):
        if self.contains(x, y):
            if identifier not in self.last_visitors:
                self.current_visits += 1
            self.current_visitors.append(identifier)
            return True
        return False

    def next_frame(self):
        previous_statistics = self.statistics[-1] if len(self.statistics) > 0 else 0
        self.statistics.append(previous_statistics + self.current_visits)
        if self.is_pbc:
            self.last_visitors += self.current_visitors
        else:
            self.last_visitors = self.current_visitors
        self.current_visitors = []
        self.current_visits = 0

    def clear(self):
        self.last_visitors = []
        self.current_visitors = []
        self.statistics = []
        self.current_visits = 0

with (open("../../input.txt") as input_file,
      open("../../visits_input.txt") as visits_file):
    input_data = input_file.readlines()
    particle_count = int(input_data[0][:-1])
    plane_length = int(input_data[1][:-1])
    time_count = int(input_data[5][:-1])

    visits_data = visits_file.readlines()
    zones_count = int(visits_data[0][:-1])
    zones_radius = float(visits_data[1][:-1])
    is_pbc = visits_data[2][:-1] == "p" or visits_data[2][:-1] == "P"

    zones = []
    for i in range(zones_count):
        zones.append(Zone(random.uniform(0, plane_length), random.uniform(0, plane_length), zones_radius, is_pbc))

    zones_file = open(f"../../visits_zones.txt", "w")
    for zone in zones:
        zones_file.write(f"{zone.radius} {zone.x} {zone.y}\n")
    zones_file.close()

    files = [open(f"../../times{t}.txt") for t in range(0, 30)]
    for k in range(len(files)):
        data = list(csv.reader(files[k], delimiter=" "))

        times = []
        for i in range(time_count):
            times.append(data[i * particle_count:(i + 1) * particle_count])

        for time in times:
            for particle_data in time:
                identifier, x, y = particle_data[1], float(particle_data[2]), float(particle_data[3])
                for zone in zones:
                    zone.is_visiting(x, y, identifier)
            for zone in zones:
                zone.next_frame()

        output = open(f"../../visits{k}.txt", "w")
        for zone in zones:
            for i in range(len(zone.statistics)):
                space = " " if i < len(zone.statistics) - 1 else ""
                output.write(f"{zone.statistics[i]}{space}")
            output.write("\n")
            zone.clear()
        output.close()
