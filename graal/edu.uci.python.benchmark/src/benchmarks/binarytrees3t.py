# The Computer Language Benchmarks Game
# http://shootout.alioth.debian.org/
#
# contributed by Antoine Pitrou
# modified by Dominique Wahli
# modified by Heinrich Acker

import sys
import time

def make_tree(item, depth):
    if not depth: return item, None, None
    item2 = item + item
    depth -= 1
    return item, make_tree(item2 - 1, depth), make_tree(item2, depth)

def check_tree(xxx_todo_changeme):
    (item, left, right) = xxx_todo_changeme
    if not left: return item
    return item + check_tree(left) - check_tree(right)

def main(n):
    min_depth = 4
    max_depth = max(min_depth + 2, int(n))
    stretch_depth = max_depth + 1

    print("stretch tree of depth %d\t check:" % stretch_depth, check_tree(make_tree(0, stretch_depth)))

    long_lived_tree = make_tree(0, max_depth)

    iterations = 2**max_depth

    for depth in range(min_depth, stretch_depth, 2):

        check = 0
        for i in range(1, iterations + 1):
            check += check_tree(make_tree(i, depth)) + check_tree(make_tree(-i, depth))

        print("%d\t trees of depth %d\t check:" % (iterations * 2, depth), check)
        iterations //= 4

    print("long lived tree of depth %d\t check:" % max_depth, check_tree(long_lived_tree))
      

def measure():
    print("Start timing...")
    start = time.time()
    main(num)
    duration = "%.3f\n" % (time.time() - start)
    print("binarytrees: " + duration)

# warm up
num =  int(sys.argv[1])
for run in range(1):
    main(15)

measure()