# zwei 12/02/13
# builtin function len()
from math      import sqrt
import time

def callSqrt(num):
	value = 0
	for i in range(num):
		value = sqrt(i)

	return value


def measure():
	print("Start timing...")
	start = time.time()

	result = callSqrt(500000000)

	print("Sqrt", result)

	duration = "%.3f\n" % (time.time() - start)
	print("math-sqrt: " + duration)

#warm up
for run in range(10000):
	callSqrt(run)

measure()