""" Generic Unification algorithm for expression trees with lists of children

This implementation is a direct translation of

Artificial Intelligence: A Modern Approach by Stuart Russel and Peter Norvig
Second edition, section 9.2, page 276

It is modified in the following ways:

1.  We allow associative and commutative Compound expressions. This results in
    combinatorial blowup.
2.  We explore the tree lazily.
3.  We provide generic interfaces to symbolic algebra libraries in Python.

A more traditional version can be found here
http://aima.cs.berkeley.edu/python/logic.html
"""

from __future__ import print_function, division

from sympy.utilities.iterables import kbins

class Compound(object):
    """ A little class to represent an interior node in the tree

    This is analagous to SymPy.Basic for non-Atoms
    """
    def __init__(self, op, args):
        self.op = op
        self.args = args

    def __eq__(self, other):
        return (type(self) == type(other) and self.op == other.op and
                self.args == other.args)

    def __hash__(self):
        return hash((type(self), self.op, self.args))

    def __str__(self):
        return "%s[%s]" % (str(self.op), ', '.join(map(str, self.args)))

class Variable(object):
    """ A Wild token """
    def __init__(self, arg):
        self.arg = arg

    def __eq__(self, other):
        return type(self) == type(other) and self.arg == other.arg

    def __hash__(self):
        return hash((type(self), self.arg))

    def __str__(self):
        return "Variable(%s)" % str(self.arg)

class CondVariable(object):
    """ A wild token that matches conditionally

    arg   - a wild token
    valid - an additional constraining function on a match
    """
    def __init__(self, arg, valid):
        self.arg = arg
        self.valid = valid

    def __eq__(self, other):
        return (type(self) == type(other) and
                self.arg == other.arg and
                self.valid == other.valid)

    def __hash__(self):
        return hash((type(self), self.arg, self.valid))

    def __str__(self):
        return "CondVariable(%s)" % str(self.arg)

def unify(x, y, s=None):
    """ Unify two expressions

    inputs:
        x, y - expression trees containing leaves, Compounds and Variables
        s    - a mapping of variables to subtrees
    outputs:
        lazy sequence of mappings {Variable: subtree}

    Example
    =======

    >>> from sympy.unify.core import unify, Compound, Variable
    >>> expr    = Compound("Add", ("x", "y"))
    >>> pattern = Compound("Add", ("x", Variable("a")))
    >>> next(unify(expr, pattern, {}))
    {Variable(a): 'y'}
    """
    s = s or {}

    if x == y:
        yield s
    elif isinstance(x, (Variable, CondVariable)):
        for match in unify_var(x, y, s):
            yield match
    elif isinstance(y, (Variable, CondVariable)):
        for match in unify_var(y, x, s):
            yield match
    elif isinstance(x, Compound) and isinstance(y, Compound):
        for sop in unify(x.op, y.op, s):
            if is_associative(x) and is_associative(y):
                a, b = (x, y) if len(x.args) < len(y.args) else (y, x)
                for aaargs, bbargs in allcombinations(a.args, b.args, 'associative'):
                    aa = [unpack(Compound(a.op, arg)) for arg in aaargs]
                    bb = [unpack(Compound(b.op, arg)) for arg in bbargs]
                    for match in unify(aa, bb, sop):
                        yield match
            elif len(x.args) == len(y.args):
                for match in unify(x.args, y.args, sop):
                    yield match

    elif is_args(x) and is_args(y) and len(x) == len(y):
        if len(x) == 0:
            yield s
        else:
            for shead in unify(x[0], y[0], s):
                for match in unify(x[1:], y[1:], shead):
                    yield match

def unify_var(var, x, s):
    if var in s:
        for match in unify(s[var], x, s):
            yield match
    elif isinstance(var, Variable):
        yield assoc(s, var, x)

def occur_check(var, x):
    """ var occurs in subtree owned by x? """
    if var == x:
        return True
    elif isinstance(x, Compound):
        return occur_check(var, x.args)
    elif is_args(x):
        if _any(occur_check(var, xi) for xi in x): return True
    return False

def _any(iterable):
    for i in iterable:
        if i:
            return True
    return False

def assoc(d, key, val):
    """ Return copy of d with key associated to val """
    d = d.copy()
    d[key] = val
    return d

def is_args(x):
    """ Is x a traditional iterable? """
    return type(x) == list or type(x) == tuple

def unpack(x):
    if isinstance(x, Compound) and len(x.args) == 1:
        return x.args[0]
    else:
        return x

def is_associative(x):
    return isinstance(x, Compound) and (x.op in ('Add', 'Mul', 'CAdd', 'CMul'))
def is_commutative(x):
    return isinstance(x, Compound) and (x.op in ('CAdd', 'CMul'))

def allcombinations(A, B, ordered):
    """
    Restructure A and B to have the same number of elements

    ordered must be either 'commutative' or 'associative'

    A and B can be rearranged so that the larger of the two lists is
    reorganized into smaller sublists.

    >>> from sympy.unify.core import allcombinations
    >>> for x in allcombinations((1, 2, 3), (5, 6), 'associative'): print(x)
    (((1,), (2, 3)), ((5,), (6,)))
    (((1, 2), (3,)), ((5,), (6,)))

    >>> for x in allcombinations((1, 2, 3), (5, 6), 'commutative'): print(x)
        (((1,), (2, 3)), ((5,), (6,)))
        (((1, 2), (3,)), ((5,), (6,)))
        (((1,), (3, 2)), ((5,), (6,)))
        (((1, 3), (2,)), ((5,), (6,)))
        (((2,), (1, 3)), ((5,), (6,)))
        (((2, 1), (3,)), ((5,), (6,)))
        (((2,), (3, 1)), ((5,), (6,)))
        (((2, 3), (1,)), ((5,), (6,)))
        (((3,), (1, 2)), ((5,), (6,)))
        (((3, 1), (2,)), ((5,), (6,)))
        (((3,), (2, 1)), ((5,), (6,)))
        (((3, 2), (1,)), ((5,), (6,)))
    """

    sm, bg = (A, B) if len(A) < len(B) else (B, A)
    for part in kbins(range(len(bg)), len(sm), None):
        if bg == B:
            yield list((a,) for a in A), core_partition(B, part)
        else:
            yield core_partition(A, part), list((b,) for b in B)

def core_partition(it, part):
    """ Partition a tuple/list into pieces defined by indices

    >>> from sympy.unify.core import partition
    >>> partition((10, 20, 30, 40), [[0, 1, 2], [3]])
    ((10, 20, 30), (40,))
    """
    return [index(it, ind) for ind in part]

def index(it, ind):
    """ Fancy indexing into an indexable iterable (tuple, list)

    >>> from sympy.unify.core import index
    >>> index([10, 20, 30], (1, 2, 0))
    [20, 30, 10]
    """
    return [it[i] for i in ind]
