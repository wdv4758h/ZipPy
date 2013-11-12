# zwei 10/07/2013
# micro benchmark: simple for range loop
import time

iteration = 50000 # 50000

def add(left, right):
    return left + right

def sumitup(iteration):
	total = 0
	for i in range(iteration):
		total = add(total, i)

	return total

def measure(num):
	print("Start timing...")
	start = time.time()

	for i in range(num): # 50000
		sumitup(iteration)

	print(sumitup(iteration))
	duration = "%.3f\n" % (time.time() - start)
	print("for-range: " + duration)

for i in range(5000): # 1000
	sumitup(1000)

#add("a", "b")
measure(50000)