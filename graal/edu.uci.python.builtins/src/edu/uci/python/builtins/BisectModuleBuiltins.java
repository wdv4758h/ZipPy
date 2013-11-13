/*
 * Copyright (c) 2013, Regents of the University of California
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.uci.python.builtins;

import java.util.Arrays;

import edu.uci.python.runtime.sequence.*;
import edu.uci.python.runtime.standardtypes.*;

public final class BisectModuleBuiltins extends PythonBuiltins {

    // bisect.bisect(a, x, lo=0, hi=len(a))
    @Builtin(name = "bisect", minNumOfArguments = 2, maxNumOfArguments = 4, takesKeywordArguments = true)
    public abstract static class PythonBisectNode extends PythonBuiltinNode {

        public PythonBisectNode(String name) {
            super(name);
        }

        public PythonBisectNode(PythonBisectNode prev) {
            this(prev.getName());
        }

        public int bisect(Object arg1, Object arg2, Object keyword1, Object keyword2) {
            /**
             * TODO
             */
            return 0;
            // return bisect(arg1, arg2);
        }
    }

    @Builtin(name = "bisect_right", minNumOfArguments = 2, maxNumOfArguments = 4, takesKeywordArguments = true)
    public abstract static class PythonBisectRightNode extends PythonBuiltinNode {

        public PythonBisectRightNode(String name) {
            super(name);
        }

        public PythonBisectRightNode(PythonBisectRightNode prev) {
            this(prev.getName());
        }

        public int bisect_right(Object[] args, Object[] keywords) {
            return bisect(args, keywords);
        }

        public int bisect_right(Object arg0, Object arg1) {
            return bisect(arg0, arg1);
        }

        public int bisect_right(Object arg) {
            return bisect(arg);
        }
    }

    @Builtin(name = "bisect_left", minNumOfArguments = 2, maxNumOfArguments = 4, takesKeywordArguments = true)
    public abstract static class PythonBisectLeftNode extends PythonBuiltinNode {

        public PythonBisectLeftNode(String name) {
            super(name);
        }

        public PythonBisectLeftNode(PythonBisectLeftNode prev) {
            this(prev.getName());
        }

        public int bisect_left(Object[] args, Object[] keywords) {
            if (args.length == 2) {
                /**
                 * TODO
                 */
                return 0;
                // return bisect_left(args[0], args[1]);
            } else if (args.length == 3 && args[0] instanceof PList) {
                PList plist = (PList) args[0];
                PList slice = (PList) (plist).getSlice((int) args[2], ((PList) args[0]).len(), 1, ((PList) args[0]).len());
                Object[] tempArray = new Object[slice.len() + 1];
                System.arraycopy(slice.getSequence(), 0, tempArray, 0, slice.len());
                tempArray[tempArray.length] = args[1];
                Arrays.sort(tempArray);
                int index = Arrays.binarySearch(tempArray, args[1]);

                do {
                    index--;
                } while (index >= 0 && tempArray[index].equals(args[1]));

                return index + 1 + (int) args[2];
            } else if (args.length == 4 && args[0] instanceof PList) {
                PList slice = (PList) ((PList) args[0]).getSlice((int) args[2], (int) args[3], 1, ((PList) args[0]).len());
                Object[] tempArray = new Object[slice.len() + 1];
                System.arraycopy(slice.getSequence(), 0, tempArray, 0, slice.len());
                tempArray[tempArray.length] = args[1];
                Arrays.sort(tempArray);
                int index = Arrays.binarySearch(tempArray, args[1]);

                do {
                    index--;
                } while (index >= 0 && tempArray[index].equals(args[1]));

                return index + 1 + (int) args[2];
            } else {
                throw new RuntimeException("wrong number of arguments for bisect_left() ");
            }
        }
    }

    @Builtin(name = "insort", minNumOfArguments = 2, maxNumOfArguments = 4, takesKeywordArguments = true)
    public abstract static class PythonBisectInsortNode extends PythonBuiltinNode {

        public PythonBisectInsortNode(String name) {
            super(name);
        }

        public PythonBisectInsortNode(PythonBisectInsortNode prev) {
            this(prev.getName());
        }
    }

    @Builtin(name = "insort_right", minNumOfArguments = 2, maxNumOfArguments = 4, takesKeywordArguments = true)
    public abstract static class PythonBisectInsortRightNode extends PythonBuiltinNode {

        public PythonBisectInsortRightNode(String name) {
            super(name);
        }

        public PythonBisectInsortRightNode(PythonBisectInsortRightNode prev) {
            this(prev.getName());
        }

        public void insort_right(Object[] args, Object[] keywords) {
            insort(args, keywords);
        }

        public void insort_right(Object arg0, Object arg1) {
            insort(arg0, arg1);
        }

        public void insort_right(Object arg) {
            insort(arg);
        }
    }

    @Builtin(name = "insort_right", minNumOfArguments = 2, maxNumOfArguments = 4, takesKeywordArguments = true)
    public abstract static class PythonBisectInsortLeftNode extends PythonBuiltinNode {

        public PythonBisectInsortLeftNode(String name) {
            super(name);
        }

        public PythonBisectInsortLeftNode(PythonBisectInsortLeftNode prev) {
            this(prev.getName());
        }

        public void insort_left(Object[] args, Object[] keywords) {
            if (args.length > 1 && args[0] instanceof PList) {
                ((PList) args[0]).addItem(bisect_left(args, keywords), args[1]);
            } else {
                throw new RuntimeException("wrong number of arguments for insort_left() ");
            }
        }

        public void insort_left(Object arg0, Object arg1) {
            if (arg0 instanceof PList) {
                ((PList) arg0).addItem(bisect_left(arg0, arg1), arg1);
            } else {
                throw new RuntimeException("invalid arguments number for insort_left() ");
            }
        }

        public void insort_left(Object arg) {
            throw new RuntimeException("wrong number of arguments for insort_left() ");
        }
    }

    public static int bisect(Object arg0, Object arg1) {
        if (arg0 instanceof PList) {
            PList plist = (PList) arg0;
            Object[] list = plist.getSequence();

            if (list.length == 0) {
                return 0;
            }

            return getIndexRight(list, arg1);
        } else {
            throw new RuntimeException("invalid arguments number for bisect() ");
        }
    }

    public static int bisect(Object arg) {
        throw new RuntimeException("wrong number of arguments for bisect() ");
    }

    public static int bisect_left(Object arg0, Object arg1) {
        if (arg0 instanceof PList) {
            PList plist = (PList) arg0;
            Object[] list = plist.getSequence();

            if (list.length == 0) {
                return 0;
            }

            return getIndexLeft(list, arg1);
        } else {
            throw new RuntimeException("invalid arguments number for bisect_left() ");
        }
    }

    public static int bisect_left(Object arg) {
        throw new RuntimeException("wrong number of arguments for bisect_left() ");
    }

    public static void insort(Object[] args, Object[] keywords) {
        if (args.length > 1 && args[0] instanceof PList) {
            ((PList) args[0]).addItem(bisect(args, keywords), args[1]);
        } else {
            throw new RuntimeException("wrong number of arguments for insort() ");
        }
    }

    public static void insort(Object arg0, Object arg1) {
        if (arg0 instanceof PList) {
            ((PList) arg0).addItem(bisect(arg0, arg1), arg1);
        } else {
            throw new RuntimeException("invalid arguments number for insort() ");
        }
    }

    public static void insort(Object arg) {
        throw new RuntimeException("wrong number of arguments for insort() ");
    }

    // Checkstyle: resume method name check

    public static int getIndexLeft(Object[] args, Object key) {
        return binarySearchLeft(args, 0, args.length - 1, key);
    }

    public static int binarySearchLeft(Object[] args, int start, int stop, Object key) {
        if (start <= stop) {
            int middle = (stop - start) / 2 + start;
            if (((String) args[middle]).compareTo((String) key) > 0) {
                if (middle - 1 >= 0 && ((String) args[middle - 1]).compareTo((String) key) < 0) {
                    return middle;
                } else if (middle - 1 <= 0) {
                    return 0;
                } else {
                    return binarySearchLeft(args, start, middle - 1, key);
                }
            } else if (((String) args[middle]).compareTo((String) key) < 0) {
                if (middle + 1 < args.length && ((String) args[middle + 1]).compareTo((String) key) > 0) {
                    return middle + 1;
                } else if (middle + 1 >= args.length - 1) {
                    return args.length;
                } else {
                    return binarySearchLeft(args, middle + 1, stop, key);
                }
            } else {
                int i = middle - 1;
                while (((String) args[i]).compareTo((String) key) == 0 && i >= 0) {
                    i--;
                }
                return i + 1;
            }
        }
        return -1; // should not happen
    }

    public static int getIndexRight(Object[] args, Object key) {
        if (key instanceof String) {
            return binarySearchRightStr(args, 0, args.length - 1, (String) key);
        } else {
            return binarySearchRightDouble(args, 0, args.length - 1, (double) key);
        }
    }

    public static int binarySearchRightDouble(Object[] args, int start, int stop, double key) {
        if (start <= stop) {
            int middle = (stop - start) / 2 + start;
            if (((double) args[middle]) > key) {
                if (middle - 1 >= 0 && ((double) args[middle - 1]) < key) {
                    return middle;
                } else if (middle - 1 <= 0) {
                    return 0;
                } else {
                    return binarySearchRightDouble(args, start, middle - 1, key);
                }
            } else if (((double) args[middle]) < key) {
                if (middle + 1 < args.length && ((double) args[middle + 1]) > key) {
                    return middle + 1;
                } else if (middle + 1 >= args.length - 1) {
                    return args.length;
                } else {
                    return binarySearchRightDouble(args, middle + 1, stop, key);
                }
            } else {
                int i = middle + 1;
                while (((double) args[i]) == key && i < args.length) {
                    i++;
                }
                return i;
            }
        }
        return -1; // should not happen
    }

    public static int binarySearchRightStr(Object[] args, int start, int stop, String key) {
        if (start <= stop) {
            int middle = (stop - start) / 2 + start;
            if (((String) args[middle]).compareTo(key) > 0) {
                if (middle - 1 >= 0 && ((String) args[middle - 1]).compareTo(key) < 0) {
                    return middle;
                } else if (middle - 1 <= 0) {
                    return 0;
                } else {
                    return binarySearchRightStr(args, start, middle - 1, key);
                }
            } else if (((String) args[middle]).compareTo(key) < 0) {
                if (middle + 1 < args.length && ((String) args[middle + 1]).compareTo(key) > 0) {
                    return middle + 1;
                } else if (middle + 1 >= args.length - 1) {
                    return args.length;
                } else {
                    return binarySearchRightStr(args, middle + 1, stop, key);
                }
            } else {
                int i = middle + 1;
                while (((String) args[i]).compareTo(key) == 0 && i < args.length) {
                    i++;
                }
                return i;
            }
        }
        return -1; // should not happen
    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub

    }
}
