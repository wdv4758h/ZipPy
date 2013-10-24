# zwei 10/10/13
# function calls
import time

def emptyFunction(arg):
	return arg

def callFunctions(num):
	count = 0
	for i in range(num):
		ret = emptyFunction(i)
		count += 1

	return count


def measure():
	print("Start timing...")
	start = time.time()

	sum = callFunctions(100000000) #1000000

	print("Number of calls ", sum)

	duration = "%.3f\n" % (time.time() - start)
	print("function_call: " + duration)

#warm up
for run in range(5):
	callFunctions(400)

measure()